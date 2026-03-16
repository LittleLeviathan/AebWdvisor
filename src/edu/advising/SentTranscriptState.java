package edu.advising.state;

/**
 * SentTranscriptState - Concrete State (Week 9)
 *
 * Terminal state - once a request is SENT no further
 * transitions are allowed.
 */
public class SentTranscriptState implements TranscriptRequestState {

    public static final SentTranscriptState INSTANCE = new SentTranscriptState();
    private SentTranscriptState() {}

    @Override
    public void submit(TranscriptRequestContext context) {
        System.err.println("ERROR: Request is already SENT. No further transitions allowed.");
    }

    @Override
    public void process(TranscriptRequestContext context) {
        System.err.println("ERROR: Request is already SENT. No further transitions allowed.");
    }

    @Override
    public void prepare(TranscriptRequestContext context) {
        System.err.println("ERROR: Request is already SENT. No further transitions allowed.");
    }

    @Override
    public void dispatch(TranscriptRequestContext context) {
        System.err.println("ERROR: Request is already SENT. No further transitions allowed.");
    }

    @Override
    public void cancel(TranscriptRequestContext context) {
        System.err.println("ERROR: Request is already SENT. No further transitions allowed.");
    }

    @Override
    public void fail(TranscriptRequestContext context, String reason) {
        System.err.println("ERROR: Request is already SENT. No further transitions allowed.");
    }

    @Override
    public void retry(TranscriptRequestContext context) {
        System.err.println("ERROR: Request is already SENT. No further transitions allowed.");
    }

    @Override
    public String getStateName() {
        return "SENT";
    }
}