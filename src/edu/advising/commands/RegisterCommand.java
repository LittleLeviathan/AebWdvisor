package edu.advising.commands;

import edu.advising.notifications.NotificationManager;
import edu.advising.notifications.ObservableStudent;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * RegisterCommand - Register student for a course section
 */
class RegisterCommand extends BaseCommand {
    private ObservableStudent student;
    private Section section;
    private NotificationManager notificationManager;
    private int enrollmentId;

    public RegisterCommand(ObservableStudent student, Section section) {
        super();
        this.student = student;
        this.section = section;
        this.notificationManager = NotificationManager.getInstance();
    }

    @Override
    public void execute() {
        executionTime = LocalDateTime.now();

        if (!section.hasCapacity()) {
            logExecution(String.format("✗ Registration failed for %s - section full",
                    section.getCourseCode()));
            successful = false;
            return;
        }

        // Check for schedule conflicts (simplified)
        if (hasScheduleConflict()) {
            logExecution(String.format("✗ Registration failed for %s - schedule conflict",
                    section.getCourseCode()));
            successful = false;
            return;
        }

        // Enroll in memory
        if (section.enroll(student)) {
            // Persist to database
            enrollmentId = persistEnrollment();

            executed = true;
            successful = true;

            logExecution(String.format("✓ Student %s registered for %s",
                    student.getStudentId(), section.getCourseCode()));

            // Trigger notification
            notificationManager.notifyRegistration(student, section.getCourseCode(), true);
        } else {
            successful = false;
        }
    }

    @Override
    public void undo() {
        if (!executed || !successful) {
            System.out.println("Cannot undo - command not executed or failed");
            return;
        }

        // Remove from section
        section.drop(student);

        // Remove from database
        removeEnrollment();

        logExecution(String.format("↶ Undone: Registration for %s", section.getCourseCode()));

        // Notify about drop
        notificationManager.notifyRegistration(student, section.getCourseCode(), false);
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
        // Simplified - in real implementation, check time conflicts
        return false;
    }

    private int persistEnrollment() {
        /*
        try {
            String sql = "INSERT INTO enrollments (student_id, section_id, enrollment_date, status) " +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = DatabaseManager.getInstance().getConnection()
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, student.getId());
            pstmt.setInt(2, section.getId());
            pstmt.setTimestamp(3, Timestamp.valueOf(executionTime));
            pstmt.setString(4, "ENROLLED");
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error persisting enrollment: " + e.getMessage());
        }
        */
        return -1;
    }

    private void removeEnrollment() {
        /*
        try {
            String sql = "UPDATE enrollments SET status = 'DROPPED' WHERE id = ?";
            PreparedStatement pstmt = DatabaseManager.getInstance().getConnection()
                    .prepareStatement(sql);
            pstmt.setInt(1, enrollmentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error removing enrollment: " + e.getMessage());
        }
        */
    }
}

