package edu.advising.state;

import edu.advising.notifications.NotificationManager;
import java.time.LocalDateTime;

/**
 * Represents an active, confirmed enrollment.
 * Legal transitions: drop() → DROPPED, withdraw() → WITHDRAWN, complete() → COMPLETED.
 * This is a stateless singleton.
 */
public class EnrolledState implements EnrollmentState {

    public static final EnrolledState INSTANCE = new EnrolledState();

    private EnrolledState() {}

    public static EnrolledState getInstance() {
        return INSTANCE;
    }

    /**
     * Records the drop reason, transitions to DROPPED,
     * and fires an enrollment update notification.
     */
    @Override
    public void drop(EnrollmentContext ctx, String reason) {
        ctx.getEnrollment().setDropReason(reason);
        ctx.getEnrollment().setDroppedAt(LocalDateTime.now());
        ctx.getEnrollment().setStatus("DROPPED");
        ctx.setState(DroppedState.getInstance());
        ctx.persist();
        NotificationManager.getInstance().notifyEnrollmentUpdate(ctx.getEnrollment());
    }

    /**
     * Records a W grade, transitions to WITHDRAWN,
     * and fires an enrollment update notification.
     */
    @Override
    public void withdraw(EnrollmentContext ctx) {
        ctx.getEnrollment().setFinalGrade("W");
        ctx.getEnrollment().setGradedAt(LocalDateTime.now());
        ctx.getEnrollment().setStatus("WITHDRAWN");
        ctx.setState(WithdrawnState.getInstance());
        ctx.persist();
        NotificationManager.getInstance().notifyEnrollmentUpdate(ctx.getEnrollment());
    }

    /**
     * Records the final grade, transitions to COMPLETED,
     * and fires both an enrollment update and a grade posted notification.
     */
    @Override
    public void complete(EnrollmentContext ctx, String finalGrade) {
        ctx.getEnrollment().setFinalGrade(finalGrade);
        ctx.getEnrollment().setGradedAt(LocalDateTime.now());
        ctx.getEnrollment().setStatus("COMPLETED");
        ctx.setState(CompletedState.getInstance());
        ctx.persist();
        NotificationManager.getInstance().notifyEnrollmentUpdate(ctx.getEnrollment());
        NotificationManager.getInstance().notifyGradePosted(ctx.getEnrollment());
    }

    /** @return true — drop is allowed from ENROLLED */
    @Override
    public boolean canDrop()     { return true; }

    /** @return true — withdraw is allowed from ENROLLED */
    @Override
    public boolean canWithdraw() { return true; }

    /** @return true — complete is allowed from ENROLLED */
    @Override
    public boolean canComplete() { return true; }
}