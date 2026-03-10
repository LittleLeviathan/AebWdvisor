package edu.advising.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Table;
import edu.advising.notifications.ObservableStudent;
import edu.advising.users.Student;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * DropCommand - Drop a course section
 */
@Table(name = "command_history", isSubTable = true)
public class DropCommand extends BaseCommand {
    private ObservableStudent student;
    private Section section;
    private int previousEnrollmentId;
    private DatabaseManager dbManager;

    // Adding No argument constructor needed for fromSuperType() and ORM autoMapper()
    public DropCommand() {
        this(null, null);
    }

    public DropCommand(ObservableStudent student, Section section) {
        super();
        this.commandType = "DROP";
        this.student = student;
        this.section = section;
        this.dbManager = DatabaseManager.getInstance();
    }

    public static DropCommand fromSuperType(BaseCommand base) {
        DropCommand cmd = new DropCommand();
        BaseCommand.copyBaseFields(base, cmd);
        cmd.initAfterLoad();
        return cmd;
    }

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        if (section.drop(student)) {
            // Update database
            updateEnrollmentStatus("DROPPED");

            executed = true;
            successful = true;

            System.out.printf("✓ Student %s dropped %s%n",
                    student.getStudentId(), section.getCourseCode());

            // Check waitlist and promote next student
            try {
                promoteFromWaitlist();
            } catch (SQLException | IllegalAccessException e) {
                e.printStackTrace();
                System.out.println("Failed to promote from waitlist.");
            }
        } else {
            successful   = false;
            errorMessage = String.format("Drop failed — student not enrolled in %s",
                    section.getCourseCode());
            System.out.println("✗ " + errorMessage);
        }
    }

    @Override
    public void undo() {
        if (!executed || !successful) {
            System.out.println("Cannot undo - command not executed or failed");
            return;
        }

        // Re-enroll
        if (section.enroll(student) > 0) {
            updateEnrollmentStatus("ENROLLED");
            System.out.printf("↶ Undone: Drop of %s - student re-enrolled%n",
                    section.getCourseCode());
            this.undoneAt = LocalDateTime.now();
            this.isUndone = true;
        }
    }

    @Override
    public boolean isUndoable() {
        return executed && successful && section.hasCapacity();
    }

    @Override
    public String getDescription() {
        return String.format("Drop %s (%s)", section.getCourseCode(), section.getCourseName());
    }

    private void updateEnrollmentStatus(String status) {
        // Section.drop() already updates the enrollment via ORM upsert.
        // This method exists as a safety net for direct DropCommand use outside Section.
        try {
            String sql = "UPDATE enrollments SET status = ? " +
                    "WHERE student_id = ? AND section_id = ? AND status = 'ENROLLED'";
            dbManager.executeUpdate(sql, status, student.getId(), section.getId());
        } catch (SQLException e) {
            System.err.println("DropCommand: enrollment status sync failed — " + e.getMessage());
        }
    }

    private void promoteFromWaitlist() throws SQLException, IllegalAccessException {
        if (!section.getWaitlist().isEmpty() && section.hasCapacity()) {
            // Get the next waitlist entry
            WaitlistEntry nextWaitlistEntry = section.getWaitlist().get(0);
            // Lookup the student for this entry
            Student student = nextWaitlistEntry.getStudent();
            // Remove that student from the waitlist
            section.removeFromWaitlist(student);
            section.enroll(student);
            System.out.println(String.format("↑ Student ID %s promoted from waitlist", student.getStudentId()));

            // In real implementation, notify the student with observer!!!
        }
    }

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("studentPk",            student.getId());   // int PK
        data.put("studentId", student.getStudentId());
        data.put("sectionId", section.getId()); // Assuming Section has an id
        data.put("previousEnrollmentId", previousEnrollmentId);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize DropCommand data", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        if (json == null || json.isBlank()) return;
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, Map.class);
            int studentPk = (int) data.get("studentPk");
            int sectionId = (int) data.get("sectionId");
            this.previousEnrollmentId = (int) data.get("previousEnrollmentId");

            Student raw  = DatabaseManager.getInstance().fetchOne(Student.class, "id", studentPk);
            this.student = ObservableStudent.fromSuperType(raw);
            this.section = DatabaseManager.getInstance().fetchOne(Section.class, "id", sectionId);
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("Failed to deserialize DropCommand data", e);
        }
    }
}