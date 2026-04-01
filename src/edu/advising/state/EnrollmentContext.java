package edu.advising.state;

import edu.advising.commands.Enrollment;
import edu.advising.commands.Section;
import edu.advising.core.DatabaseManager;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class EnrollmentContext {

    private final Enrollment enrollment;
    private EnrollmentState state;

    /**
     * Public constructor — allows tests to build an EnrollmentContext
     * from a manually constructed Enrollment without hitting the database.
     */
    public EnrollmentContext(Enrollment enrollment) {
        this.enrollment = enrollment;
        this.state = StateFactory.enrollmentStateFor(enrollment.getStatus());
    }

    public static EnrollmentContext create(int studentId, int sectionId)
            throws SQLException, IllegalAccessException {
        Enrollment enrollment = new Enrollment(studentId, sectionId);
        enrollment.setStatus("PENDING");
        enrollment.setEnrollmentDate(LocalDateTime.now());
        DatabaseManager.getInstance().upsert(enrollment);
        return new EnrollmentContext(enrollment);
    }

    public static EnrollmentContext load(int enrollmentId) throws SQLException {
        Enrollment enrollment = DatabaseManager.getInstance()
                .fetchOne(Enrollment.class, "id", enrollmentId);
        if (enrollment == null) {
            throw new SQLException("Enrollment not found for id: " + enrollmentId);
        }
        return new EnrollmentContext(enrollment);
    }

    public void confirm()                   { state.confirm(this); }
    public void drop(String reason)         { state.drop(this, reason); }
    public void withdraw()                  { state.withdraw(this); }
    public void complete(String finalGrade) { state.complete(this, finalGrade); }
    public void reenroll()                  { state.reenroll(this); }

    public boolean canDrop()     { return state.canDrop(); }
    public boolean canWithdraw() { return state.canWithdraw(); }
    public boolean canComplete() { return state.canComplete(); }
    public boolean canReenroll() { return state.canReenroll(); }

    public void setState(EnrollmentState newState) {
        this.state = newState;
    }

    public void persist() {
        if (enrollment.getSectionId() == 0) return;
        try {
            DatabaseManager.getInstance().upsert(enrollment);
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException("EnrollmentContext.persist() failed: " + e.getMessage(), e);
        }
    }

    public Enrollment getEnrollment() { return enrollment; }
    public EnrollmentState getState() { return state; }

    public Section getSection() throws SQLException {
        return enrollment.getSection();
    }
}