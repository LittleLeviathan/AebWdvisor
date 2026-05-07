package edu.advising.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Table;
import edu.advising.notifications.NotificationManager;
import edu.advising.notifications.ObservableStudent;
import edu.advising.state.EnrollmentContext;
import edu.advising.state.facultyWaitlistPermissions.FacultyPermission;
import edu.advising.state.facultyWaitlistPermissions.FacultyPermissionContext;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.advising.users.Student;

import java.util.HashMap;
import java.util.Map;

/**
 * RegisterCommand - Register student for a course section
 */
@Table(name = "command_history", isSubTable = true)
public class RegisterCommand extends BaseCommand {
    private ObservableStudent student;
    private Section section;
    private final NotificationManager notificationManager;
    private int enrollmentId;

    // Adding No argument constructor needed for fromSuperType() and ORM autoMapper()
    public RegisterCommand() {
        this(null, null);
    }

    public RegisterCommand(ObservableStudent student, Section section) {
        super();
        this.commandType = "REGISTER";
        this.student = student;
        this.section = section;
        this.notificationManager = NotificationManager.getInstance();
    }

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        if (!section.hasCapacity()) {
            // Check if the student has a valid (APPROVED) faculty permission
            // for this specific section before blocking registration
            if (!hasValidFacultyPermission()) {
                successful = false;
                errorMessage = String.format("Registration failed for %s - section full", section.getCourseCode());
                System.out.println("✗ " + errorMessage);
                return;
            }
            System.out.printf("✓ Faculty permission bypassing capacity check for %s%n", section.getCourseCode());
        }

        if (hasScheduleConflict()) {
            successful = false;
            errorMessage = String.format("Registration failed for %s - schedule conflict", section.getCourseCode());
            System.out.println("✗ " + errorMessage);
            return;
        }

        try {
            EnrollmentContext ctx = EnrollmentContext.create(
                    student.getId(), section.getId());
            ctx.confirm();
            this.enrollmentId = ctx.getEnrollment().getId();
            executed   = true;
            successful = true;
            System.out.printf("✓ Student %s registered for %s%n",
                    student.getStudentId(), section.getCourseCode());
            notificationManager.notifyRegistration(student, section.getCourseCode(), true);
        } catch (Exception e) {
            successful   = false;
            errorMessage = "Registration failed: " + e.getMessage();
        }
    }

    /**
     * Checks whether the student has a valid (APPROVED and not expired)
     * FacultyPermission for this specific section.
     * Queries the faculty_permissions table by student_id and section_id,
     * loads the context (which auto-expires if past expiryDate), and
     * returns true only if isValid() is true.
     */
    private boolean hasValidFacultyPermission() {
        try {
            String sql = "SELECT * FROM faculty_permissions WHERE student_id = ? AND section_id = ? LIMIT 1";
            List<FacultyPermission> results = DatabaseManager.getInstance().fetchList(
                    sql,
                    rs -> {
                        FacultyPermission fp = new FacultyPermission();
                        fp.setId(rs.getInt("id"));
                        return fp;
                    },
                    student.getId(), section.getId()
            );

            if (results.isEmpty()) {
                return false;
            }

            // Load the full context — this auto-expires if past expiryDate
            FacultyPermissionContext ctx = FacultyPermissionContext.load(results.get(0).getId());
            return ctx.isValid();

        } catch (SQLException e) {
            System.err.println("RegisterCommand: error checking faculty permission: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void undo() {
        if (!executed || !successful) {
            System.out.println("Cannot undo - command not executed or failed");
            return;
        }

        // Remove from section
        if( section.drop(student) ) {
            System.out.printf("↶ Undone: Registration for %s%n", section.getCourseCode());
            this.undoneAt = LocalDateTime.now();
            this.isUndone = true;
            // Notify about drop
            notificationManager.notifyRegistration(student, section.getCourseCode(), false);
        }
    }

    @Override
    public boolean isUndoable() {
        return executed && successful;
    }

    @Override
    public String getDescription() {
        return String.format("Register for %s (%s)", section.getCourseCode(), section.getCourseName());
    }

    private boolean hasScheduleConflict() {
        // Simplified - in real implementation, check time conflicts in student.
        return false;
    }

    @Override
    protected String serializeCommandData() {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = new HashMap<>();
        data.put("studentPk", student.getId());
        data.put("studentId", student.getStudentId());
        data.put("sectionId", section.getId());
        data.put("enrollmentId", enrollmentId);
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize RegisterCommand data", e);
        }
    }

    @Override
    protected void deserializeCommandData(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> data = mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<>(){});
            int studentPk = (int) data.get("studentPk");
            int sectionId = (int) data.get("sectionId");
            this.enrollmentId = (int) data.get("enrollmentId");

            Student raw = DatabaseManager.getInstance().fetchOne(Student.class, "id", studentPk);
            if (raw != null) {
                this.student = ObservableStudent.fromSuperType(raw);
                this.section = DatabaseManager.getInstance().fetchOne(Section.class, "id", sectionId);
            }
        } catch (JsonProcessingException | SQLException e) {
            throw new RuntimeException("Failed to deserialize RegisterCommand data", e);
        }
    }
}