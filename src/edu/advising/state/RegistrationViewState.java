package edu.advising.state;

import java.sql.SQLException;

/**
 * RegistrationViewState - Concrete State (Week 6 - User Story #34)
 * The registration screen where students can check registration status.
 * Implemented as a singleton since it is stateless.
 */
public class RegistrationViewState implements ViewState {

    // Singleton instance - only one ever exists
    public static final RegistrationViewState INSTANCE = new RegistrationViewState();

    // Private constructor prevents anyone from creating a new instance
    private RegistrationViewState() {}

    /**
     * Called when the app navigates to the registration screen
     * - Print "Navigating to Registration"
     */
    @Override
    public void enter(ViewContext context) {
        System.out.println("[RegistrationViewState] Navigating to Registration.");
    }

    /**
     * Called when the app navigates away from the registration screen
     * - Print "Leaving Registration"
     */
    @Override
    public void exit(ViewContext context) {
        System.out.println("[RegistrationViewState] Leaving Registration.");
    }

    /**
     * Displays the registration screen options
     * - Print "=== Registration ==="
     * - Print available actions: CHECK_STATUS, BACK, LOGOUT
     */
    @Override
    public void render() {
        System.out.println("=== Registration ===");
        System.out.println("Available actions: CHECK_STATUS <semester> <year>, BACK, LOGOUT");
    }

    /**
     * Handles actions on the registration screen
     * - If action is "CHECK_STATUS":
     *   - Get semester from args[0], year from Integer.parseInt(args[1])
     *   - Call RegistrationPeriodContext.currentPeriod(semester, year)
     *   - Print the current state name from the returned context
     * - If action is "BACK":
     *   - Call context.back()
     * - If action is "LOGOUT":
     *   - Call context.logout()
     * - Otherwise print "Unknown action: " + action
     */
    @Override
    public void handleAction(ViewContext context, String action, String... args) throws SQLException, IllegalAccessException {
        switch (action) {
            case "CHECK_STATUS":
                if (args.length >= 2) {
                    String semester = args[0];
                    int year = Integer.parseInt(args[1]);
                    RegistrationPeriodContext regCtx = RegistrationPeriodContext.currentPeriod(semester, year);
                    System.out.println("Registration status for " + semester + " " + year + ": "
                            + regCtx.getCurrentState().getStateName());
                } else {
                    System.out.println("[RegistrationViewState] CHECK_STATUS requires semester and year.");
                }
                break;
            case "BACK":
                context.back();
                break;
            case "LOGOUT":
                context.logout();
                break;
            default:
                System.out.println("[RegistrationViewState] Unknown action: " + action);
                break;
        }
    }

    /**
     * Returns the name of this view
     */
    @Override
    public String getViewName() {
        return "REGISTRATION";
    }

    /**
     * Registration screen requires authentication
     */
    @Override
    public boolean requiresAuthentication() {
        return true;
    }
}