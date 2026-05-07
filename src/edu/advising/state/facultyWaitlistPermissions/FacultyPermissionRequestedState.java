package edu.advising.state.facultyWaitlistPermissions;

import edu.advising.notifications.NotificationManager;

public class FacultyPermissionRequestedState implements FacultyPermissionState {
    public static FacultyPermissionRequestedState INSTANCE = new FacultyPermissionRequestedState();
    private FacultyPermissionRequestedState(){}


    @Override
    public void approve(FacultyPermissionContext ctx) {
        ctx.getPermission().setStatus("APPROVED");
        ctx.setState(FacultyPermissionApprovedState.INSTANCE);
        ctx.persist();
        NotificationManager.getInstance().notifyPermissionDecision(ctx.getPermission());
    }

    @Override
    public void deny(FacultyPermissionContext ctx, String reason) {
        ctx.getPermission().setDenialReason(reason);
        ctx.getPermission().setStatus("DENIED");
        ctx.setState(FacultyPermissionDeniedState.INSTANCE);
        ctx.persist();
        NotificationManager.getInstance().notifyPermissionDecision(ctx.getPermission());
    }

    @Override
    public void expire(FacultyPermissionContext ctx) {
        System.err.println("ERROR: Cannot expire a REQUESTED permission.");
    }

    @Override
    public void resubmit(FacultyPermissionContext ctx) {
        System.err.println("ERROR: Cannot resubmit a REQUESTED permission — it has not been denied or expired yet.");
    }

    @Override
    public void revoke(FacultyPermissionContext ctx , String reason) {
        System.err.println("ERROR: Cannot revoke a REQUESTED permission — it has not been approved yet.");
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String getStateName() {
        return "REQUESTED";
    }
}
