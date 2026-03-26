package edu.advising.state.facultyWaitlistPermissions;

public class FacultyPermissionExpiredState implements FacultyPermissionState {
    public static FacultyPermissionState INSTANCE = new FacultyPermissionExpiredState();
    private FacultyPermissionExpiredState(){}

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
        return "EXPIRED";
    }
}
