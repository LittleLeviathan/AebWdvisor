package core;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseManager dbm = DatabaseManager.getInstance();

        String sql = "SELECT * FROM users;";
        TestHandler th = new TestHandler();

        dbm.executeQuery(sql, th);
    }
}
