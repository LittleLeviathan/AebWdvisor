package edu.advising.state;

/**
 * LoginViewState - Concrete State (Week 6 - User Story #34)
 * Alias for GuestViewState — the login screen and guest screen
 * are the same view in this application.
 * Implemented as a singleton since it is stateless.
 */
public class LoginViewState implements ViewState {

    // Singleton instance - only one ever exists
    public static final LoginViewState INSTANCE = new LoginViewState();

    // Private constructor prevents anyone from creating a new instance
    private LoginViewState() {}

    /**
     * Delegates to GuestViewState.INSTANCE.enter()
     * - The login screen behaves exactly like the guest screen
     */
    @Override
    public void enter(ViewContext context) {
        // TODO: delegate to GuestViewState.INSTANCE.enter(context)
    }

    /**
     * Delegates to GuestViewState.INSTANCE.exit()
     */
    @Override
    public void exit(ViewContext context) {
        // TODO: delegate to GuestViewState.INSTANCE.exit(context)
    }

    /**
     * Delegates to GuestViewState.INSTANCE.render()
     */
    @Override
    public void render() {
        // TODO: delegate to GuestViewState.INSTANCE.render()
    }

    /**
     * Delegates to GuestViewState.INSTANCE.handleAction()
     */
    @Override
    public void handleAction(ViewContext context, String action, String... args) {
        // TODO: delegate to GuestViewState.INSTANCE.handleAction(context, action, args)
    }

    /**
     * Returns the name of this view
     */
    @Override
    public String getViewName() {
        return "LOGIN";
    }

    /**
     * Login screen does not require authentication
     */
    @Override
    public boolean requiresAuthentication() {
        return false;
    }
}
