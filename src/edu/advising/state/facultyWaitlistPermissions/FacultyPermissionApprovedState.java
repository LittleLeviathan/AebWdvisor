package edu.advising.state.facultyWaitlistPermissions;

// Singleton state for faculty waitlist permissions

import edu.advising.notifications.NotificationManager;

public class FacultyPermissionApprovedState implements FacultyPermissionState {
    public static FacultyPermissionApprovedState INSTANCE = new FacultyPermissionApprovedState();
    private FacultyPermissionApprovedState(){}

    @Override
    public void approve(FacultyPermissionContext ctx) {
        System.err.println("ERROR: Permission is already APPROVED.");
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
        ctx.getPermission().setStatus("EXPIRED");
        ctx.setState(FacultyPermissionExpiredState.INSTANCE);
        ctx.persist();
    }

    @Override
    public void resubmit(FacultyPermissionContext ctx) {
        System.err.println("ERROR: Cannot resubmit an APPROVED permission.");
    }

    @Override
    public void revoke(FacultyPermissionContext ctx, String reason) {
        ctx.getPermission().setDenialReason(reason);
        ctx.getPermission().setStatus("DENIED");
        ctx.setState(FacultyPermissionDeniedState.INSTANCE);
        ctx.persist();
        NotificationManager.getInstance().notifyPermissionDecision(ctx.getPermission());
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
