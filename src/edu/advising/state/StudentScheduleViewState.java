package edu.advising.state;

public class StudentScheduleViewState implements ViewState {

    public static final StudentScheduleViewState INSTANCE = new StudentScheduleViewState();

    private StudentScheduleViewState(){}

    @Override
    public void enter(ViewContext context) {
        System.out.println("[StudentScheduleViewState] Navigating to Schedule.");
    }

    @Override
    public void exit(ViewContext context) {
        System.out.println("[StudentScheduleViewState] Leaving Schedule.");
    }

    @Override
    public void render() {
        System.out.println("=== View Class Schedule ===");
        System.out.println("Available actions: SORT BY SEMESTER, SORT BY DAY, SORT BY STATUS, SORT BY DELIVERY MODE, BACK, LOGOUT");
    }

    @Override
    public void handleAction(ViewContext context, String action, String... args) {
        switch (action) {
            case "BACK":
                context.back();
                break;
            case "LOGOUT":
                context.logout();
                break;
        }

    }

    @Override
    public String getViewName() {
        return "SCHEDULE";
    }

    @Override
    public boolean requiresAuthentication() {
        return true;
    }
}
