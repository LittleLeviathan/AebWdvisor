package edu.advising.state;

import edu.advising.auth.AuthenticationContext;
import edu.advising.users.User;
import java.util.ArrayDeque;
import java.util.Deque;
import edu.advising.BetterAdvisorApp;

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
        currentState = GuestViewState.INSTANCE;
        currentState.enter(this);
        currentState.render();
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
        if (newState.requiresAuthentication() && currentUser == null) {
            navigateTo(GuestViewState.INSTANCE);
            return;
        }
        if (currentState != null) {
            currentState.exit(this);
            history.push(currentState);
        }
        currentState = newState;
        currentState.enter(this);
        currentState.render();
        updateScene(newState);
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
        if (history.isEmpty()) {
            System.out.println("No previous view to go back to.");
            return;
        }
        ViewState previous = history.pop();
        currentState.exit(this);
        currentState = previous;
        currentState.enter(this);
        currentState.render();
        updateScene(currentState);
    }

    /**
     * Logs the user out and returns to GuestViewState
     * - Set currentUser to null
     * - Clear the history stack
     * - Navigate to GuestViewState.INSTANCE
     */
    public void logout() {
        currentUser = null;
        history.clear();
        if (currentState != null) {
            currentState.exit(this);
        }
        currentState = GuestViewState.INSTANCE;
        currentState.enter(this);
        currentState.render();
        updateScene(GuestViewState.INSTANCE);
    }

    /**
     * Delegates the action to the current state to handle
     * - Call currentState.handleAction() with the action and args
     */
    public void handleAction(String action, String... args) {
        currentState.handleAction(this, action, args);
    }

    /**
     * Renders the current view
     * - Call currentState.render()
     */
    public void render() {
        currentState.render();
    }
    /**
     * Navigates to a new state without pushing current state to history.
     * Used for login transitions so GuestViewState doesn't end up in history.
     */
    public void navigateToWithoutHistory(ViewState newState) {
        if (currentState != null) {
            currentState.exit(this);
        }
        currentState = newState;
        currentState.enter(this);
        currentState.render();
        updateScene(newState);
    }
    private void updateScene(ViewState newState) {
        if (BetterAdvisorApp.primaryStage == null) return;

        javafx.scene.Scene finalScene;

        switch (newState.getViewName()) {
            case "GUEST":
            case "LOGIN":
                finalScene = edu.advising.gui.LoginScreen.getScene();
                break;
            case "STUDENT_DASHBOARD":
                finalScene = edu.advising.gui.StudentDashboardScreen.getScene(currentUser);
                break;
            case "FACULTY_DASHBOARD":
                finalScene = edu.advising.gui.FacultyDashboardScreen.getScene(currentUser);
                break;
            case "REGISTRATION":
                finalScene = edu.advising.gui.RegistrationScreen.getScene(currentUser);
                break;
            case "TRANSCRIPT":
                finalScene = edu.advising.gui.TranscriptScreen.getScene(currentUser);
                break;
            case "SCHEDULE":
                finalScene = edu.advising.gui.StudentScheduleScreen.getScene(currentUser);
                break;
            case "PERMISSION_MANAGEMENT":
                finalScene = edu.advising.gui.PermissionManagementScreen.getScene(currentUser);
                break;
            default:
                return;
        }

        javafx.application.Platform.runLater(() -> {
            BetterAdvisorApp.primaryStage.setScene(finalScene);
        });
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