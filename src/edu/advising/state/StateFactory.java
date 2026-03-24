package edu.advising.state;

import edu.advising.state.TranscriptRequestState;
import edu.advising.state.PendingTranscriptState;
import edu.advising.state.ProcessingTranscriptState;
import edu.advising.state.ReadyTranscriptState;
import edu.advising.state.SentTranscriptState;
import edu.advising.state.CancelledTranscriptState;
import edu.advising.state.FailedTranscriptState;

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

    /**
     * Maps a registration period status string from the DB
     * to the correct State singleton.
     */
    public static RegistrationPeriodState registrationStateFor(String status) {
        if (status == null) {
            return NotOpenRegistrationState.INSTANCE;
        }
        switch (status) {
            case "NOT_OPEN": return NotOpenRegistrationState.INSTANCE;
            case "OPEN":     return OpenRegistrationState.INSTANCE;
            case "LATE":     return LateRegistrationState.INSTANCE;
            case "CLOSED":   return ClosedRegistrationState.INSTANCE;
            default:
                throw new IllegalArgumentException(
                        "Unknown registration status: " + status);
        }
    }

    public static WaitlistState waitlistStateFor(String status){
        if (status == null){
            return ActiveWaitlistState.getInstance();
        }
        switch (status){
            case "ACTIVE": return ActiveWaitlistState.getInstance();
            case "OFFERED": return OfferedWaitlistState.getInstance();
            case "ENROLLED": return EnrolledFromWaitlistState.getInstance();
            case "REMOVED": return RemovedWaitlistState.getInstance();
            case "EXPIRED": return ExpiredWaitlistSate.getInstance();
            default: throw new IllegalArgumentException("Unknown waitlist status: " + status);
        }
    }
}
