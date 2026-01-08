package edu.advising.commands;

import edu.advising.core.*;
import edu.advising.users.Student;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Course Section - Represents a course section
 */
@Table(name = "sections")
public class Section {
    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "course_id")
    private int courseId;  // References courses
    @Id
    @Column(name = "section_number")
    private String sectionNumber;
    @Id
    @Column(name = "semester")
    private String semester;
    @Id
    @Column(name = "year")
    private int year;
    @Column(name = "capacity")
    private int capacity;
    @Column(name = "enrolled")
    private int enrolled;
    @Column(name = "faculty_id")
    private int facultyId; // References faculty
    @Column(name = "room")
    private String room;
    @Column(name = "status")
    private String status;  //OPEN, CLOSED, CANCELLED
    @ManyToOne(targetEntity = Course.class, joinColumn = "course_id")
    private Course course; // Cached object representing this sections courses.
    @ManyToMany(
            targetEntity = Student.class,
            joinTable = "enrollments",
            joinColumn = "section_id",
            inverseJoinColumn = "student_id"
    )
    private List<Student> enrolledStudents;
    @OneToMany(targetEntity = Enrollment.class, mappedBy = "section_id")
    private List<Enrollment> enrollments;

    // TODO: Figure out lazy loading of linked data-structures like waitlist and faculty
    private List<Integer> waitlist;

    public Section() {}

    public Section(int id, int courseId, String sectionNumber,
                   String semester, int year, int capacity, int enrolled, int facultyId, String room, String status) {
        this.id = id;
        this.courseId = courseId;
        this.sectionNumber = sectionNumber;
        this.semester = semester;
        this.year = year;
        this.capacity = capacity;
        this.enrolled = enrolled;
        this.facultyId = facultyId;
        this.room = room;
        this.status = status;
        this.enrolledStudents = new ArrayList<>();
        this.waitlist = new ArrayList<>();
    }

    public Section(int id, int courseId, String sectionNumber,
                   String semester, int year, int capacity, int enrolled, int facultyId) {
        this(id, courseId, sectionNumber, semester, year, capacity, enrolled, facultyId, null, null);
    }

    public Section(int courseId, String sectionNumber,
                   String semester, int year, int capacity, int enrolled, int facultyId) {
        this(-1, courseId, sectionNumber, semester, year, capacity, enrolled, facultyId, null, null);
    }

    public Section(int courseId, String sectionNumber,
                   String semester, int year, int capacity) {
        this(-1, courseId, sectionNumber, semester, year, capacity, -1, -1, null, null);
    }

    public boolean hasCapacity() {
        return enrolled < capacity;
    }

    private boolean isAlreadyEnrolled(Student newStudent) {
        return enrolledStudents.stream().anyMatch(student -> student.getId() == newStudent.getId());
    }

    public boolean enroll(Student newStudent) {
        if (hasCapacity() && !isAlreadyEnrolled(newStudent)) {
            // TODO: Fix upsertAll and upsert code to handle related tables and see if simply doing this with an
            //   upsertAll works after putting newStudent in enrolledStudents.
            //   BUT; For now, I'm manually creating a new Enrollment.
            try {
                Enrollment enrollment = new Enrollment(newStudent.getId(), this.getId());
                DatabaseManager.getInstance().upsert(enrollment);
                enrolledStudents.add(newStudent);
                enrolled++;
                // TODO: Also need to upsertAll to this Section to save enrolled data back
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error enrolling student due to DB.");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                System.out.println("Error enrolling student due to Reflection.");
            }
            return false;
        }
        return false;
    }

    public boolean drop(Student dropStudent) {
        // TODO: Create Generic Delete code in DatabaseManager that allows for deleting models generically.
        //   BUT; For now, I'm manually updating the enrollment.
        // First let's see if we can find an Enrollment for this student.
        try {
            Optional<Enrollment> optionalEnrollment = this.getEnrollments().stream()
                    .filter(enrollment -> enrollment.getStudentId() == dropStudent.getId()).findAny();
            if(optionalEnrollment.isPresent()) {
                // Update the Enrollment with the DROP status
                Enrollment enrollment = optionalEnrollment.get();
                enrollment.setStatus("DROPPED");
                enrollment.setDroppedAt(LocalDateTime.now());
                DatabaseManager.getInstance().upsert(enrollment);
                if( enrolledStudents.removeIf(student -> student.getId() == dropStudent.getId()) ) {
                    enrolled--;
                    return true;
                }
            }
        } catch (SQLException | IllegalAccessException e) { e.printStackTrace(); }
        return false;
    }

    public void addToWaitlist(int studentId) {
        if (!waitlist.contains(studentId)) {
            waitlist.add(studentId);
        }
    }

    public boolean removeFromWaitlist(int studentId) {
        return waitlist.remove(Integer.valueOf(studentId));
    }

    public int getWaitlistPosition(int studentId) {
        return waitlist.indexOf(studentId) + 1; // 1-based
    }

    // Getters
    public int getId() { return id; }
    public String getSectionNumber() { return sectionNumber; }
    public String getSemester() { return semester; }
    public int getCapacity() { return capacity; }
    public int getEnrolled() { return enrolled; }
    public int getAvailableSeats() { return capacity - enrolled; }
    public List<Integer> getWaitlist() { return new ArrayList<>(waitlist); }

    public String getCourseName() {
        try {
            Course c = this.getCourse();
            return (c != null) ? c.getName() : "UNKNOWN";
        } catch (SQLException se) {
            se.printStackTrace();
            return "UNKNOWN (Cause: DB ERROR)";
        }
    }

    public String getCourseCode() {
        // TODO: Maybe link to courses-course here and pull back the course name instead of using courseId?
        return String.valueOf(courseId) + "-" +  sectionNumber + "-" + semester + "-" + year; // 5-2-SP-26
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%s-%s: %s %s (%d/%d enrolled)",
                courseId, sectionNumber, semester, year, enrolled, capacity);
    }

    public Course getCourse() throws SQLException {
        if (this.course == null) {
            // Lazy Load: Use the generic fetchOne from DatabaseManager
            this.course = DatabaseManager.getInstance()
                    .fetchOne(Course.class, "id", this.courseId);
        }
        return (this.course != null) ? this.course : null;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Student> getEnrolledStudents() throws SQLException {
        if (this.enrolledStudents == null) {
            this.enrolledStudents = DatabaseManager.getInstance().fetchManyToMany(
                    Student.class, "enrollments", "section_id", "student_id", this.getId()
            );
        }
        return this.enrolledStudents;
    }

    public void setEnrolledStudents(List<Student> students) {
        this.enrolledStudents = students;
    }

    public List<Enrollment> getEnrollments() throws SQLException {
        // TODO: Gotta find a way to modify the fetch calls to take additional filters since this will return
        //   Enrollements in ANY status (i.e. DROPPED, etc.).
        if (this.enrollments == null) {
            // Lazy Load: Use the generic fetchMany from DatabaseManager
            this.enrollments = DatabaseManager.getInstance()
                    .fetchMany(Enrollment.class, "section_id", this.id);
        }
        return this.enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }
}
