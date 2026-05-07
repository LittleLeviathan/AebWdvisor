package edu.advising;

import edu.advising.auth.AuthenticationContext;
import edu.advising.auth.BasicAuthentication;
import edu.advising.core.DatabaseManager;
import edu.advising.state.ViewContext;
import javafx.application.Application;
import javafx.stage.Stage;
import edu.advising.gui.LoginScreen;
import edu.advising.users.UserFactory;

import java.sql.SQLException;

public class BetterAdvisorApp extends Application {

    public static Stage primaryStage;
    public static ViewContext viewContext;

    @Override
    public void start(Stage stage) throws SQLException {
        primaryStage = stage;

        // Set up authentication
        AuthenticationContext authContext = new AuthenticationContext(new BasicAuthentication());

        // Set up the ViewContext (navigation brain)
        viewContext = new ViewContext();
        viewContext.setAuthContext(authContext);

        // Create test data for development — skip if they already exist
        try {
            addTestData();
        } catch (RuntimeException | SQLException e) {
            Throwable cause = (e instanceof RuntimeException) ? e.getCause() : e;
            if (cause instanceof SQLException) {
                System.out.println(e.getMessage());
            } else {
                throw e;
            }
        }

        // Set window title and size
        stage.setTitle("BetterAdvisor");
        stage.setWidth(900);
        stage.setHeight(650);

        // Initialize the state machine
        viewContext.start();

        // Show the login screen
        stage.setScene(LoginScreen.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static void addTestData() throws SQLException {
        UserFactory userFactory = new UserFactory();
        DatabaseManager db = DatabaseManager.getInstance();

        userFactory.createUser("FACULTY", "testFaculty", "Password1!", "faculty@school.edu", "Jane", "Smith", "F001", "Computer Science");
        userFactory.createUser("STUDENT", "testStudent", "Password1!", "test@school.edu", "John", "Doe", "S12345");

        // Sample faculty user for test sections
        int facultyId = db.executeInsert("INSERT INTO users (username, password, user_type, first_name, last_name, email) " +
                "VALUES ('prof_jones', 'Password1!', 'FACULTY', 'Alan', 'Jones', 'ajones@school.edu')");
        db.executeUpdate("INSERT INTO faculty (id, employee_id, department) VALUES (?, 'F002', 'Computer Science')", facultyId);

        int faculty2Id = db.executeInsert("INSERT INTO users (username, password, user_type, first_name, last_name, email) " +
                "VALUES ('prof_kim', 'Password1!', 'FACULTY', 'Susan', 'Kim', 'skim@school.edu')");
        db.executeUpdate("INSERT INTO faculty (id, employee_id, department) VALUES (?, 'F003', 'Mathematics')", faculty2Id);

// Sample courses
// Insert departments first
        int csDeptId = db.executeInsert("INSERT INTO departments (name, code) VALUES ('Computer Science', 'CS')");
        int mathDeptId = db.executeInsert("INSERT INTO departments (name, code) VALUES ('Mathematics', 'MATH')");

        int cs101Id = db.executeInsert("INSERT INTO courses (code, name, description, credits, department_id, level, is_active) " +
                "VALUES ('CS101', 'Intro to Programming', 'Fundamentals of programming using Java.', 3, ?, '100', TRUE)", csDeptId);
        int cs201Id = db.executeInsert("INSERT INTO courses (code, name, description, credits, department_id, level, is_active) " +
                "VALUES ('CS201', 'Data Structures', 'Arrays, linked lists, trees, and graphs.', 3, ?, '200', TRUE)", csDeptId);
        int cs301Id = db.executeInsert("INSERT INTO courses (code, name, description, credits, department_id, level, is_active) " +
                "VALUES ('CS301', 'Algorithms', 'Sorting, searching, and complexity analysis.', 3, ?, '300', TRUE)", csDeptId);
        int math101Id = db.executeInsert("INSERT INTO courses (code, name, description, credits, department_id, level, is_active) " +
                "VALUES ('MATH101', 'Calculus I', 'Limits, derivatives, and integrals.', 4, ?, '100', TRUE)", csDeptId);
        int math201Id = db.executeInsert("INSERT INTO courses (code, name, description, credits, department_id, level, is_active) " +
                "VALUES ('MATH201', 'Linear Algebra', 'Vectors, matrices, and transformations.', 3, ?, '200', TRUE)", csDeptId);

// Sample sections (Spring 2026)
        db.executeUpdate("INSERT INTO sections (course_id, section_number, semester, `year`, capacity, enrolled, faculty_id, room, status) " +
                "VALUES (?, '01', 'SPRING', 2026, 30, 24, ?, 'SCI-101', 'OPEN')", cs101Id, facultyId);
        db.executeUpdate("INSERT INTO sections (course_id, section_number, semester, `year`, capacity, enrolled, faculty_id, room, status) " +
                "VALUES (?, '02', 'SPRING', 2026, 30, 30, ?, 'SCI-102', 'CLOSED')", cs101Id, facultyId);
        db.executeUpdate("INSERT INTO sections (course_id, section_number, semester, `year`, capacity, enrolled, faculty_id, room, status) " +
                "VALUES (?, '01', 'SPRING', 2026, 25, 10, ?, 'ENG-205', 'OPEN')", cs201Id, faculty2Id);
        db.executeUpdate("INSERT INTO sections (course_id, section_number, semester, `year`, capacity, enrolled, faculty_id, room, status) " +
                "VALUES (?, '01', 'SPRING', 2026, 20, 19, ?, 'SCI-310', 'OPEN')", cs301Id, facultyId);
        db.executeUpdate("INSERT INTO sections (course_id, section_number, semester, `year`, capacity, enrolled, faculty_id, room, status) " +
                "VALUES (?, '01', 'SPRING', 2026, 35, 5, ?, 'MATH-100', 'OPEN')", math101Id, faculty2Id);
        db.executeUpdate("INSERT INTO sections (course_id, section_number, semester, `year`, capacity, enrolled, faculty_id, room, status) " +
                "VALUES (?, '01', 'SPRING', 2026, 28, 28, ?, 'MATH-201', 'CLOSED')", math201Id, faculty2Id);
    }
}