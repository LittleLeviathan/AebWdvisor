package edu.advising.state;

/**
 * ClosedRegistrationState - Concrete State (Week 9)
 * Registration is closed for the semester. This is a terminal state.
 * Students cannot register or drop. No further transitions are possible.
 */
public class ClosedRegistrationState implements RegistrationPeriodState {

    public static final ClosedRegistrationState INSTANCE = new ClosedRegistrationState();

    private ClosedRegistrationState() {}

    @Override
    public void open(RegistrationPeriodContext context) {
        System.out.println("[Registration] Cannot reopen — registration is CLOSED for the semester.");
    }

    @Override
    public void transitionToLate(RegistrationPeriodContext context) {
        System.out.println("[Registration] Cannot transition to LATE — registration is CLOSED for the semester.");
    }

    @Override
    public void close(RegistrationPeriodContext context) {
        System.out.println("[Registration] Registration is already CLOSED for the semester.");
    }

    @Override
    public boolean canRegister(RegistrationPeriodContext context) {
        System.out.println("[Registration] Registration is CLOSED. Please contact the registrar.");
        return false;
    }

    @Override
    public boolean canDrop(RegistrationPeriodContext context) {
        System.out.println("[Registration] Drop period is CLOSED. Please contact the registrar.");
        return false;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void checkAndAdvance(RegistrationPeriodContext context) {
        System.out.println("[Registration] Period is CLOSED — no further transitions possible.");
    }

    @Override
    public String getStateName() {
        return "CLOSED";
    }
}