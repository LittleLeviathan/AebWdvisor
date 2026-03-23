package edu.advising.state;

import edu.advising.notifications.NotificationManager;

/**
 * Represents a newly created enrollment waiting to be confirmed.
 * The only legal transition is confirm() → ENROLLED.
 * This is a stateless singleton.
 */
public class PendingEnrollmentState implements EnrollmentState {

    public static final PendingEnrollmentState INSTANCE = new PendingEnrollmentState();

    private PendingEnrollmentState() {}

    public static PendingEnrollmentState getInstance() {
        return INSTANCE;
    }

    /**
     * Transitions PENDING → ENROLLED and fires an enrollment update notification.
     */
    @Override
    public void confirm(EnrollmentContext ctx) {
        ctx.getEnrollment().setStatus("ENROLLED");
        ctx.setState(EnrolledState.getInstance());
        ctx.persist();
        NotificationManager.getInstance().notifyEnrollmentUpdate(ctx.getEnrollment());
    }
}