package edu.advising.auth;

import edu.advising.common.ValidationResult;
import edu.advising.core.DatabaseManager;
import edu.advising.users.User;
import edu.advising.users.UserFactory;

import java.sql.SQLException;

/**
 * BasicAuthentication - Concrete Strategy
 * Simple username/password authentication (for development/testing)
 */
public class BasicAuthentication implements AuthenticationStrategy {
    private DatabaseManager dbManager;
    private UserFactory userFactory = new UserFactory();

    public BasicAuthentication() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public AuthenticationResult authenticate(String username, String password) {
        try {
            String sql = "SELECT password FROM users WHERE username = ?";
            return dbManager.executeQuery(sql, rs -> {
                if (rs.next() && rs.getString("password").equals(password)) {
                    User user = userFactory.getUserByUsername(username);
                    return AuthenticationResult.success(user);
                }
                return AuthenticationResult.failed("Invalid credentials");
            }, username);
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
            return AuthenticationResult.failed("Authentication error");
        }
    }

    @Override
    public AuthenticationResult continueAuthentication(String authToken, String credential) {
        return AuthenticationResult.failed("Basic auth doesn't support continuation");
    }


    @Override
    public String hashPassword(String password) {
        // Basic strategy: no hashing (not secure, for demo only)
        return password;
    }

    @Override
    public boolean validatePasswordStrength(String password) {
        // Strong validation: length, uppercase, lowercase, digit, special char
        if (password == null) {
            return false;
        }
        try {
            ValidationResult vr = PasswordPolicyValidator.validateAgainstPolicy(password);
            return vr.isValid();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}