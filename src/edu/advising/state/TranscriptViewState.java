package edu.advising.state;

import java.sql.SQLException;

/**
 * TranscriptViewState - Concrete State (Week 6 - User Story #34)
 * The transcript screen where students can view their transcript.
 * Implemented as a singleton since it is stateless.
 */
public class TranscriptViewState implements ViewState {

    // Singleton instance - only one ever exists
    public static final TranscriptViewState INSTANCE = new TranscriptViewState();

    // Private constructor prevents anyone from creating a new instance
    private TranscriptViewState() {}

    /**
     * Called when the app navigates to the transcript screen
     * - Print "Navigating to Transcript"
     */
    @Override
    public void enter(ViewContext context) {
        System.out.println("[TranscriptViewState] Navigating to Transcript.");
    }

    /**
     * Called when the app navigates away from the transcript screen
     * - Print "Leaving Transcript"
     */
    @Override
    public void exit(ViewContext context) {
        System.out.println("[TranscriptViewState] Leaving Transcript.");
    }

    /**
     * Displays the transcript screen options
     * - Print "=== Transcript ==="
     * - Print available actions: BACK, LOGOUT
     */
    @Override
    public void render() {
        System.out.println("=== Transcript ===");
        System.out.println("Available actions: BACK, LOGOUT");
    }

    /**
     * Handles actions on the transcript screen
     * - If action is "BACK":
     *   - Call context.back()
     * - If action is "LOGOUT":
     *   - Call context.logout()
     * - Otherwise print "Unknown action: " + action
     */
    @Override
    public void handleAction(ViewContext context, String action, String... args) throws SQLException, IllegalAccessException {
        switch (action) {
            case "BACK":
                context.back();
                break;
            case "LOGOUT":
                context.logout();
                break;
            default:
                System.out.println("[TranscriptViewState] Unknown action: " + action);
                break;
        }
    }

    /**
     * Returns the name of this view
     */
    @Override
    public String getViewName() {
        return "TRANSCRIPT";
    }

    /**
     * Transcript screen requires authentication
     */
    @Override
    public boolean requiresAuthentication() {
        return true;
    }
}