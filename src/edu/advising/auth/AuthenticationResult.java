package edu.advising.auth;

import edu.advising.users.User;

/**
 * AuthenticationResult - returned by all authentication attempts
 * Contains state and token for stateless tracking
 */
public class AuthenticationResult {
    private AuthenticationState state;
    private String authToken; // JWT or session token for stateless tracking
    private String message;
    private User user;

    public AuthenticationResult(AuthenticationState state, String message) {
        this.state = state;
        this.message = message;
    }

    public static AuthenticationResult failed(String message) {
        return new AuthenticationResult(AuthenticationState.FAILED, message);
    }

    public static AuthenticationResult awaitingTwoFactor(String authToken) {
        AuthenticationResult result = new AuthenticationResult(
                AuthenticationState.AWAITING_TWO_FACTOR,
                "2FA code required");
        result.authToken = authToken;
        return result;
    }

    public static AuthenticationResult success(User user) {
        AuthenticationResult result = new AuthenticationResult(
                AuthenticationState.FULLY_AUTHENTICATED,
                "Authentication successful");
        result.user = user;
        return result;
    }

    // Getters
    public AuthenticationState getState() { return state; }
    public String getAuthToken() { return authToken; }
    public String getMessage() { return message; }
    public User getUser() { return user; }
    public boolean isFullyAuthenticated() {
        return state == AuthenticationState.FULLY_AUTHENTICATED;
    }
    public boolean requiresTwoFactor() {
        return state == AuthenticationState.AWAITING_TWO_FACTOR;
    }
}