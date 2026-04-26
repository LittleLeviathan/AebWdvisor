package edu.advising.state;

/**
 * ViewState - State Pattern Interface (Week 6 - User Story #34)
 * Represents a single screen/view in the portal navigation system.
 * Each concrete view implements these methods differently.
 */
public interface ViewState {

    // Called when the application navigates TO this view
    // - Print or display the view name
    // - Set up anything this view needs when it becomes active
    void enter(ViewContext context);

    // Called when the application navigates AWAY from this view
    // - Clean up anything this view set up in enter()
    void exit(ViewContext context);

    // Displays the current view's content to the user
    // - Print the menu options or information for this screen
    void render();

    // Handles user actions on this view
    // - Check what action was requested (LOGIN, NAVIGATE, LOGOUT, etc.)
    // - Delegate to the appropriate method or context
    void handleAction(ViewContext context, String action, String... args);

    // Returns the name of this view (ex. "GUEST", "STUDENT_DASHBOARD")
    String getViewName();

    // Returns true if the user must be logged in to access this view
    // - GuestViewState returns false
    // - All other views return true
    boolean requiresAuthentication();
}