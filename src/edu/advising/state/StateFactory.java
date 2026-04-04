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
     * Maps an enrollment status string from the DB to the correct
     * EnrollmentState singleton.
     */
    public static EnrollmentState enrollmentStateFor(String status) {
        if (status == null) {
            return PendingEnrollmentState.getInstance();
        }
        switch (status) {
            case "PENDING":   return PendingEnrollmentState.getInstance();
            case "ENROLLED":  return EnrolledState.getInstance();
            case "DROPPED":   return DroppedState.getInstance();
            case "WITHDRAWN": return WithdrawnState.getInstance();
            case "COMPLETED": return CompletedState.getInstance();
            default:
                throw new IllegalArgumentException(
                        "Unknown enrollment status: " + status);
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
    /**
     * Maps a view name string to the correct ViewState singleton.
     */
    public static ViewState viewStateFor(String viewName) {
        if (viewName == null) {
            return GuestViewState.INSTANCE;
        }
        switch (viewName) {
            case "GUEST":                 return GuestViewState.INSTANCE;
            case "LOGIN":                 return LoginViewState.INSTANCE;
            case "STUDENT_DASHBOARD":     return StudentDashboardViewState.INSTANCE;
            case "FACULTY_DASHBOARD":     return FacultyDashboardViewState.INSTANCE;
            case "REGISTRATION":          return RegistrationViewState.INSTANCE;
            case "TRANSCRIPT":            return TranscriptViewState.INSTANCE;
            case "PERMISSION_MANAGEMENT": return PermissionManagementViewState.INSTANCE;
            default:
                throw new IllegalArgumentException(
                        "Unknown view name: " + viewName);
        }
    }
}
