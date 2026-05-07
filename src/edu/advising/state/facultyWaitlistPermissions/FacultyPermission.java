package edu.advising.state.facultyWaitlistPermissions;

import edu.advising.core.Table;
import edu.advising.core.Column;
import edu.advising.core.Id;

import java.time.LocalDateTime;

/**
 * FacultyPermisson - ORM Entity (Week 9)
 * Maps to the 'faculty_permissions' table in the database.
 * Each field below is a column in that table.
 */
@Table(name = "faculty_permissions")
public class FacultyPermission {

    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;

    @Column(name = "student_id", foreignKey = true)
    private int studentId;

    @Column(name = "section_id", foreignKey = true)
    private int sectionId;

    @Column(name = "faculty_id", foreignKey = true)
    private int facultyId;

    @Column(name = "status")
    private String status; // REQUESTED, APPROVED, DENIED, EXPIRED

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "denial_reason")
    private String denialReason;

    // ===== Constructors =====

    public FacultyPermission() {}

    public FacultyPermission(int studentId, int sectionId, int facultyId) {
        this.studentId = studentId;
        this.sectionId = sectionId;
        this.facultyId = facultyId;
        this.status = "REQUESTED";
        this.requestDate = LocalDateTime.now();
        this.expiryDate = requestDate.plusHours(48);
    }

    // ===== Getters / Setters =====

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public int getSectionId() { return sectionId; }
    public int getFacultyId() { return facultyId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getRequestDate() { return requestDate; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }

    public String getDenialReason() { return denialReason; }
    public void setDenialReason(String denialReason) { this.denialReason = denialReason; }
}