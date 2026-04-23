package edu.advising.state.facultyWaitlistPermissions;

import edu.advising.notifications.NotificationManager;

import java.time.LocalDateTime;

public class FacultyPermissionExpiredState implements FacultyPermissionState {
    public static FacultyPermissionExpiredState INSTANCE = new FacultyPermissionExpiredState();
    private FacultyPermissionExpiredState(){}

    @Override
    public void approve(FacultyPermissionContext ctx) {
        System.err.println("ERROR: Cannot approve an EXPIRED permission — student must resubmit first.");
    }

    @Override
    public void deny(FacultyPermissionContext ctx, String reason) {
        System.err.println("ERROR: Cannot deny an EXPIRED permission.");
    }

    @Override
    public void expire(FacultyPermissionContext ctx) {
        System.err.println("ERROR: Permission is already EXPIRED.");
    }

    @Override
    public void resubmit(FacultyPermissionContext ctx) {
        ctx.getPermission().setExpiryDate(LocalDateTime.now().plusHours(48));
        ctx.getPermission().setStatus("REQUESTED");
        ctx.setState(FacultyPermissionRequestedState.INSTANCE);
        ctx.persist();
        NotificationManager.getInstance().notifyPermissionRequest(ctx.getPermission());
    }

    @Override
    public void revoke(FacultyPermissionContext ctx, String reason) {
        System.err.println("ERROR: Cannot revoke an EXPIRED permission.");
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
