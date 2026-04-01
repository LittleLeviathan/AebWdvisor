package edu.advising.state;

import edu.advising.notifications.NotificationManager;
import java.sql.SQLException;

/**
 * Represents an enrollment that was dropped by the student.
 * The only legal transition is reenroll() → ENROLLED,
 * but only if the section still has capacity.
 * This is a stateless singleton.
 */
public class DroppedState implements EnrollmentState {

    public static final DroppedState INSTANCE = new DroppedState();

    private DroppedState() {}

    public static DroppedState getInstance() {
        return INSTANCE;
    }

    /**
     * Transitions DROPPED → ENROLLED if section has capacity.
     * Throws IllegalStateException if section is full.
     */
    @Override
    public void reenroll(EnrollmentContext ctx) {
        try {
            if (!ctx.getSection().hasCapacity()) {
                throw new IllegalStateException(
                        "Cannot re-enroll: section is at capacity.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not check section capacity.", e);
        }

        ctx.getEnrollment().setDropReason(null);
        ctx.getEnrollment().setDroppedAt(null);
        ctx.getEnrollment().setStatus("ENROLLED");
        ctx.setState(EnrolledState.getInstance());
        ctx.persist();
        NotificationManager.getInstance().notifyEnrollmentUpdate(ctx.getEnrollment());
    }

    /** @return true — reenroll is allowed from DROPPED */
    @Override
    public boolean canReenroll() { return true; }
}