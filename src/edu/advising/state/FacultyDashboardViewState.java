package edu.advising.state;

import java.sql.SQLException;

/**
 * FacultyDashboardViewState - Concrete State (Week 6 - User Story #34)
 * The main dashboard screen for logged in faculty members.
 * Implemented as a singleton since it is stateless.
 */
public class FacultyDashboardViewState implements ViewState {

    // Singleton instance - only one ever exists
    public static final FacultyDashboardViewState INSTANCE = new FacultyDashboardViewState();

    // Private constructor prevents anyone from creating a new instance
    private FacultyDashboardViewState() {}

    /**
     * Called when the app navigates to the faculty dashboard
     * - Print "Welcome to your Faculty Dashboard"
     */
    @Override
    public void enter(ViewContext context) {
        System.out.println("[FacultyDashboardViewState] Welcome to your Faculty Dashboard.");
    }

    /**
     * Called when the app navigates away from the faculty dashboard
     * - Print "Leaving Faculty Dashboard"
     */
    @Override
    public void exit(ViewContext context) {
        System.out.println("[FacultyDashboardViewState] Leaving Faculty Dashboard.");
    }

    /**
     * Displays the faculty dashboard options
     * - Print "=== Faculty Dashboard ==="
     * - Print available actions: NAVIGATE PERMISSIONS, LOGOUT
     */
    @Override
    public void render() {
        System.out.println("=== Faculty Dashboard ===");
        System.out.println("Available actions: NAVIGATE PERMISSIONS, LOGOUT");
    }

    /**
     * Handles actions on the faculty dashboard
     * - If action is "NAVIGATE" and args[0] is "PERMISSIONS":
     *   - Call context.navigateTo(PermissionManagementViewState.INSTANCE)
     * - If action is "LOGOUT":
     *   - Call context.logout()
     * - Otherwise print "Unknown action: " + action
     */
    @Override
    public void handleAction(ViewContext context, String action, String... args) throws SQLException, IllegalAccessException {
        switch (action) {
            case "NAVIGATE":
                if (args.length > 0 && "PERMISSIONS".equals(args[0])) {
                    context.navigateTo(PermissionManagementViewState.INSTANCE);
                } else {
                    System.out.println("[FacultyDashboardViewState] Unknown navigation target: " + (args.length > 0 ? args[0] : "none"));
                }
                break;
            case "LOGOUT":
                context.logout();
                break;
            default:
                System.out.println("[FacultyDashboardViewState] Unknown action: " + action);
                break;
        }
    }

    /**
     * Returns the name of this view
     */
    @Override
    public String getViewName() {
        return "FACULTY_DASHBOARD";
    }

    /**
     * Faculty dashboard requires authentication
     */
    @Override
    public boolean requiresAuthentication() {
        return true;
    }
}