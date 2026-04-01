package edu.advising.state.facultyWaitlistPermissions;

// Singleton state for faculty waitlist permissions

public class FacultyPermissionApprovedState implements FacultyPermissionState {
    public static FacultyPermissionState INSTANCE = new FacultyPermissionApprovedState();
    private FacultyPermissionApprovedState(){}

    @Override
    public void approve() {

    }

    @Override
    public void deny(String reason) {

    }

    @Override
    public void expire() {

    }

    @Override
    public void resubmit() {

    }

    @Override
    public void revoke(String reason) {

    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String getStateName() {
        return "APPROVED";
    }
}
