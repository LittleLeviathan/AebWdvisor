package edu.advising.state;

/**
 * TranscriptRequestState - State Pattern Interface (Week 9)
 *
 * Defines every possible action a transcript request can take.
 * Each concrete state class implements this and decides what's
 * legal vs. illegal from that particular state.
 */

public interface TranscriptRequestState {

    void submit(TranscriptRequestContext context);

    void process(TranscriptRequestContext context);

    void prepare(TranscriptRequestContext context);

    void dispatch(TranscriptRequestContext context);

    void cancel(TranscriptRequestContext context);

    void fail(TranscriptRequestContext context, String reason);

    void retry(TranscriptRequestContext context);

    /** Every state must be able to report its own name. */
    String getStateName();
}





