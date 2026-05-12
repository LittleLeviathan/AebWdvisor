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

    }

    @Override
    public void handleAction(ViewContext context, String action, String... args) {

    }

    @Override
    public String getViewName() {
        return "TRANSCRIPT";
    }

    @Override
    public boolean requiresAuthentication() {
        return true;
    }
}
