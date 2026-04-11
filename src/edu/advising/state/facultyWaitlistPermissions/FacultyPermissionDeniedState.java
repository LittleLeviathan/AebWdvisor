package edu.advising.state.facultyWaitlistPermissions;

import edu.advising.notifications.NotificationManager;

import java.time.LocalDateTime;

public class FacultyPermissionDeniedState implements FacultyPermissionState {
    public static FacultyPermissionDeniedState INSTANCE = new FacultyPermissionDeniedState();
    private FacultyPermissionDeniedState(){}

    @Override
    public void approve(FacultyPermissionContext ctx) {
        System.err.println("ERROR: Cannot approve a DENIED permission — student must resubmit first.");
    }

    @Override
    public void deny(FacultyPermissionContext ctx, String reason) {
        System.err.println("ERROR: Permission is already DENIED.");
    }

    @Override
    public void expire(FacultyPermissionContext ctx) {
        System.err.println("ERROR: Cannot expire a DENIED permission.");
    }

    @Override
    public void resubmit(FacultyPermissionContext ctx) {
        ctx.getPermission().setDenialReason(null);
        ctx.getPermission().setExpiryDate(LocalDateTime.now().plusHours(48));
        ctx.getPermission().setStatus("REQUESTED");
        ctx.setState(FacultyPermissionRequestedState.INSTANCE);
        ctx.persist();
        NotificationManager.getInstance().notifyPermissionRequest(ctx.getPermission());
    }

    @Override
    public void revoke(FacultyPermissionContext ctx, String reason) {
        System.err.println("ERROR: Cannot revoke a DENIED permission.");
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
