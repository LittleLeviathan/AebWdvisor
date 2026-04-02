package edu.advising.state;

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
        // TODO: implement
    }

    /**
     * Called when the app navigates away from the registration screen
     * - Print "Leaving Registration"
     */
    @Override
    public void exit(ViewContext context) {
        // TODO: implement
    }

    /**
     * Displays the registration screen options
     * - Print "=== Registration ==="
     * - Print available actions: CHECK_STATUS, BACK, LOGOUT
     */
    @Override
    public void render() {
        // TODO: implement
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
    public void handleAction(ViewContext context, String action, String... args) {
        // TODO: implement
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