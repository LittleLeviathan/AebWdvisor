package edu.advising.state;

import edu.advising.core.DatabaseManager;
import edu.advising.notifications.NotificationManager;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * TranscriptRequestContext - Context Class (Week 9)
 *
 * Holds the current state of a transcript request and
 * delegates all transitions to the current state object.
 * Also handles saving changes to the database.
 */
public class TranscriptRequestContext {

    private TranscriptRequest request;
    private TranscriptRequestState currentState;
    private DatabaseManager dbManager;
    private NotificationManager notificationManager;

    // -------------------------------------------------------------------------
    // Factory Methods
    // -------------------------------------------------------------------------

    /**
     * create() - builds a brand new transcript request.
     * Assigns a tracking number, saves to DB, starts in PENDING state.
     */
    public static TranscriptRequestContext create(int studentId, String requestType,
                                                  String recipientName, String recipientAddress,
                                                  boolean isRush) {
        TranscriptRequestContext ctx = new TranscriptRequestContext();
        ctx.request = new TranscriptRequest(studentId, requestType,
                recipientName, recipientAddress, isRush);
        ctx.request.setTrackingNumber(generateTrackingNumber());
        ctx.currentState = PendingTranscriptState.INSTANCE;
        ctx.saveToDatabase();
        return ctx;
    }

    /**
     * load() - reconstructs a context from an existing DB row by ID.
     */
    public static TranscriptRequestContext load(int requestId) {
        TranscriptRequestContext ctx = new TranscriptRequestContext();
        ctx.dbManager = DatabaseManager.getInstance();
        try {
            String sql = "SELECT * FROM transcript_requests WHERE id = ?";
            ctx.request = ctx.dbManager.executeQuery(sql, rs -> {
                if (rs.next()) {
                    TranscriptRequest r = new TranscriptRequest();
                    r.setId(rs.getInt("id"));
                    r.setStatus(rs.getString("status"));
                    r.setTrackingNumber(rs.getString("tracking_number"));
                    r.setFailureReason(rs.getString("failure_reason"));
                    r.setProcessedBy(rs.getInt("processed_by"));
                    return r;
                }
                return null;
            }, requestId);
        } catch (SQLException e) {
            System.err.println("Error loading transcript request: " + e.getMessage());
        }
        ctx.currentState = StateFactory.transcriptStateFor(ctx.request.getStatus());
        return ctx;
    }

    // -------------------------------------------------------------------------
    // Private Constructor
    // -------------------------------------------------------------------------

    private TranscriptRequestContext() {
        this.dbManager = DatabaseManager.getInstance();
        this.notificationManager = NotificationManager.getInstance();
    }

    // -------------------------------------------------------------------------
    // Transition Methods (delegates to current state)
    // -------------------------------------------------------------------------

    public void submit()              { currentState.submit(this); }
    public void process()             { currentState.process(this); }
    public void prepare()             { currentState.prepare(this); }
    public void dispatch()            { currentState.dispatch(this); }
    public void cancel()              { currentState.cancel(this); }
    public void fail(String reason)   { currentState.fail(this, reason); }
    public void retry()               { currentState.retry(this); }

    // -------------------------------------------------------------------------
    // State Management (called by state classes)
    // -------------------------------------------------------------------------

    public void transitionTo(TranscriptRequestState newState) {
        this.currentState = newState;
        this.request.setStatus(newState.getStateName());
        persistToDatabase();
        notificationManager.notifyTranscriptStatusChange(this.request);
    }

    public String getCurrentStateName() {
        return currentState.getStateName();
    }

    // -------------------------------------------------------------------------
    // Database Methods
    // -------------------------------------------------------------------------

    private void saveToDatabase() {
        try {
            String sql = "INSERT INTO transcript_requests " +
                    "(student_id, request_type, recipient_name, recipient_address, " +
                    "status, tracking_number, is_rush, request_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            int id = dbManager.executeInsert(sql,
                    request.getStudentId(),
                    request.getRequestType(),
                    request.getRecipientName(),
                    request.getRecipientAddress(),
                    request.getStatus(),
                    request.getTrackingNumber(),
                    request.isRush(),
                    request.getRequestDate());
            request.setId(id);
        } catch (SQLException e) {
            System.err.println("Error saving transcript request: " + e.getMessage());
        }
    }

    private void persistToDatabase() {
        try {
            String sql = "UPDATE transcript_requests SET status = ?, " +
                    "failure_reason = ?, processed_by = ?, completed_date = ? " +
                    "WHERE id = ?";
            dbManager.executeUpdate(sql,
                    request.getStatus(),
                    request.getFailureReason(),
                    request.getProcessedBy(),
                    request.getCompletedDate(),
                    request.getId());
        } catch (SQLException e) {
            System.err.println("Error updating transcript request: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Getters (used by state classes)
    // -------------------------------------------------------------------------

    public TranscriptRequest getRequest() { return request; }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static String generateTrackingNumber() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder("TR-");
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt((int)(Math.random() * chars.length())));
        }
        return sb.toString();
    }
}