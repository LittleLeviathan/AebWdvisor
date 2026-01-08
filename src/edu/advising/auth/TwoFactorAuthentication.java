package edu.advising.auth;

import edu.advising.core.DatabaseManager;
import edu.advising.users.User;
import edu.advising.users.UserFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * TwoFactorAuthentication - Concrete Strategy
 * Two-factor authentication with temporary codes (simulated)
 */
public class TwoFactorAuthentication implements AuthenticationStrategy {
    private AuthenticationStrategy baseAuth;
    private DatabaseManager dbManager = DatabaseManager.getInstance();
    private UserFactory userFactory = new UserFactory();
    private static final int CODE_VALIDITY_MINUTES = 5; // 5 minutes

    public TwoFactorAuthentication(AuthenticationStrategy baseAuth) {
        this.baseAuth = baseAuth;
    }

    /**
     * Step 1: Authenticate with username/password, then send 2FA code
     */
    @Override
    public AuthenticationResult authenticate(String username, String password) {
        // First, validate with base authentication
        AuthenticationResult baseResult = baseAuth.authenticate(username, password);
        if (!baseResult.isFullyAuthenticated()) {
            return baseResult; // Password was wrong
        }
        // Let's get the user that just authenticated.
        User user = baseResult.getUser();

        try {
            // Generate auth token for stateless tracking
            String authToken = generateAuthToken();

            // Generate and store 2FA code
            String twoFactorCode = generateTwoFactorCode();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expires = now.plusMinutes(CODE_VALIDITY_MINUTES);
            // TODO: Update DB Manager to CREATE auth_sessions table, or use something like Redis
            // Store auth session in database
            String insertSession = "INSERT INTO auth_sessions (auth_token, user_id, state, expires_at) " +
                    "VALUES (?, ?, ?, ?)";
            //TODO: Get Enum label for AWAITING_TWO_FACTOR instead of this string.
            dbManager.executeUpdate(
                    insertSession, authToken, user.getId(), "AWAITING_TWO_FACTOR",
                    Timestamp.valueOf(expires));

            // Store 2FA code
            String sql = "INSERT INTO two_factor_codes (user_id, code, code_type, generated_at, expires_at) " +
                    "VALUES (?, ?, ?, ?, ?)";

            int codeId = dbManager.executeInsert(sql, user.getId(), twoFactorCode, "SMS",
                    Timestamp.valueOf(now), Timestamp.valueOf(expires));
            System.out.printf("Generated codeId: %d%n", codeId);

            // Notify the user of this code.
            sendTwoFactorCode(user, twoFactorCode); // SMS, email, etc.

            System.out.println("✓ 2FA code sent. Please verify with code");
            return AuthenticationResult.awaitingTwoFactor(authToken); // Not fully authenticated with this strategy.
        } catch (SQLException e) {
            System.err.println("✗ Error generating 2FA code: " + e.getMessage());
            return AuthenticationResult.failed("2FA setup error");
        }
    }

    /**
     * Step 2: Verify 2FA code using auth token
     */
    @Override
    public AuthenticationResult continueAuthentication(String authToken, String code) {
        try {
            // Retrieve session from database
            String sessionSql = "SELECT user_id, state FROM auth_sessions " +
                    "WHERE auth_token = ? AND expires_at > CURRENT_TIMESTAMP";
            return dbManager.executeQuery(sessionSql, sessionRs -> {
                if (!sessionRs.next()) {
                    return AuthenticationResult.failed("Invalid or expired auth token");
                }

                int userId = sessionRs.getInt("user_id");
                String state = sessionRs.getString("state");

                //TODO: Make this an Enum state comparison.
                if (!"AWAITING_TWO_FACTOR".equals(state)) {
                    return AuthenticationResult.failed("Invalid authentication state");
                }

                // Verify 2FA code
                String codeSql = "SELECT id FROM two_factor_codes " +
                        "WHERE user_id = ? AND code = ? AND is_used = FALSE " +
                        "AND expires_at > CURRENT_TIMESTAMP";
                return dbManager.executeQuery(codeSql, codeRs -> {
                    if (!codeRs.next()) {
                        // Increment failed attempts
                        incrementFailedAttempts(userId);
                        return AuthenticationResult.failed("Invalid or expired 2FA code");
                    }

                    int codeId = codeRs.getInt("id");

                    // Mark code as used
                    String markUsed = "UPDATE two_factor_codes SET is_used = TRUE, " +
                            "used_at = CURRENT_TIMESTAMP WHERE id = ?";
                    dbManager.executeUpdate(markUsed, codeId);

                    // Update session state
                    String updateSession = "UPDATE auth_sessions SET state = ? WHERE auth_token = ?";
                    dbManager.executeUpdate(updateSession, "FULLY_AUTHENTICATED", authToken);

                    // Get user and return success
                    User user = userFactory.getUserById(userId);
                    System.out.println("✓ 2FA verification successful");
                    return AuthenticationResult.success(user);

                }, userId, code);
            }, authToken);
        } catch (SQLException e) {
            System.err.println("Error verifying 2FA: " + e.getMessage());
            return AuthenticationResult.failed("2FA verification error");
        }
    }

    @Override
    public String hashPassword(String password) {
        return baseAuth.hashPassword(password);
    }

    @Override
    public boolean validatePasswordStrength(String password) {
        return baseAuth.validatePasswordStrength(password);
    }

    // Helper methods

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    private String generateTwoFactorCode() {
        return String.format("%06d", (int) (Math.random() * 900000) + 100000);
    }

    private void sendTwoFactorCode(User user, String code) {
        // In real system: twilioService.sendSMS(user.getPhone(), code);
        System.out.printf("📱 SMS sent to user %s with code: %s (valid for %d minutes)%n",
                user.getEmail(), code, CODE_VALIDITY_MINUTES);
    }

    private void incrementFailedAttempts(int userId) throws SQLException {
        String sql = "UPDATE two_factor_codes SET attempts = attempts + 1 " +
                "WHERE user_id = ? AND is_used = FALSE " +
                "ORDER BY generated_at DESC LIMIT 1";
        dbManager.executeUpdate(sql, userId);
    }
}
