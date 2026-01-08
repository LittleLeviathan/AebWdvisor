import edu.advising.auth.*;
import edu.advising.core.DatabaseManager;
import edu.advising.users.User;
import edu.advising.users.UserFactory;

import java.sql.SQLException;
import java.util.Scanner;

// Test driver
public class Week3Test {
    public static void main(String[] args) {
        // Setup: Create test user with BasicAuthentication
        AuthenticationContext authContext = new AuthenticationContext(new BasicAuthentication());
        UserFactory factory = new UserFactory();

        // Create test user
        User student;
        String pass1 = "Test123!";
        String pass2 = "newpass";
        try {
            student = factory.createUser("STUDENT", "alice", pass1,
                    "alice@college.edu", "Alice", "Johnson", "S54321");
        } catch (RuntimeException re) {
            System.out.println(re.getMessage());
            // Swap passwords because it's likely the user already exists.
            String temp = pass1;
            pass1 = pass2;
            pass2 = temp;
            student = factory.getUserByUsername("alice");
        }

        System.out.println("=== Testing Basic Authentication ===");
        AuthenticationResult success = authContext.login("alice", pass1, "127.0.0.1");
        System.out.println("Login result: " + success.getMessage());

        if (success.isFullyAuthenticated()) {
            success.getUser().showDashboard();
            authContext.logout();
        }

        System.out.println("\n=== Testing Password Change ===");
        authContext.login("alice", pass1, "127.0.0.1");
        try {
            authContext.changePassword("alice", pass1, "newpass");
        } catch(SQLException se) {
            System.out.printf("=== ERROR ===%n%s%n=== ERROR ===%n", se.getMessage());
        }

        System.out.println("\n=== Switching to Secure Authentication ===");
        authContext.setStrategy(new SecureAuthentication());

        // Create another user with hashed password
        User faculty;
        try {
            faculty = factory.createUser("FACULTY", "profbrown",
                    new SecureAuthentication().hashPassword("secure456"),
                    "brown@college.edu", "Robert", "Brown", "F11111", "Mathematics");
        } catch (RuntimeException re) {
            System.out.println(re.getMessage());
            faculty = factory.getUserByUsername("profbrown");
        }

        success = authContext.login("profbrown", "secure456", "127.0.0.1");
        System.out.println("Login result: " + success.getMessage());

        System.out.println("\n=== Testing Two-Factor Authentication ===");
        AuthenticationContext athContext = new AuthenticationContext(
                new TwoFactorAuthentication(new SecureAuthentication()));
        AuthenticationResult authResult = athContext.login(
                "profbrown", "secure456", "127.0.0.1");
        if (authResult.requiresTwoFactor()) {
            // Step 2: Get code from user
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter 2FA code: ");
            String code = scanner.nextLine();

            AuthenticationResult finalResult = athContext.verify(authResult.getAuthToken(), code);

            if (finalResult.isFullyAuthenticated()) {
                System.out.println("Welcome, " + finalResult.getUser().getUsername());
            }
        }

        System.out.println("\n=== Testing Password Reset ===");
        // User requests password reset
        boolean initiated = authContext.initiatePasswordReset("profbrown", "brown@college.edu");
        if (initiated) {
            // In a REAL scenario
            // Token is stored in database and "sent" to user's email
            // User clicks link in email with token, submits new password
            Scanner s = new Scanner(System.in);
            System.out.println("Enter Your Reset Token: ");
            String token = s.nextLine();
            System.out.println("Enter Your New Password: ");
            String newPassword = s.nextLine();

            boolean resetSuccess = authContext.resetPasswordWithToken(token, newPassword);

            if (resetSuccess) {
                // User can now log in with new password
                authContext.login("profbrown", newPassword, "127.0.0.1");
            }
        }

        System.out.println("\n=== Testing Password Strength Validation ===");
        SecureAuthentication secureAuth = new SecureAuthentication();
        System.out.println("'test' is strong: " + secureAuth.validatePasswordStrength("test"));
        System.out.println("'Test123!' is strong: " + secureAuth.validatePasswordStrength("Test123!"));

        DatabaseManager.getInstance().shutdown();
    }
}
