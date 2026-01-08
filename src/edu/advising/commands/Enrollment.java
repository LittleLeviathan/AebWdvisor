package edu.advising.commands;

import edu.advising.core.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Course Section Enrollment - Represents an enrollment in a course section
 */
@Table(name = "enrollments")
public class Enrollment {
    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "student_id")
    private int studentId; // References sudents
    @Id
    @Column(name = "section_id") // References sections
    private int sectionId;
    @Column(name = "enrollment_date")
    private LocalDateTime enrollmentDate;
    @Column(name = "status")
    private String status; // ENROLLED, DROPPED, WITHDRAWN, COMPLETED
    @Column(name = "grade")
    private String grade;
    @Column(name = "grade_points")
    private BigDecimal gradePoints;
    @Column(name = "midterm_grade")
    private String midtermGrade;
    @Column(name = "final_grade")
    private String finalGrade;
    @Column(name = "graded_at")
    private LocalDateTime gradedAt;
    @Column(name = "dropped_at")
    private LocalDateTime droppedAt;
    @Column(name = "drop_reason")
    private String dropReason;
    @ManyToOne(targetEntity = Section.class, joinColumn = "section_id")
    private Section section; // Cached object representing this enrollment's course section.

    public Enrollment() {}

    public Enrollment(int studentId, int sectionId) {
        this.studentId = studentId;
        this.sectionId = sectionId;
        this.status = "ENROLLED";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public LocalDateTime getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDateTime enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public BigDecimal getGradePoints() {
        return gradePoints;
    }

    public void setGradePoints(BigDecimal gradePoints) {
        this.gradePoints = gradePoints;
    }

    public String getMidtermGrade() {
        return midtermGrade;
    }

    public void setMidtermGrade(String midtermGrade) {
        this.midtermGrade = midtermGrade;
    }

    public String getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(String finalGrade) {
        this.finalGrade = finalGrade;
    }

    public LocalDateTime getGradedAt() {
        return gradedAt;
    }

    public void setGradedAt(LocalDateTime gradedAt) {
        this.gradedAt = gradedAt;
    }

    public LocalDateTime getDroppedAt() {
        return droppedAt;
    }

    public void setDroppedAt(LocalDateTime droppedAt) {
        this.droppedAt = droppedAt;
    }

    public String getDropReason() {
        return dropReason;
    }

    public void setDropReason(String dropReason) {
        this.dropReason = dropReason;
    }

    public Section getSection() throws SQLException {
        if (this.section == null) {
            // Lazy Load: Use the generic fetchOne from DatabaseManager
            this.section = DatabaseManager.getInstance()
                    .fetchOne(Section.class, "section_id", this.sectionId);
        }
        return (this.section != null) ? this.section : null;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
