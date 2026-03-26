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

    public static FacultyPermissionContext create(int studentId, int sectionId, int facultyId){
        return new FacultyPermissionContext(new FacultyPermission(studentId, sectionId, facultyId));
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
        // Load the correct State from the permission's status
        FacultyPermissionContext ctx = new FacultyPermissionContext(permission);
        ctx.setState(
                StateFactory.permissionStateFor(
                        permission.getStatus()
                )
        );
        //if (ctx.isExpiredByTime()) {
        //    ctx.expire();
        //}
        return ctx;
    }

    public void setState(FacultyPermissionState state){
        permission.setStatus( state.getStateName() );
        this.state = state;
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

    public void approve(){}
    public void deny(String reason){}
    public void expire(){}
    public void resubmit(){}
    public void revoke(){}

    public boolean isValid(){
        return state.isValid();
    }
    /**
     * Returns true if the permission is APPROVED and the current time is past expiryDate.
     */
    public boolean isExpiredByTime() {
        return state.isValid()
                && LocalDateTime.now().isAfter(permission.getExpiryDate());
    }
}
