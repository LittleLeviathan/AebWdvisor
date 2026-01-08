import edu.advising.commands.Course;
import edu.advising.commands.Section;
import edu.advising.core.DatabaseManager;
import edu.advising.notifications.*;
import edu.advising.users.Faculty;
import edu.advising.users.Student;
import edu.advising.users.User;
import edu.advising.users.UserFactory;
import java.sql.SQLException;
import java.util.List;

// Test driver
public class Week5Test {
    public static void main(String[] args) {
        System.out.println("=== WEEK 5: COMMAND PATTERN DEMO ===\n");
        // Grab a copy of the DatabaseManager
        DatabaseManager dbManager = DatabaseManager.getInstance();

        System.out.println("\n=== Fetch just a plain User from the database with new ORM fetch functions. ===\n");
        try {
            User u = dbManager.fetchOne(User.class, "id", 3);
            System.out.println(u.getFullName());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\n=== Fetch Student and Faculty using ORM. Test new Sup/Sub hierarchy model loading. ===\n");
        try {
            Student s = dbManager.fetchOne(Student.class, "id", 3);
            System.out.printf("%s: GPA: %s%n", s.getFullName(), s.getGpa());
            Faculty f = dbManager.fetchOne(Faculty.class, "id", 4);
            System.out.printf("%s: Dept: %s%n", f.getFullName(), f.getDepartment());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\n===  Test OneToMany and ManyToOne relationships. ===\n");
        try {
            Course cis12 = dbManager.fetchOne(Course.class, "id", 1);
            List<Section> sections = cis12.getSections();
            System.out.printf("Course Name: %s%n", cis12.getName());
            for(Section s : sections) {
                System.out.printf("Section Number: %s, Semester: %s, Year: %s%n",
                        s.getSectionNumber(), s.getSemester(), s.getYear());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\n===  Test ManyToMany relationships. ===\n");
        try {
            // Fetch a student from the database.
            Student s1 = dbManager.fetchOne(Student.class, "student_id", "S54322");
            System.out.println(s1.getFullName());
            // Now get the course sections that student is a member of.
            List<Section> s1Sections = s1.getSections();
            for(Section s : s1Sections) {
                System.out.printf("Course: %s Section: %s Semester: %s Year: %s%n",
                        s.getCourseName(), s.getSectionNumber(), s.getSemester(), s.getYear());
                // Now for each section, look in revers and list the students in each section!
                List<Student> students = s.getEnrolledStudents();
                for(Student es : students) {
                    System.out.printf("    %s%n", es.getFullName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\n===  Test Upsert and UpsertAll with relationships. ===\n");
        try {
            Course cis12 = dbManager.fetchOne(Course.class, "id", 1);
            List<Section> sections = cis12.getSections();
            System.out.printf("Course Name: %s%n", cis12.getName());
            for(Section s : sections) {
                System.out.printf("Section Number: %s, Semester: %s, Year: %s%n",
                        s.getSectionNumber(), s.getSemester(), s.getYear());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbManager.shutdown();
    }
}