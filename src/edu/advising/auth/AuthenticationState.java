package edu.advising.auth;

/**
 * Authentication State - represents where user is in auth flow
 */
enum AuthenticationState {
    UNAUTHENTICATED,
    PASSWORD_VERIFIED,
    AWAITING_TWO_FACTOR,
    FULLY_AUTHENTICATED,
    FAILED
}