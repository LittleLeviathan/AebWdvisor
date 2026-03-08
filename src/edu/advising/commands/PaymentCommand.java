package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - PaymentCommand (Concrete Command)
// ============================================================================
//
// FEATURE:  Financial Information → Make a Payment
//
// WHY COMMAND PATTERN HERE:
//   A payment is a transactional operation that:
//     1. Must be logged for auditing (every cent that moves needs a record).
//     2. May need to be reversed (refunds — the undo operation).
//     3. Should trigger Observer notifications (PaymentReceived → email receipt).
//     4. Could be part of a MacroCommand (e.g., enroll + pay tuition at once).
//
//   Without Command Pattern, all of this logic would be tangled into a button
//   handler or a service method. Command Pattern separates:
//     WHO triggers the action (GUI button / REST endpoint)
//     WHAT the action does (this class)
//     HOW it is undone (the undo() method)
//
// UNDO SEMANTICS:
//   Undoing a payment marks the Payment row as REFUNDED via ORM upsert().
//   In a real system this would also call a payment gateway refund API.
//
// GUI INTEGRATION:
//   PaymentCommand cmd = new PaymentCommand(student, amount, paymentType, paymentMethod);
//   executor.execute(cmd);
//   if (cmd.wasSuccessful()) showReceipt(cmd.getPaymentReferenceNumber());
//
// ============================================================================

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Table;
import edu.advising.notifications.NotificationManager;
import edu.advising.notifications.ObservableStudent;
import edu.advising.users.Student;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Table(name = "command_history", isSubTable = true)
public class PaymentCommand extends BaseCommand {
    private ObservableStudent student;
    private BigDecimal amount;
    private String paymentType;    // TUITION, FEE, HOUSING, etc.
    private String paymentMethod;  // CREDIT_CARD, CHECK, CASH, etc.

    // Populated after execute() completes — needed for undo and receipt display.
    private Payment paymentRecord;

    private NotificationManager notificationManager;
    private DatabaseManager     dbManager;

    // Constructors

    public PaymentCommand() {
        this(null, null, null, null);
    }

    /**
     * @param student       The student making the payment.
     * @param amount        Payment amount as BigDecimal (must be > 0).
     * @param paymentType   Category: TUITION, FEE, HOUSING, etc.
     * @param paymentMethod Method: CREDIT_CARD, CHECK, CASH, ONLINE, etc.
     */
    public PaymentCommand(ObservableStudent student, BigDecimal amount,
                          String paymentType, String paymentMethod) {
        super();
        this.commandType         = "PAYMENT";
        this.student             = student;
        this.amount              = amount;
        this.paymentType         = paymentType;
        this.paymentMethod       = paymentMethod;
        this.notificationManager = NotificationManager.getInstance();
        this.dbManager           = DatabaseManager.getInstance();
    }

    /** Backward-compatible convenience constructor for double amounts. */
    public PaymentCommand(ObservableStudent student, double amount, String paymentType) {
        this(student, BigDecimal.valueOf(amount), paymentType, "ONLINE");
    }

    public static PaymentCommand fromSuperType(BaseCommand base) {
        PaymentCommand cmd = new PaymentCommand();
        BaseCommand.copyBaseFields(base, cmd);
        cmd.initAfterLoad();
        return cmd;
    }

