package edu.advising.state;

/**
 * CancelledTranscriptState - Concrete State (Week 9)
 *
 * Terminal state - once a request is CANCELLED no further
 * transitions are allowed.
 */
public class CancelledTranscriptState implements TranscriptRequestState {

    public static final CancelledTranscriptState INSTANCE = new CancelledTranscriptState();
    private CancelledTranscriptState() {}

    @Override
    public void submit(TranscriptRequestContext context) {
        System.err.println("ERROR: Request is CANCELLED. No further transitions allowed.");
    }

    @Override
    public void process(TranscriptRequestContext context) {
        System.err.println("ERROR: Request is CANCELLED. No further transitions allowed.");
    }

    @Override
    public void prepare(TranscriptRequestContext context) {
        System.err.println("ERROR: Request is CANCELLED. No further transitions allowed.");
    }

    @Override
    public void dispatch(TranscriptRequestContext context) {
        System.err.println("ERROR: Request is CANCELLED. No further transitions allowed.");
    }

    @Override
    public void cancel(TranscriptRequestContext context) {
        System.err.println("ERROR: Request is already CANCELLED. No further transitions allowed.");
    }

    @Override
    public void fail(TranscriptRequestContext context, String reason) {
        System.err.println("ERROR: Request is CANCELLED. No further transitions allowed.");
    }

    @Override
    public void retry(TranscriptRequestContext context) {
        System.err.println("ERROR: Request is CANCELLED. No further transitions allowed.");
    }

    @Override
    public String getStateName() {
        return "CANCELLED";
    }
}