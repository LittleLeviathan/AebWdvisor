package edu.advising.state;

/**
 * ReadyTranscriptState - Concrete State (Week 9)
 *
 * Represents a transcript request that has been prepared
 * and is waiting to be dispatched by the registrar.
 *
 * ALLOWED:   dispatch(), cancel()
 * BLOCKED:   everything else
 */
public class ReadyTranscriptState implements TranscriptRequestState {

    // Singleton - one shared instance, no need to create new objects
    public static final ReadyTranscriptState INSTANCE = new ReadyTranscriptState();
    private ReadyTranscriptState() {}

    @Override
    public void submit(TranscriptRequestContext context) {
        System.err.println("ERROR: Cannot submit a request that is already READY.");
    }

    @Override
    public void process(TranscriptRequestContext context) {
        System.err.println("ERROR: Cannot process a request that is already READY.");
    }

    @Override
    public void prepare(TranscriptRequestContext context) {
        System.err.println("ERROR: Request is already in READY state.");
    }

    @Override
    public void dispatch(TranscriptRequestContext context) {
        System.out.println("Transcript request " + context.getRequest().getTrackingNumber()
                + " has been SENT.");
        context.getRequest().setCompletedDate(java.time.LocalDateTime.now());
        context.transitionTo(SentTranscriptState.INSTANCE);
    }

    @Override
    public void cancel(TranscriptRequestContext context) {
        System.out.println("Transcript request " + context.getRequest().getTrackingNumber()
                + " has been CANCELLED.");
        context.transitionTo(CancelledTranscriptState.INSTANCE);
    }

    @Override
    public void fail(TranscriptRequestContext context, String reason) {
        System.err.println("ERROR: Cannot fail a READY request.");
    }

    @Override
    public void retry(TranscriptRequestContext context) {
        System.err.println("ERROR: Cannot retry a READY request.");
    }

    @Override
    public String getStateName() {
        return "READY";
    }
}