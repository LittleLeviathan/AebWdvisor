package edu.advising.commands;

// ============================================================================
// WEEK 5: COMMAND PATTERN - UpdateContactCommand (Concrete Command)
// ============================================================================
//
// FEATURE:  Academic Profile → Contact Information Update
//
// WHY COMMAND PATTERN HERE:
//   At first glance, updating an email address looks too simple for a Command.
//   But consider:
//     1. UNDO: If a user accidentally changes their email to a typo, they need
//        a way to reverse it without contacting an administrator.
//     2. AUDIT: FERPA and institutional policy often require logging who changed
//        contact info and when — the command_history table provides this for free.
//     3. MACRO: A future "Import Contact Info from SSO" feature could batch
//        multiple UpdateContactCommands inside a MacroCommand.
//     4. VALIDATION: The command encapsulates all validation (email format,
//        duplicate check) in one place, reusable from CLI, web, or desktop GUI.
//
// UNDO SEMANTICS:
//   The old values are captured at construction time (before execute()).
//   Undo restores the previous values using the same ORM upsert path.
//   This guarantees the user record stays consistent regardless of how
//   many times they undo/redo the change.
//
// GUI INTEGRATION:
//   // "Save" button on the Contact Information Update form:
//   UpdateContactCommand cmd = new UpdateContactCommand(
//       student, emailField.getText(), phoneField.getText()
//   );
//   executor.execute(cmd);
//
//   if (cmd.wasSuccessful()) {
//       showSuccessToast("Contact information updated.");
//   } else {
//       showError(cmd.getErrorMessage());
//   }
//   undoButton.setEnabled(executor.canUndo()); // "Undo Contact Update" tooltip
//
// ============================================================================

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Table;
import edu.advising.notifications.ObservableStudent;
import edu.advising.users.Student;
import edu.advising.users.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Table(name = "command_history", isSubTable = true)
public class UpdateContactCommand extends BaseCommand {

    // ── State needed for execute and undo ────────────────────────────────────

    private ObservableStudent student;
    private String newEmail;
    private String newPhone;
    private String oldEmail;    // Captured at construction for undo
    private String oldPhone;    // Captured at construction for undo

    private final DatabaseManager dbManager;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    // Adding No argument constructor needed for fromSuperType() and ORM autoMapper()
    public UpdateContactCommand() {
        this(new ObservableStudent("", "", "", "", "", "S0000"),
                "", "");
    }

    /**
     * Capture old values at construction time, before anything is changed.
     * This is the "snapshot before" approach standard in Command Pattern undo.
     *
     * @param student  The currently logged-in, live user object.
     * @param newEmail New email address (null to leave unchanged).
     * @param newPhone New phone number (null to leave unchanged).
     */
    public UpdateContactCommand(ObservableStudent student, String newEmail, String newPhone) {
        super();
        this.commandType = "UPDATE_CONTACT";
        this.student     = student;
        this.newEmail    = newEmail;
        this.newPhone    = newPhone;

        // Snapshot old values NOW, before any changes are made.
        // This is what makes undo() reliable.
        this.oldEmail = student.getEmail();
        this.oldPhone = student.getPhone(); // Requires phone field on User — see User.java note

        this.dbManager = DatabaseManager.getInstance();
    }

