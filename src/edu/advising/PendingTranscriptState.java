package edu.advising.state;

/**
 * PendingTranscriptState - Concrete State (Week 9)
 *
 * Represents a transcript request that has been submitted
 * but not yet picked up by the registrar.
 *
 * ALLOWED:   process(), cancel()
 * BLOCKED:   everything else
 */
public class PendingTranscriptState implements TranscriptRequestState {

    // Singleton - one shared instance, no need to create new objects
    public static final PendingTranscriptState INSTANCE = new PendingTranscriptState();
    private PendingTranscriptState() {}

    @Override
    public void submit(TranscriptRequestContext context) {
        System.err.println("ERROR: Request is already in PENDING state.");
    }

    @Override
    public void process(TranscriptRequestContext context) {
        System.out.println("Transcript request " + context.getRequest().getTrackingNumber()
                + " is now PROCESSING.");
        context.transitionTo(ProcessingTranscriptState.INSTANCE);
    }

    @Override
    public void prepare(TranscriptRequestContext context) {
        System.err.println("ERROR: Cannot prepare a PENDING request. Must process it first.");
    }

    @Override
    public void dispatch(TranscriptRequestContext context) {
        System.err.println("ERROR: Cannot dispatch a PENDING request.");
    }

    @Override
    public void cancel(TranscriptRequestContext context) {
        System.out.println("Transcript request " + context.getRequest().getTrackingNumber()
                + " has been CANCELLED.");
        context.transitionTo(CancelledTranscriptState.INSTANCE);
    }

    @Override
    public void fail(TranscriptRequestContext context, String reason) {
        System.err.println("ERROR: Cannot fail a PENDING request.");
    }

    @Override
    public void retry(TranscriptRequestContext context) {
        System.err.println("ERROR: Cannot retry a PENDING request.");
    }

    @Override
    public String getStateName() {
        return "PENDING";
    }
}
