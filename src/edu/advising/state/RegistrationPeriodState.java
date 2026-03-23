package edu.advising.state;

/**
 * RegistrationPeriodState - State Pattern Interface (Week 9)
 *
 * Defines every possible action a registration period can take.
 * Each concrete state class implements this and decides what's
 * legal vs. illegal from that particular state.
 */
public interface RegistrationPeriodState {

    /** Transition from NOT_OPEN to OPEN. */
    void open(RegistrationPeriodContext context);

    /** Transition from OPEN to LATE. */
    void transitionToLate(RegistrationPeriodContext context);

    /** Transition from OPEN or LATE to CLOSED. */
    void close(RegistrationPeriodContext context);

    /** Returns true only from OPEN and LATE states. */
    boolean canRegister(RegistrationPeriodContext context);

    /** Returns true only from OPEN and LATE states. */
    boolean canDrop(RegistrationPeriodContext context);

    /** Returns true if the period is currently OPEN. */
    boolean isOpen();

    /**
     * Compares wall-clock LocalDateTime.now() against the period's
     * dates and auto-advances state without manual admin intervention.
     */
    void checkAndAdvance(RegistrationPeriodContext context);

    /** Every state must be able to report its own name. */
    String getStateName();
}