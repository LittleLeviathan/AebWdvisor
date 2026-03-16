package edu.advising.state;

/**
 * StateFactory - Utility Class (Week 9)
 *
 * Maps database status strings to the correct State singleton.
 * Non-instantiable - private constructor, all methods are static.
 */
public class StateFactory {

    // Private constructor - prevents anyone from creating an instance
    private StateFactory() {}

    /**
     * Maps a transcript request status string from the DB
     * to the correct State singleton.
     */
    public static TranscriptRequestState transcriptStateFor(String status) {
        if (status == null) {
            return PendingTranscriptState.INSTANCE;
        }
        switch (status) {
            case "PENDING":    return PendingTranscriptState.INSTANCE;
            case "PROCESSING": return ProcessingTranscriptState.INSTANCE;
            case "READY":      return ReadyTranscriptState.INSTANCE;
            case "SENT":       return SentTranscriptState.INSTANCE;
            case "CANCELLED":  return CancelledTranscriptState.INSTANCE;
            case "FAILED":     return FailedTranscriptState.INSTANCE;
            default:
                throw new IllegalArgumentException(
                        "Unknown transcript status: " + status);
        }
    }
}
