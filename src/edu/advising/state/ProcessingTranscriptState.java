package edu.advising.state;

/**
 * ProcessingTranscriptState - Concrete State (Week 9)
 *
 * Represents a transcript request that the registrar
 * has picked up and is actively working on.
 *
 * ALLOWED:   prepare(), cancel(), fail()
 * BLOCKED:   everything else
 */
public class ProcessingTranscriptState implements TranscriptRequestState {

    // Singleton - one shared instance, no need to create new objects
    public static final ProcessingTranscriptState INSTANCE = new ProcessingTranscriptState();
    private ProcessingTranscriptState() {}

    @Override
    public void submit(TranscriptRequestContext context) {
        System.err.println("ERROR: Cannot submit a request that is already PROCESSING.");
    }

    @Override
    public void process(TranscriptRequestContext context) {
        System.err.println("ERROR: Request is already in PROCESSING state.");
    }

    @Override
    public void prepare(TranscriptRequestContext context) {
        System.out.println("Transcript request " + context.getRequest().getTrackingNumber()
                + " is now READY.");
        context.transitionTo(ReadyTranscriptState.INSTANCE);
    }

    @Override
    public void dispatch(TranscriptRequestContext context) {
        System.err.println("ERROR: Cannot dispatch a PROCESSING request. Must prepare it first.");
    }

    @Override
    public void cancel(TranscriptRequestContext context) {
        System.out.println("Transcript request " + context.getRequest().getTrackingNumber()
                + " has been CANCELLED.");
        context.transitionTo(CancelledTranscriptState.INSTANCE);
    }

    @Override
    public void fail(TranscriptRequestContext context, String reason) {
        System.out.println("Transcript request " + context.getRequest().getTrackingNumber()
                + " has FAILED. Reason: " + reason);
        context.getRequest().setFailureReason(reason);
        context.transitionTo(FailedTranscriptState.INSTANCE);
    }

    @Override
    public void retry(TranscriptRequestContext context) {
        System.err.println("ERROR: Cannot retry a PROCESSING request.");
    }

    @Override
    public String getStateName() {
        return "PROCESSING";
    }
}