package edu.advising.state;

/**
 * StudentDashboardViewState - Concrete State (Week 6 - User Story #34)
 * The main dashboard screen for logged in students.
 * Implemented as a singleton since it is stateless.
 */
public class StudentDashboardViewState implements ViewState {

    // Singleton instance - only one ever exists
    public static final StudentDashboardViewState INSTANCE = new StudentDashboardViewState();

    // Private constructor prevents anyone from creating a new instance
    private StudentDashboardViewState() {}

    /**
     * Called when the app navigates to the student dashboard
     * - Print "Welcome to your Student Dashboard"
     */
    @Override
    public void enter(ViewContext context) {
        // TODO: implement
    }

    /**
     * Called when the app navigates away from the student dashboard
     * - Print "Leaving Student Dashboard"
     */
    @Override
    public void exit(ViewContext context) {
        // TODO: implement
    }

    /**
     * Displays the student dashboard options
     * - Print "=== Student Dashboard ==="
     * - Print available actions: NAVIGATE REGISTRATION, NAVIGATE TRANSCRIPT, LOGOUT
     */
    @Override
    public void render() {
        // TODO: implement
    }

    /**
     * Handles actions on the student dashboard
     * - If action is "NAVIGATE" and args[0] is "REGISTRATION":
     *   - Call context.navigateTo(RegistrationViewState.INSTANCE)
     * - If action is "NAVIGATE" and args[0] is "TRANSCRIPT":
     *   - Call context.navigateTo(TranscriptViewState.INSTANCE)
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
        return "STUDENT_DASHBOARD";
    }

    /**
     * Student dashboard requires authentication
     */
    @Override
    public boolean requiresAuthentication() {
        return true;
    }
}