    // -------------------------------------------------------------------------
    // Command Interface — execute()
    // -------------------------------------------------------------------------

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        // Pre-condition: amount must be positive
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            successful = false;
            errorMessage = "Payment amount must be greater than zero.";
            System.out.println("✗ " + errorMessage);
            return;
        }

        // Build Payment ORM entity and persist it via upsert()
        paymentRecord = new Payment(
                student.getId(),
                amount,
                paymentType,
                paymentMethod,
                "COMPLETED"
        );
        paymentRecord.setNotes("Processed via " + paymentMethod);

        try {
            // upsert() reflects over Payment's @Table/@Column annotations and
            // builds the MERGE statement — no hand-written SQL needed here.
            dbManager.upsert(paymentRecord);

            if (paymentRecord.getId() <= 0) {
                // upsert() should set the generated id via setId() — something went wrong.
                throw new IllegalStateException("Payment was saved but no ID was returned.");
            }

            // Adjust the student's account balance atomically
            updateStudentAccountBalance(amount.negate()); // payment reduces balance owed

            executed  = true;
            successful = true;

            System.out.printf("✓ Payment processed: $%.2f (%s) via %s | Ref: %s%n",
                    amount, paymentType, paymentMethod, paymentRecord.getReferenceNumber());

            // ── Trigger Observer notification ─────────────────────────────────
            // This fires the NotificationManager which pushes to all attached
            // Observer channels (email receipt, push notification, etc.)
            notificationManager.notifyPaymentReceived(student, amount.doubleValue(), paymentType);

        } catch (SQLException | IllegalAccessException | IllegalStateException e) {
            successful   = false;
            errorMessage = "Payment processing failed: " + e.getMessage();
            System.err.println("✗ " + errorMessage);
        }
    }

    // -------------------------------------------------------------------------
    // Command Interface — undo()
    // -------------------------------------------------------------------------

    @Override
    public void undo() {
        if (!executed || !successful || paymentRecord == null) {
            System.out.println("Cannot undo — payment was not completed.");
            return;
        }

        try {
            // Mark the Payment entity REFUNDED and re-persist via ORM upsert()
            paymentRecord.setStatus("REFUNDED");
            dbManager.upsert(paymentRecord);

            // Reverse the balance adjustment
            updateStudentAccountBalance(amount); // adds the amount back to balance owed

            undoneAt = LocalDateTime.now();
            isUndone = true;

            System.out.printf("↶ Undone: Refund issued $%.2f (%s) | Ref: %s%n",
                    amount, paymentType, paymentRecord.getReferenceNumber());

            // Notify student of the refund.
            notificationManager.notifyPaymentReceived(
                    student, -amount.doubleValue(), "REFUND-" + paymentType);

        } catch (SQLException | IllegalAccessException e) {
            System.err.println("✗ Failed to process refund: " + e.getMessage());
        }
    }

    @Override
    public boolean isUndoable() {
        // Can only refund if the original payment was in this session and succeeded.
        // In production, you'd also enforce a refund window (e.g., same calendar day).
        return executed && successful && paymentRecord != null && paymentRecord.isCompleted();
    }

    @Override
    public String getDescription() {
        return String.format("Payment of $%.2f (%s via %s)", amount, paymentType, paymentMethod);
    }

    // -------------------------------------------------------------------------
    // Convenience Getter — used by the UI to show a receipt after execute()
    // -------------------------------------------------------------------------

    /**
     * Returns the reference number for receipt display after execute().
     *
     * GUI Usage:
     *   executor.execute(cmd);
     *   if (cmd.wasSuccessful()) receiptLabel.setText("Ref: " + cmd.getPaymentReferenceNumber());
     */
    public String getPaymentReferenceNumber() {
        return paymentRecord != null ? paymentRecord.getReferenceNumber() : null;
    }

    // -------------------------------------------------------------------------
    // Serialization — for CommandHistory persistence
    // -------------------------------------------------------------------------

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("studentPk",  student.getId());   // int PK
        data.put("studentId",     student.getStudentId());                               // int PK
        data.put("amount",        amount.toPlainString());                        // BigDecimal-safe
        data.put("paymentType",   paymentType);
        data.put("paymentMethod", paymentMethod);
         // Store the generated payment record id so we can retrieve it on undo/redo
        data.put("paymentId",     paymentRecord != null ? paymentRecord.getId() : 0);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("PaymentCommand: serialization failed", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        if (json == null || json.isBlank()) return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);

            // Reconstruct the student by numeric pk (not the String student_id field)
            int studentPk = (int) data.get("studentPk");
            Student raw   = dbManager.fetchOne(Student.class, "id", studentPk);
            if (raw != null) {
                this.student = ObservableStudent.fromSuperType(raw);
            }

            this.amount        = new BigDecimal(data.get("amount").toString());
            this.paymentType   = (String) data.get("paymentType");
            this.paymentMethod = (String) data.get("paymentMethod");

            // Re-hydrate the Payment record so undo() can find the DB row.
            int paymentId = (int) data.get("paymentId");
            if (paymentId > 0) {
                this.paymentRecord = dbManager.fetchOne(Payment.class, "id", paymentId);
            }

        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("PaymentCommand: deserialization failed", e);
        }
    }

    // -------------------------------------------------------------------------
    // Private Helpers
    // -------------------------------------------------------------------------

    /**
     * Atomically adjusts the student's account balance.
     *
     * This intentionally uses a raw SQL UPDATE (via executeUpdate) rather than
     * an ORM upsert() because we need an atomic increment/decrement against
     * the existing row value. upsert() would overwrite the entire row with a
     * potentially stale in-memory value if two sessions ran concurrently.
     * This is the one place in PaymentCommand where direct SQL is the correct
     * and safer choice over the ORM without further ORM development.
     */
    private void updateStudentAccountBalance(BigDecimal delta) {
        String updateSql = "UPDATE student_accounts " +
                "SET current_balance = current_balance + ?, " +
                "    total_payments  = total_payments  + ?, " +
                "    last_updated    = CURRENT_TIMESTAMP " +
                "WHERE student_id = ?";
        try {
            int rows = dbManager.executeUpdate(updateSql, delta, delta.negate(), student.getId());
            if (rows == 0) {
                // Account row doesn't exist yet — create it.
                dbManager.executeInsert(
                        "INSERT INTO student_accounts " +
                                "(student_id, current_balance, total_charges, total_payments) " +
                                "VALUES (?, ?, 0.00, ?)",
                        student.getId(), delta, delta.negate()
                );
            }
        } catch (SQLException e) {
            System.err.println("PaymentCommand: could not update student account — " + e.getMessage());
        }
    }
}