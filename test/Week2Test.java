import edu.advising.core.DatabaseManager;
import edu.advising.users.User;
import edu.advising.users.UserFactory;

// Test driver
public class Week2Test {
    public static void main(String[] args) {
        UserFactory factory = new UserFactory();

        // Create different user types
        User student = factory.createUser("STUDENT", "jdoe", "pass123",
                "john.doe@college.edu", "John", "Doe", "S12345");

        User faculty = factory.createUser("FACULTY", "profsmith", "prof456",
                "smith@college.edu", "Jane", "Smith", "F98765", "Computer Science");

        // Display dashboards
        student.showDashboard();
        faculty.showDashboard();

        // Test retrieval
        User retrieved = factory.getUserByUsername("jdoe");
        if (retrieved != null) {
            System.out.println("\nRetrieved user: " + retrieved.getUsername());
            retrieved.displayInfo();
        }
        DatabaseManager dbManager = DatabaseManager.getInstance();
        dbManager.shutdown();
    }
}