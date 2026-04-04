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
        System.out.println("[GuestViewState] Navigating to Guest/Login screen.");
    }

    /**
     * Called when the app navigates away from the guest screen
     * - Print "Leaving Guest screen"
     */
    @Override
    public void exit(ViewContext context) {
        System.out.println("[GuestViewState] Leaving Guest screen.");
    }

    /**
     * Displays the guest screen options
     * - Print "=== Welcome to BetterAdvisor ==="
     * - Print available actions: LOGIN, EXIT
     */
    @Override
    public void render() {
        System.out.println("=== Welcome to BetterAdvisor ===");
        System.out.println("Available actions: LOGIN, EXIT");
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
        switch (action) {
            case "LOGIN":
                String username = args[0];
                String password = args[1];
                String ip = args[2];
                AuthenticationResult result = context.getAuthContext().login(username, password, ip);
                if (result.isFullyAuthenticated()) {
                    context.setCurrentUser(result.getUser());
                    String userType = result.getUser().getUserType();
                    if ("STUDENT".equals(userType)) {
                        context.navigateToWithoutHistory(StudentDashboardViewState.INSTANCE);
                    } else if ("FACULTY".equals(userType)) {
                        context.navigateToWithoutHistory(FacultyDashboardViewState.INSTANCE);
                    } else {
                        System.out.println("Unknown user type: " + userType);
                    }
                } else {
                    System.out.println("Login failed: " + result.getMessage());
                }
                break;
            case "LOGOUT":
                System.out.println("[GuestViewState] Already on guest screen.");
                break;
            default:
                System.out.println("[GuestViewState] Unknown action: " + action);
                break;
        }
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