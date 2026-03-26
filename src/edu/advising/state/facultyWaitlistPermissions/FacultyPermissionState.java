package edu.advising.state.facultyWaitlistPermissions;

public interface FacultyPermissionState {
    public void approve();
    public void deny();
    public void expire();
    public void resubmit();
    public void revoke();
    public boolean isValid();

    public String getStateName();
}
