package edu.advising.auth;

import edu.advising.common.ValidationResult;
import edu.advising.core.DatabaseManager;
import edu.advising.users.User;
import edu.advising.users.UserFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;

/**
 * SecureAuthentication - Concrete Strategy
 * SHA-256 hashed password authentication (production-ready)
 */
public class SecureAuthentication implements AuthenticationStrategy {
    private DatabaseManager dbManager;
    private UserFactory userFactory = new UserFactory();

    public SecureAuthentication() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public AuthenticationResult authenticate(String username, String password) {
        try {
            String sql = "SELECT password FROM users WHERE username = ?";
            return dbManager.executeQuery(sql, rs -> {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String inputHash = hashPassword(password);
                    if (storedHash.equals(inputHash)) {
                        User user = userFactory.getUserByUsername(username);
                        return AuthenticationResult.success(user);
                    }
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
        return AuthenticationResult.failed("Secure auth doesn't support continuation");
    }

    @Override
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
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
