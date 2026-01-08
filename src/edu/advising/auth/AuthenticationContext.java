package edu.advising.auth;

import edu.advising.core.DatabaseManager;
import edu.advising.users.User;
import edu.advising.users.UserFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * AuthenticationContext - Context Class
 * Manages the current authentication strategy
 */
public class AuthenticationContext {
    private AuthenticationStrategy strategy;
    private DatabaseManager dbManager;
    private UserFactory userFactory;

    public AuthenticationContext(AuthenticationStrategy strategy) {
        this.strategy = strategy;
        this.dbManager = DatabaseManager.getInstance();
        this.userFactory = new UserFactory();
    }

    // Allow runtime strategy switching
    public void setStrategy(AuthenticationStrategy strategy) {
        this.strategy = strategy;
        System.out.println("Authentication strategy changed to: " +
                strategy.getClass().getSimpleName());
    }

    public AuthenticationStrategy getStrategy() {
        return strategy;
    }

    /**
     * Login with current strategy
     */
    public AuthenticationResult login(String username, String password, String ipAddress) {
        AuthenticationResult authResult =  strategy.authenticate(username, password);

        // Log attempt
        try {
            String sql = "INSERT INTO login_attempts (username, status, ip_address, failure_reason) " +
                    "VALUES (?, ?, ?, ?)";
            dbManager.executeInsert(sql, username, authResult.getState().name(), ipAddress, authResult.getMessage());
            if (authResult.isFullyAuthenticated()) {
                // Update last_login
                User user = authResult.getUser();
                String updateSql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?";
                dbManager.executeUpdate(updateSql, user.getId());
            }
        } catch (SQLException e) {
            System.err.println("Failed to log attempt: " + e.getMessage());
        }

        return authResult;
    }

    /**
     * Continue authentication with additional credential
     */
    public AuthenticationResult verify(String authToken, String credential) {
        return strategy.continueAuthentication(authToken, credential);
    }

    private boolean isPasswordInHistory(int userId, String newHash) throws SQLException {
        String sql = "SELECT password_hash FROM password_history " +
                "WHERE user_id = ? ORDER BY changed_at DESC LIMIT 5";

        return dbManager.executeQuery(sql, rs -> {
            while (rs.next()) {
                if (rs.getString("password_hash").equals(newHash)) {
                    return true; // Password was used recently
                }
            }
            return false;
        }, userId);
    }

