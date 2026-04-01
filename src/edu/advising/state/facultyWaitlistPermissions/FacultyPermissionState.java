package edu.advising.state.facultyWaitlistPermissions;

public interface FacultyPermissionState {
    void approve();
    void deny(String reason);
    void expire();
    void resubmit();
    void revoke(String reason);
    boolean isValid();

    String getStateName();
}
