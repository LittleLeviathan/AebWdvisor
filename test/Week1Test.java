import java.sql.Connection;

// Simple test driver
public class Week1Test {
    public static void main(String[] args) {
        // Demonstrate Singleton - same instance returned
        edu.advising.core.DatabaseManager db1 = edu.advising.core.DatabaseManager.getInstance();
        edu.advising.core.DatabaseManager db2 = edu.advising.core.DatabaseManager.getInstance();

        System.out.println("db1 == db2: " + (db1 == db2)); // true
        System.out.println("Database manager initialized successfully");

        try {
            // Test database connection
            String sql = "SELECT u.* FROM users u;";
            db1.executeQuery(sql, rs -> {
                while(rs.next()) {
                    System.out.println(rs.getInt("id"));
                }
                return null;
            });
            System.out.println("Connection valid");
            // Let's seed the database with some values.
            db1.seedDatabase();
            // Explicitly shutting down the database is needed when using file based h2
            // because maven will detect open H2 background threads for database clean-up or MVStore.
            db1.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
