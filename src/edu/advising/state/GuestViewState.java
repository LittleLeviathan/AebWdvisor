package edu.advising.state;

import edu.advising.auth.AuthenticationResult;
import edu.advising.users.User;

/**
 * GuestViewState - Concrete State (Week 6 - User Story #34)
 * The default view when no user is logged in.
 * Handles login and routes to the correct dashboard by role.
 * Implemented as a singleton since it is stateless.
 */
public class GuestViewState implements ViewState {

    // Singleton instance - only one ever exists
    public static final GuestViewState INSTANCE = new GuestViewState();

    // Private constructor prevents anyone from creating a new instance
    private GuestViewState() {}

    /**
     * Called when the app navigates to the guest screen
     * - Print "Navigating to Guest/Login screen"
     */
    @Override
    public void enter(ViewContext context) {
        // TODO: implement
    }

    /**
     * Called when the app navigates away from the guest screen
     * - Print "Leaving Guest screen"
     */
    @Override
    public void exit(ViewContext context) {
        // TODO: implement
    }

    /**
     * Displays the guest screen options
     * - Print "=== Welcome to BetterAdvisor ==="
     * - Print available actions: LOGIN, EXIT
     */
    @Override
    public void render() {
        // TODO: implement
    }

    /**
     * Handles actions on the guest screen
     * - If action is "LOGIN":
     *   - Get username from args[0], password from args[1], ip from args[2]
     *   - Call context.getAuthContext().login(username, password, ip)
     *   - If result is fully authenticated:
     *     - Call context.setCurrentUser(result.getUser())
     *     - Check the user's type
     *     - If STUDENT navigate to StudentDashboardViewState.INSTANCE
     *     - If FACULTY navigate to FacultyDashboardViewState.INSTANCE
     *   - If not authenticated print the failure message
     * - If action is "LOGOUT" do nothing, already on guest screen
     */
    @Override
    public void handleAction(ViewContext context, String action, String... args) {
        // TODO: implement
    }

    /**
     * Returns the name of this view
     */
    @Override
    public String getViewName() {
        return "GUEST";
    }

    /**
     * Guest screen does not require authentication
     */
    @Override
    public boolean requiresAuthentication() {
        return false;
    }
}