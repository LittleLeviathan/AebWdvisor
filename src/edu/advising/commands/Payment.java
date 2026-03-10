package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - Payment Model (ORM Entity)
// ============================================================================
//
// WHY THIS MODEL IS HERE:
//   The PaymentCommand needs to persist payment records. Rather than writing
//   raw INSERT SQL strings inside the command (which was the old commented-out
//   approach), we define a proper ORM-annotated entity and let DatabaseManager
//   handle the persistence via upsert().
//
//   This is also the pattern established by Enrollment, WaitlistEntry, and
//   Section — models annotated with @Table and @Column so the ORM can reflect
//   over them at runtime.
//
// DB TABLE: payments (defined in DatabaseManager.initializeDatabase(), Week 5-8 section)
//
// FIELDS MAP EXACTLY TO:
//   id, student_id, amount, payment_type, payment_method,
//   payment_date, status, transaction_id, reference_number, notes
//
// ============================================================================

import edu.advising.core.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "payments")
public class Payment {

    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;

    @Column(name = "student_id", foreignKey = true)
    private int studentId;          // FK → students(id)

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "payment_type")
    private String paymentType;     // TUITION, FEE, HOUSING, etc.

    @Column(name = "payment_method")
    private String paymentMethod;   // CREDIT_CARD, CHECK, CASH, etc.

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "status")
    private String status;          // PENDING, COMPLETED, FAILED, REFUNDED

    @Column(name = "transaction_id")
    private String transactionId;   // External gateway reference

    @Column(name = "reference_number")
    private String referenceNumber; // Internal reference for the student

    @Column(name = "notes")
    private String notes;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** No-arg constructor required by ORM reflective instantiation. */
    public Payment() {}

    /**
     * Minimal constructor used by PaymentCommand when processing a new payment.
     */
    public Payment(int studentId, BigDecimal amount, String paymentType,
                   String paymentMethod, String status) {
        this.studentId     = studentId;
        this.amount        = amount;
        this.paymentType   = paymentType;
        this.paymentMethod = paymentMethod;
        this.status        = status;
        this.paymentDate   = LocalDateTime.now();
        // Generate a human-readable reference number for the student's receipt.
        this.referenceNumber = generateReferenceNumber();
    }

    // -------------------------------------------------------------------------
    // Convenience Methods
    // -------------------------------------------------------------------------

    /** @return true when this payment record represents a completed transaction. */
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }

    /** @return true when this payment has been refunded (e.g. via undo). */
    public boolean isRefunded() {
        return "REFUNDED".equals(status);
    }

    /**
     * Generates a simple reference number for receipt display.
     * In a real system this would come from a payment gateway.
     * Format: PAY-<timestamp-millis>
     */
    private static String generateReferenceNumber() {
        return "PAY-" + System.currentTimeMillis();
    }

    // -------------------------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------------------------

    public int getId()                     { return id; }
    public void setId(int id)              { this.id = id; }

    public int getStudentId()              { return studentId; }
    public void setStudentId(int studentId){ this.studentId = studentId; }

    public BigDecimal getAmount()          { return amount; }
    public void setAmount(BigDecimal amount){ this.amount = amount; }

    public String getPaymentType()         { return paymentType; }
    public void setPaymentType(String t)   { this.paymentType = t; }

    public String getPaymentMethod()       { return paymentMethod; }
    public void setPaymentMethod(String m) { this.paymentMethod = m; }

    public LocalDateTime getPaymentDate()  { return paymentDate; }
    public void setPaymentDate(LocalDateTime d){ this.paymentDate = d; }

    public String getStatus()              { return status; }
    public void setStatus(String status)   { this.status = status; }

    public String getTransactionId()       { return transactionId; }
    public void setTransactionId(String t) { this.transactionId = t; }

    public String getReferenceNumber()     { return referenceNumber; }
    public void setReferenceNumber(String r){ this.referenceNumber = r; }

    public String getNotes()               { return notes; }
    public void setNotes(String notes)     { this.notes = notes; }

    @Override
    public String toString() {
        return String.format("Payment[id=%d, student=%d, amount=%s, type=%s, status=%s, ref=%s]",
                id, studentId, amount, paymentType, status, referenceNumber);
    }
}