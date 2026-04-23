package edu.advising.repository;
import edu.advising.commands.Section;
import edu.advising.core.DatabaseManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
public class SectionRepository {
    private static final DatabaseManager db = DatabaseManager.getInstance();
    public static Section findById(int id) throws SQLException {
        return db.fetch("SELECT * FROM sections WHERE id = ?",
                rs -> mapRow(rs),
                id
        );
    }
    public static List<Section> findAll() throws SQLException {
        return db.fetchList("SELECT * FROM sections",
                rs -> mapRow(rs)
        );
    }
    // helper method for constructing Courses from the ResultSet from the Database
    private static Section mapRow(ResultSet rs) throws SQLException {
        return new Section(
                rs.getInt("id"),
                rs.getInt("course_id"),
                rs.getString("section_number"),
                rs.getString("semester"),
                rs.getInt("year"),
                rs.getInt("capacity"),
                rs.getInt("enrolled"),
                rs.getInt("faculty_id"),
                rs.getString("room"),
                rs.getString("status")
        );
    }
}