    /**
     * Change password functionality
     */
    public boolean changePassword(String username, String oldPassword, String newPassword) throws SQLException {
        // Verify old password
        AuthenticationResult authResult = strategy.authenticate(username, oldPassword);
        if (!authResult.isFullyAuthenticated()) {
            System.out.println("Old password is incorrect");
            return false;
        }
        // Now that the user is authenticated, get the user object to verify history.
        User user = authResult.getUser();
        // Use auth strategy to get our new password hash for old pass verification/update.
        String newHash = strategy.hashPassword(newPassword);
        // Verify that this is not an old password re-used.
        if (isPasswordInHistory(user.getId(), newHash)) {
            System.out.println("Cannot reuse recent passwords");
            return false;
        }

        // Validate new password strength
        if (!strategy.validatePasswordStrength(newPassword)) {
            System.out.println("New password does not meet strength requirements");
            return false;
        }

        // Update password in database
        try {
            String updateSql = "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
            int updated = dbManager.executeUpdate(updateSql, newHash, user.getId());
            if (updated > 0) {
                System.out.println("Password changed successfully");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error changing password: " + e.getMessage());
        }
        return false;
    }

    /**
     * Password recovery - "What's My Password?" feature
     */
    public boolean initiatePasswordReset(String username, String email) {
        // Verify user exists and email matches
        User user = userFactory.getUserByUsername(username);
        if (user == null || !user.getEmail().equals(email)) {
            System.out.println("✗ User not found or email doesn't match");
            return false;
        }

        try {
            // Generate secure reset token
            String resetToken = generateResetToken();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiresAt = now.plusHours(24);

            // Invalidate any existing tokens for this user
            String invalidateSql = "UPDATE password_reset_tokens SET is_used = TRUE " +
                    "WHERE user_id = ? AND is_used = FALSE";
            dbManager.executeUpdate(invalidateSql, user.getId());

            // Store new reset token
            String insertSql = "INSERT INTO password_reset_tokens " +
                    "(user_id, token, expires_at) VALUES (?, ?, ?)";
            dbManager.executeUpdate(
                    insertSql, user.getId(), resetToken, Timestamp.valueOf(expiresAt));

            System.out.println("✓ Password reset link sent to: " + email);
            System.out.println("  Reset token: " + resetToken);
            System.out.println("  Expires: " + expiresAt);

            // In real system, send email with reset link:
            // emailService.sendPasswordResetEmail(email, resetToken);

            return true;

        } catch (SQLException e) {
            System.err.println("✗ Error creating reset token: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify reset token and allow password reset
     * @param token Reset token from email link
     * @param newPassword New password to set
     * @return true if reset successful, false if token invalid/expired
     */
    public boolean resetPasswordWithToken(String token, String newPassword) {
        try {
            // Find valid token
            String sql = "SELECT user_id, expires_at FROM password_reset_tokens " +
                    "WHERE token = ? AND is_used = FALSE";
            return dbManager.executeQuery(sql, rs -> {
                if (!rs.next()) {
                    System.out.println("✗ Invalid or already used reset token");
                    return false;
                }

                int userId = rs.getInt("user_id");
                Timestamp expiresAt = rs.getTimestamp("expires_at");

                // Check if token expired
                if (expiresAt.before(Timestamp.valueOf(LocalDateTime.now()))) {
                    System.out.println("✗ Reset token has expired");
                    return false;
                }

                // Validate new password strength
                if (!strategy.validatePasswordStrength(newPassword)) {
                    System.out.println("✗ New password does not meet requirements");
                    return false;
                }

                if( isPasswordInHistory(userId, newPassword) ) {
                    System.out.println("✗ Cannot reuse recent passwords");
                    return false;
                }

                // Hash and update password
                String hashedPassword = strategy.hashPassword(newPassword);
                String updatePasswordSql = "UPDATE users SET password = ?, updated_at = CURRENT_TIMESTAMP " +
                        "WHERE id = ?";
                dbManager.executeUpdate(updatePasswordSql, hashedPassword, userId);

                // Mark token as used
                String markUsedSql = "UPDATE password_reset_tokens SET is_used = TRUE, " +
                        "used_at = CURRENT_TIMESTAMP WHERE token = ?";
                dbManager.executeUpdate(markUsedSql, token);

                // Add to password history
                String historySql = "INSERT INTO password_history (user_id, password_hash) VALUES (?, ?)";
                dbManager.executeUpdate(historySql, userId, hashedPassword);

                System.out.println("✓ Password reset successful");
                return true;

            },token);

        } catch (SQLException e) {
            System.err.println("✗ Error resetting password: " + e.getMessage());
            return false;
        }
    }

    /**
     * Clean up expired reset tokens (should be run periodically)
     */
    public void cleanupExpiredTokens() {
        try {
            String sql = "DELETE FROM password_reset_tokens " +
                    "WHERE expires_at < CURRENT_TIMESTAMP AND is_used = FALSE";
            int deleted = dbManager.executeUpdate(sql);
            System.out.println("✓ Cleaned up " + deleted + " expired reset tokens");
        } catch (SQLException e) {
            System.err.println("Error cleaning up tokens: " + e.getMessage());
        }
    }

    public void logout() {
        //TODO: Figure out what it means to logout.
        // It probably means to delegate to the strategy a logout, which will likely make sure auth_session tables are
        // updated properly to reflect dead stateless sessions.
    }

    private String generateResetToken() {
        // Generate random token
        byte[] token = new byte[32];
        new java.security.SecureRandom().nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }


    private boolean isAccountLocked(String username) throws SQLException {
        String sql = "SELECT COUNT(*) as failed_count FROM login_attempts " +
                "WHERE username = ? AND status = 'FAILED' " +
                "AND attempt_time > DATEADD('MINUTE', -15, CURRENT_TIMESTAMP)";

        return dbManager.executeQuery(sql, rs -> {
            if (rs.next()) {
                return rs.getInt("failed_count") >= 5; // Lock after 5 failures in 15 min
            }
            return false;
        }, username);
    }
}
