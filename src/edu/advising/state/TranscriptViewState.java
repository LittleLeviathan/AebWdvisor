package edu.advising.state;

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
        // TODO: implement
    }

    /**
     * Called when the app navigates away from the transcript screen
     * - Print "Leaving Transcript"
     */
    @Override
    public void exit(ViewContext context) {
        // TODO: implement
    }

    /**
     * Displays the transcript screen options
     * - Print "=== Transcript ==="
     * - Print available actions: BACK, LOGOUT
     */
    @Override
    public void render() {
        // TODO: implement
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
    public void handleAction(ViewContext context, String action, String... args) {
        // TODO: implement
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