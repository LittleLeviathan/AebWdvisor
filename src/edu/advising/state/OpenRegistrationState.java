package edu.advising.state;

/**
 * OpenRegistrationState - Concrete State (Week 9)
 *
 * Registration is open. Students can register and drop sections.
 * Transitions to LATE when transitionToLate() is called or
 * checkAndAdvance() detects closeDate has passed.
 * Transitions to CLOSED when close() is called.
 */
public class OpenRegistrationState implements RegistrationPeriodState {

    public static final OpenRegistrationState INSTANCE = new OpenRegistrationState();

    private OpenRegistrationState() {}

    @Override
    public void open(RegistrationPeriodContext context) {
        System.out.println("[Registration] Period is already OPEN.");
    }

    @Override
    public void transitionToLate(RegistrationPeriodContext context) {
        System.out.println("[Registration] Period is now in LATE registration.");
        context.setState(LateRegistrationState.INSTANCE);
        context.persist();
    }

    @Override
    public void close(RegistrationPeriodContext context) {
        System.out.println("[Registration] Registration period is now CLOSED.");
        context.setState(ClosedRegistrationState.INSTANCE);
        context.persist();
    }

    @Override
    public boolean canRegister(RegistrationPeriodContext context) {
        return true;
    }

    @Override
    public boolean canDrop(RegistrationPeriodContext context) {
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
        } else if (now.isAfter(context.getPeriod().getCloseDate())) {
            transitionToLate(context);
        }
    }

    @Override
    public String getStateName() {
        return "OPEN";
    }
}