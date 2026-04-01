package edu.advising.state;

/**
 * FailedTranscriptState - Concrete State (Week 9)
 *
 * Represents a transcript request that failed during
 * processing. Only retry() is allowed from this state.
 *
 * ALLOWED:   retry()
 * BLOCKED:   everything else
 */
public class FailedTranscriptState implements TranscriptRequestState {

    public static final FailedTranscriptState INSTANCE = new FailedTranscriptState();

    private FailedTranscriptState() {
    }

    @Override
    public void submit(TranscriptRequestContext context) {
        System.err.println("ERROR: Cannot submit a FAILED request. Use retry() instead.");
    }

    @Override
    public void process(TranscriptRequestContext context) {
        System.err.println("ERROR: Cannot process a FAILED request. Use retry() instead.");
    }

    @Override
    public void prepare(TranscriptRequestContext context) {
        System.err.println("ERROR: Cannot prepare a FAILED request. Use retry() instead.");
    }

    @Override
    public void dispatch(TranscriptRequestContext context) {
        System.err.println("ERROR: Cannot dispatch a FAILED request. Use retry() instead.");
    }

    @Override
    public void cancel(TranscriptRequestContext context) {
        System.err.println("ERROR: Cannot cancel a FAILED request. Use retry() instead.");
    }

    @Override
    public void fail(TranscriptRequestContext context, String reason) {
        System.err.println("ERROR: Request is already in FAILED state.");
    }

    @Override
    public void retry(TranscriptRequestContext context) {
        System.out.println("Transcript request " + context.getRequest().getTrackingNumber()
                + " is being retried, now PROCESSING.");
        context.getRequest().setFailureReason(null);
        context.transitionTo(ProcessingTranscriptState.INSTANCE);
    }

    @Override
    public String getStateName() {
        return "FAILED";
    }
}