    public static UpdateContactCommand fromSuperType(BaseCommand base) {
        UpdateContactCommand cmd = new UpdateContactCommand();
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

        // ── Validate inputs ──────────────────────────────────────────────────
        if (newEmail != null && !isValidEmail(newEmail)) {
            successful   = false;
            errorMessage = "Invalid email format: " + newEmail;
            System.out.println("✗ " + errorMessage);
            return;
        }

        if (newEmail != null && isDuplicateEmail(newEmail, student.getId())) {
            successful   = false;
            errorMessage = "Email address is already in use: " + newEmail;
            System.out.println("✗ " + errorMessage);
            return;
        }

        // ── Apply changes to the in-memory user object ───────────────────────
        if (newEmail != null) student.setEmail(newEmail);
        if (newPhone != null) student.setPhone(newPhone);
        student.setUpdatedAt(LocalDateTime.now());

        // ── Persist via ORM — upsert uses @Table/@Column annotations on User ─
        // upsert() generates:
        //   MERGE INTO users (id, email, phone, updated_at, ...) VALUES (...)
        // Only the columns that changed will differ; the rest stay as-is.
        try {
            Student copy = student.toSubType();  // Copying object so upsert hierarchy annotations work properly.
            dbManager.upsert(copy); // Updating the copied object, realizing fields like updatedAt won't be synced.
            //TODO: determine if other fields need to be synced as well after upsert.
            student.setUpdatedAt(copy.getUpdatedAt()); // Syncing update at manually.

            executed   = true;
            successful = true;
            System.out.printf("✓ Contact info updated for %s (ID %d)%n",
                    student.getFullName(), student.getId());

        } catch (SQLException | IllegalAccessException e) {
            // Rollback in-memory changes if the DB update fails.
            student.setEmail(oldEmail);
            student.setPhone(oldPhone);

            successful   = false;
            errorMessage = "Database update failed: " + e.getMessage();
            System.err.println("✗ " + errorMessage);
        }
    }

    // -------------------------------------------------------------------------
    // Command Interface — undo()
    // -------------------------------------------------------------------------

    @Override
    public void undo() {
        if (!executed || !successful) {
            System.out.println("Cannot undo — contact update was not completed.");
            return;
        }

        // Restore old values on the in-memory object first.
        student.setEmail(oldEmail);
        student.setPhone(oldPhone);
        student.setUpdatedAt(LocalDateTime.now());

        // Then persist the restored state.
        try {
            DatabaseManager.getInstance().upsert(student.toSubType());
            System.out.println("↶ Undone: Contact info restored for " + student.getStudentId());
            this.undoneAt = LocalDateTime.now();
            this.isUndone = true;
        } catch (SQLException | IllegalAccessException e) {
            // Undo failed — re-apply new values to keep in-memory state consistent with DB.
            student.setEmail(newEmail);
            student.setPhone(newPhone);
            System.err.println("✗ Undo failed — could not restore contact info: " + e.getMessage());
        }
    }

    @Override
    public boolean isUndoable() {
        return executed && successful;
    }

    @Override
    public String getDescription() {
        return String.format("Update contact info for %s (email: %s → %s)",
                student.getFullName(), oldEmail, newEmail != null ? newEmail : oldEmail);
    }

    // -------------------------------------------------------------------------
    // Serialization — for CommandHistory persistence and session recovery
    // -------------------------------------------------------------------------

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("studentPk", student.getId()); // Numeric PK, not the String student_id
        data.put("newEmail",  newEmail);
        data.put("newPhone",  newPhone);
        data.put("oldEmail",  oldEmail);
        data.put("oldPhone",  oldPhone);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("UpdateContactCommand: serialization failed", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        if (json == null || json.isBlank()) return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);
            int studentPk  = (int) data.get("studentPk");
            this.newEmail  = (String) data.get("newEmail");
            this.newPhone  = (String) data.get("newPhone");
            this.oldEmail  = (String) data.get("oldEmail");
            this.oldPhone  = (String) data.get("oldPhone");

            Student raw  = DatabaseManager.getInstance().fetchOne(Student.class, "id", studentPk);
            this.student = ObservableStudent.fromSuperType(raw);
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("Failed to deserialize UpdateContactCommand data", e);
        }
    }

    // -------------------------------------------------------------------------
    // Private Validation Helpers
    // -------------------------------------------------------------------------

    private boolean isValidEmail(String email) {
        // RFC 5322 simplified: local@domain.tld
        return email != null && email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Check whether another active user already owns this email address.
     * Excludes the current user so they can re-save their own email without conflict.
     */
    private boolean isDuplicateEmail(String email, int excludeUserId) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND id <> ? AND is_active = TRUE";
        try {
            return dbManager.executeQuery(sql, rs -> {
                rs.next();
                return rs.getInt(1) > 0;
            }, email, excludeUserId);
        } catch (SQLException e) {
            System.err.println("UpdateContactCommand: duplicate email check failed — " + e.getMessage());
            return false; // Fail open — let the DB UNIQUE constraint catch it
        }
    }
}