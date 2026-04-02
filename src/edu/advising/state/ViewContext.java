package edu.advising.state;

import edu.advising.auth.AuthenticationContext;
import edu.advising.users.User;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * ViewContext - State Pattern Context (Week 6 - User Story #34)
 * Holds the current view state and navigation history.
 * All navigation in the portal goes through this class.
 */
public class ViewContext {

    // The screen the user is currently on
    private ViewState currentState;

    // Stack of previous screens so we can go back
    private Deque<ViewState> history = new ArrayDeque<>();

    // The user that is currently logged in (null if not logged in)
    private User currentUser;

    // The authentication context used to log users in
    private AuthenticationContext authContext;

    /**
     * Starts the application on the GuestViewState
     * - Set currentState to GuestViewState.INSTANCE
     * - Call enter() on GuestViewState
     * - Call render() to show the guest screen
     */
    public void start() {
        // TODO: implement
    }

    /**
     * Navigates to a new view state
     * - Check if the new state requiresAuthentication()
     *   - If yes and currentUser is null, navigate to GuestViewState instead
     * - Call exit() on the current state
     * - Push the current state onto the history stack
     * - Set currentState to the new state
     * - Call enter() on the new state
     * - Call render() on the new state
     */
    public void navigateTo(ViewState newState) {
        // TODO: implement
    }

    /**
     * Goes back to the previous view
     * - If history stack is empty, do nothing
     * - Pop the previous state off the history stack
     * - Call exit() on the current state
     * - Set currentState to the popped state
     * - Call enter() on the previous state
     * - Call render() on the previous state
     */
    public void back() {
        // TODO: implement
    }

    /**
     * Logs the user out and returns to GuestViewState
     * - Set currentUser to null
     * - Clear the history stack
     * - Navigate to GuestViewState.INSTANCE
     */
    public void logout() {
        // TODO: implement
    }

    /**
     * Delegates the action to the current state to handle
     * - Call currentState.handleAction() with the action and args
     */
    public void handleAction(String action, String... args) {
        // TODO: implement
    }

    /**
     * Renders the current view
     * - Call currentState.render()
     */
    public void render() {
        // TODO: implement
    }

    // ----------------------------------------------------------------
    // Getters and Setters
    // ----------------------------------------------------------------

    public ViewState getCurrentState() { return currentState; }
    public void setCurrentState(ViewState state) { this.currentState = state; }
    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User user) { this.currentUser = user; }
    public AuthenticationContext getAuthContext() { return authContext; }
    public void setAuthContext(AuthenticationContext authContext) { this.authContext = authContext; }
    public Deque<ViewState> getHistory() { return history; }
}