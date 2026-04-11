package edu.advising.state.facultyWaitlistPermissions;


import edu.advising.core.DatabaseManager;
import edu.advising.state.StateFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class FacultyPermissionContext {
    private FacultyPermission permission;
    private FacultyPermissionState state;

    public FacultyPermissionContext(FacultyPermission permission){
        this.permission = permission;
        this.state = StateFactory.permissionStateFor( permission.getStatus() );
    }

    public static FacultyPermissionContext create(int studentId, int sectionId, int facultyId) {
        FacultyPermissionContext ctx = new FacultyPermissionContext(
                new FacultyPermission(studentId, sectionId, facultyId));
        ctx.persist();
        return ctx;
    }
    /**
     * Loads an existing FacultyPermission from the database by id,
     * reconstructs the correct state, and auto-expires if past expiryDate.
     */
    public static FacultyPermissionContext load(int permissionId) throws SQLException {
        FacultyPermission permission = DatabaseManager.getInstance()
                .fetchOne(FacultyPermission.class, "id", permissionId);
        if (permission == null) {
            throw new SQLException("FacultyPermission not found for id: " + permissionId);
        }
        FacultyPermissionContext ctx = new FacultyPermissionContext(permission);
        ctx.checkAndAdvance();
        return ctx;
    }

    // Getters and setters //
    public void setState(FacultyPermissionState state){
        permission.setStatus( state.getStateName() );
        this.state = state;
    }
    public FacultyPermission getPermission(){
        return permission;
    }
    /**
     * Saves the current state of the wrapped FacultyPermission entity to the database.
     * Called automatically after every state transition.
     */
    public void persist() {
        try {
            DatabaseManager.getInstance().upsert(permission);
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException("FacultyPermissionContext.persist() failed: " + e.getMessage(), e);
        }
    }

    public void approve(){ state.approve(this); }
    public void deny(String reason){ state.deny(this, reason); }
    public void expire(){ state.expire(this); }
    public void resubmit(){ state.resubmit(this); }
    public void revoke(String reason){ state.revoke(this, reason); }

    public boolean isValid(){
        return state.isValid();
    }

    /**
     * Checks if the permission has passed its expiryDate and advances
     * to EXPIRED if so. Should be called before using the permission
     * to make a registration decision.
     */
    public void checkAndAdvance() {
        if (isExpiredByTime()) {
            expire();
        }
    }
    /**
     * Returns true if the permission is APPROVED and the current time is past expiryDate.
     */
    public boolean isExpiredByTime() {
        return isValid() && LocalDateTime.now().isAfter(permission.getExpiryDate());
    }
}
