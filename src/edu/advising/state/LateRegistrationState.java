package edu.advising.state;

/**
 * LateRegistrationState - Concrete State (Week 9)
 *
 * Registration is in the late period. Students can still register
 * and drop but a warning about late fees is printed.
 * Transitions to CLOSED when close() is called or checkAndAdvance()
 * detects lateRegistrationEnd has passed.
 */
public class LateRegistrationState implements RegistrationPeriodState {

    public static final LateRegistrationState INSTANCE = new LateRegistrationState();

    private LateRegistrationState() {}

    @Override
    public void open(RegistrationPeriodContext context) {
        System.out.println("[Registration] Period is already past OPEN — currently in LATE registration.");
    }

    @Override
    public void transitionToLate(RegistrationPeriodContext context) {
        System.out.println("[Registration] Period is already in LATE registration.");
    }

    @Override
    public void close(RegistrationPeriodContext context) {
        System.out.println("[Registration] Late registration period is now CLOSED.");
        context.setState(ClosedRegistrationState.INSTANCE);
        context.persist();
    }

    @Override
    public boolean canRegister(RegistrationPeriodContext context) {
        System.out.println("[Registration] WARNING: Late registration fees may apply.");
        return true;
    }

    @Override
    public boolean canDrop(RegistrationPeriodContext context) {
        System.out.println("[Registration] WARNING: Late drop fees may apply.");
        return true;
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void checkAndAdvance(RegistrationPeriodContext context) {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        if (now.isAfter(context.getPeriod().getLateRegistrationEnd())) {
            close(context);
        }
    }

    @Override
    public String getStateName() {
        return "LATE";
    }
}