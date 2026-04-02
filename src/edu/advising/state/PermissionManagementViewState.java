package edu.advising.state;

/**
 * PermissionManagementViewState - Concrete State (Week 6 - User Story #34)
 * The screen where faculty can approve or deny waitlist permission requests.
 * Implemented as a singleton since it is stateless.
 */
public class PermissionManagementViewState implements ViewState {

    // Singleton instance - only one ever exists
    public static final PermissionManagementViewState INSTANCE = new PermissionManagementViewState();

    // Private constructor prevents anyone from creating a new instance
    private PermissionManagementViewState() {}

    /**
     * Called when the app navigates to the permission management screen
     * - Print "Navigating to Permission Management"
     */
    @Override
    public void enter(ViewContext context) {
        // TODO: implement
    }

    /**
     * Called when the app navigates away from the permission management screen
     * - Print "Leaving Permission Management"
     */
    @Override
    public void exit(ViewContext context) {
        // TODO: implement
    }

    /**
     * Displays the permission management screen options
     * - Print "=== Permission Management ==="
     * - Print available actions: APPROVE, DENY, BACK, LOGOUT
     */
    @Override
    public void render() {
        // TODO: implement
    }

    /**
     * Handles actions on the permission management screen
     * - If action is "APPROVE":
     *   - Get permissionId from Integer.parseInt(args[0])
     *   - Call FacultyPermissionContext.load(permissionId).approve()
     * - If action is "DENY":
     *   - Get permissionId from Integer.parseInt(args[0])
     *   - Get reason from args[1]
     *   - Call FacultyPermissionContext.load(permissionId).deny(reason)
     * - If action is "BACK":
     *   - Call context.back()
     * - If action is "LOGOUT":
     *   - Call context.logout()
     * - Otherwise print "Unknown action: " + action
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
        return "PERMISSION_MANAGEMENT";
    }

    /**
     * Permission management screen requires authentication
     */
    @Override
    public boolean requiresAuthentication() {
        return true;
    }
}