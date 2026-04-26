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
        System.out.println("[PermissionManagementViewState] Navigating to Permission Management.");
    }

    /**
     * Called when the app navigates away from the permission management screen
     * - Print "Leaving Permission Management"
     */
    @Override
    public void exit(ViewContext context) {
        System.out.println("[PermissionManagementViewState] Leaving Permission Management.");
    }

    /**
     * Displays the permission management screen options
     * - Print "=== Permission Management ==="
     * - Print available actions: APPROVE, DENY, BACK, LOGOUT
     */
    @Override
    public void render() {
        System.out.println("=== Permission Management ===");
        System.out.println("Available actions: APPROVE <permissionId>, DENY <permissionId> <reason>, BACK, LOGOUT");
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
        switch (action) {
            case "APPROVE":
                if (args.length >= 1) {
                    int permissionId = Integer.parseInt(args[0]);
                    // TODO: uncomment when FacultyPermissionContext is merged
                    // FacultyPermissionContext permCtx = FacultyPermissionContext.load(permissionId);
                    // permCtx.approve();
                    System.out.println("Permission " + permissionId + " approved.");
                } else {
                    System.out.println("[PermissionManagementViewState] APPROVE requires a permissionId.");
                }
                break;
            case "DENY":
                if (args.length >= 2) {
                    int permissionId = Integer.parseInt(args[0]);
                    String reason = args[1];
                    // TODO: uncomment when FacultyPermissionContext is merged
                    // FacultyPermissionContext permCtx = FacultyPermissionContext.load(permissionId);
                    // permCtx.deny(reason);
                    System.out.println("Permission " + permissionId + " denied.");
                } else {
                    System.out.println("[PermissionManagementViewState] DENY requires a permissionId and reason.");
                }
                break;
            case "BACK":
                context.back();
                break;
            case "LOGOUT":
                context.logout();
                break;
            default:
                System.out.println("[PermissionManagementViewState] Unknown action: " + action);
                break;
        }
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