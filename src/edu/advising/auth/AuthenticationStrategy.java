// Week 3: STRATEGY PATTERN
// Features Implemented: Multiple Authentication Methods, Change Password
// Why Now: Need flexible authentication system that can swap algorithms at runtime

package edu.advising.auth;

/**
 * AuthenticationStrategy - Strategy Interface
 * Defines the contract for all authentication algorithms
 */
public interface AuthenticationStrategy {
    /**
     * Initiate authentication - may return partial success if 2FA like algorithms needed
     */
    AuthenticationResult authenticate(String username, String password);
    /**
     * Continue authentication with additional factor(s)
     */
    AuthenticationResult continueAuthentication(String authToken, String credential);
    /**
     * Utility methods
     */
    String hashPassword(String password);
    boolean validatePasswordStrength(String password);
}

