package edu.advising.state.facultyWaitlistPermissions;

public class FacultyPermissionDeniedState implements FacultyPermissionState {
    public static FacultyPermissionState INSTANCE = new FacultyPermissionDeniedState();
    private FacultyPermissionDeniedState(){}

    @Override
    public void approve() {

    }

    @Override
    public void deny() {

    }

    @Override
    public void expire() {

    }

    @Override
    public void resubmit() {

    }

    @Override
    public void revoke() {

    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String getStateName() {
        return "DENIED";
    }
}
