package edu.advising.state;

import edu.advising.core.Column;
import edu.advising.core.Id;
import edu.advising.core.Table;

import java.time.LocalDateTime;

/**
 * TranscriptRequest - ORM Entity (Week 9)
 * Maps to the 'transcript_requests' table in the database.
 * Each field below is a column in that table.
 */
@Table(name = "transcript_requests")
public class TranscriptRequest {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "student_id")
    private int studentId;

    @Column(name = "request_type")
    private String requestType;

    @Column(name = "recipient_name")
    private String recipientName;

    @Column(name = "recipient_address")
    private String recipientAddress;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Column(name = "status")
    private String status;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "processed_by")
    private int processedBy;

    @Column(name = "is_rush")
    private boolean isRush;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    // No-arg constructor required by ORM
    public TranscriptRequest() {}

    public TranscriptRequest(int studentId, String requestType,
                             String recipientName, String recipientAddress,
                             boolean isRush) {
        this.studentId        = studentId;
        this.requestType      = requestType;
        this.recipientName    = recipientName;
        this.recipientAddress = recipientAddress;
        this.isRush           = isRush;
        this.requestDate      = LocalDateTime.now();
        this.status           = "PENDING";
    }

    // Getters
    public int getId()                      { return id; }
    public int getStudentId()               { return studentId; }
    public String getRequestType()          { return requestType; }
    public String getRecipientName()        { return recipientName; }
    public String getRecipientAddress()     { return recipientAddress; }
    public LocalDateTime getRequestDate()   { return requestDate; }
    public String getStatus()               { return status; }
    public String getTrackingNumber()       { return trackingNumber; }
    public String getFailureReason()        { return failureReason; }
    public int getProcessedBy()             { return processedBy; }
    public boolean isRush()                 { return isRush; }
    public LocalDateTime getCompletedDate() { return completedDate; }

    // Setters
    public void setId(int id)                                 { this.id = id; }
    public void setStatus(String status)                      { this.status = status; }
    public void setTrackingNumber(String trackingNumber)      { this.trackingNumber = trackingNumber; }
    public void setFailureReason(String failureReason)        { this.failureReason = failureReason; }
    public void setProcessedBy(int processedBy)               { this.processedBy = processedBy; }
    public void setCompletedDate(LocalDateTime completedDate) { this.completedDate = completedDate; }
}