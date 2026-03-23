package edu.advising.state;

/**
 * Represents an enrollment where the student formally withdrew.
 * A W grade has been recorded. This is a TERMINAL state —
 * no further transitions are allowed.
 * This is a stateless singleton.
 */
public class WithdrawnState implements EnrollmentState {

    public static final WithdrawnState INSTANCE = new WithdrawnState();

    private WithdrawnState() {}

    public static WithdrawnState getInstance() {
        return INSTANCE;
    }

    // No overrides needed — all transition methods inherited from
    // EnrollmentState will throw IllegalStateException automatically.
    // All guard methods return false by default.
}