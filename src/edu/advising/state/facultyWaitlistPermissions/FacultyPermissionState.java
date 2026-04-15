package edu.advising.state.facultyWaitlistPermissions;

public interface FacultyPermissionState {
    void approve(FacultyPermissionContext ctx);
    void deny(FacultyPermissionContext ctx, String reason);
    void expire(FacultyPermissionContext ctx);
    void resubmit(FacultyPermissionContext ctx);
    void revoke(FacultyPermissionContext ctx, String reason);
    boolean isValid();

    String getStateName();
}
