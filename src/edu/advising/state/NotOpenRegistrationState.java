package edu.advising.state;

/**
 * NotOpenRegistrationState - Concrete State (Week 9)
 *
 * Registration has not yet opened. Students cannot register or drop.
 * Transitions to OPEN when open() is called or checkAndAdvance()
 * detects the openDate has passed.
 */
public class NotOpenRegistrationState implements RegistrationPeriodState {

    public static final NotOpenRegistrationState INSTANCE = new NotOpenRegistrationState();

    private NotOpenRegistrationState() {}

    @Override
    public void open(RegistrationPeriodContext context) {
        System.out.println("[Registration] Period is now OPEN.");
        context.setState(OpenRegistrationState.INSTANCE);
        context.persist();
    }

    @Override
    public void transitionToLate(RegistrationPeriodContext context) {
        System.out.println("[Registration] Cannot transition to LATE — period is not open yet.");
    }

    @Override
    public void close(RegistrationPeriodContext context) {
        System.out.println("[Registration] Cannot close — period has not opened yet.");
    }

    @Override
    public boolean canRegister(RegistrationPeriodContext context) {
        System.out.println("[Registration] Registration is not open yet. Please check back later.");
        return false;
    }

    @Override
    public boolean canDrop(RegistrationPeriodContext context) {
        System.out.println("[Registration] Drop period is not open yet. Please check back later.");
        return false;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void checkAndAdvance(RegistrationPeriodContext context) {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        if (now.isAfter(context.getPeriod().getOpenDate())) {
            open(context);
        }
    }

    @Override
    public String getStateName() {
        return "NOT_OPEN";
    }
}