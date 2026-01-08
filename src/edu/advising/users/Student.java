package edu.advising.users;

import edu.advising.commands.Section;
import edu.advising.core.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * ADD ANNOTATION STUFF ON Command Pattern Week
 * -
 * Student - Concrete user type
 */
@Table(name = "students")
public class Student extends User {
    @Id
    @Column(name = "student_id")
    private String studentId;
    @Column(name = "gpa")
    private BigDecimal gpa;
    @Column(name = "enrollment_status")
    private String enrollmentStatus;
    @Column(name = "academic_standing")
    private String academicStanding;
    @Column(name = "classification")
    private String classification;
    @Column(name = "major")
    private String major;
    @Column(name = "minor")
    private String minor;
    @Column(name = "advisor_id")
    private int advisorId;

    @ManyToMany(
            targetEntity = Section.class,
            joinTable = "enrollments",
            joinColumn = "student_id", // Linking table's FK for Student & User table's PK
            inverseJoinColumn = "section_id" // Linking table's FK for Section table's PK
    )
    private List<Section> sections;

    public Student() {}

    public Student(String username, String password, String email,
                   String firstName, String lastName, String studentId) {
        super(username, password, email, firstName, lastName);
        this.userType = "STUDENT";
        this.studentId = studentId;
        this.gpa = new BigDecimal("0.0");
    }

    @Override
    public void showDashboard() {
        System.out.println("\n=== STUDENT DASHBOARD ===");
        System.out.println("Student ID: " + studentId);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("GPA: " + gpa.toPlainString());
        System.out.println("\nAvailable Features:");
        System.out.println("- Register for Classes");
        System.out.println("- View Schedule");
        System.out.println("- Check Grades");
        System.out.println("- Financial Aid");
        System.out.println("- Make Payment");
    }

    // Getters and setters
    public String getStudentId() {
        return studentId;
    }

    public BigDecimal getGpa() {
        return gpa;
    }

    public void setGpa(BigDecimal gpa) {
        this.gpa = gpa;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public String getAcademicStanding() {
        return academicStanding;
    }

    public void setAcademicStanding(String academicStanding) {
        this.academicStanding = academicStanding;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

    public int getAdvisorId() {
        return advisorId;
    }

    public void setAdvisorId(int advisorId) {
        this.advisorId = advisorId;
    }

    public List<Section> getSections() throws SQLException {
        if (this.sections == null) {
            this.sections = DatabaseManager.getInstance().fetchManyToMany(
                    Section.class, "enrollments",
                    "student_id", // Linking table's FK for Student & User table's PK
                    "section_id", // Linking table's FK for Section table's PK
                    this.id
            );
        }
        return this.sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
