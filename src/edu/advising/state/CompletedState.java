package edu.advising.state;

/**
 * Represents an enrollment where a final grade has been posted
 * and the course is finished. This is a TERMINAL state —
 * no further transitions are allowed.
 * This is a stateless singleton.
 */
public class CompletedState implements EnrollmentState {

    public static final CompletedState INSTANCE = new CompletedState();

    private CompletedState() {}

    public static CompletedState getInstance() {
        return INSTANCE;
    }

    // No overrides needed — all transition methods inherited from
    // EnrollmentState will throw IllegalStateException automatically.
    // All guard methods return false by default.
}