package edu.advising.repository;

import edu.advising.commands.Course;
import edu.advising.core.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CourseRepository {
    private static final DatabaseManager db = DatabaseManager.getInstance();
    public static Course findById(int id) throws SQLException {
        return db.fetch("SELECT * FROM courses WHERE id = ?",
                rs -> mapRow(rs),
                id
        );
    }
    public static List<Course> findAll() throws SQLException {
        return db.fetchList("SELECT * FROM courses",
                rs -> mapRow(rs)
        );
    }
    // helper method for constructing Courses from the ResultSet from the Database
    private static Course mapRow(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getInt("id"));
        course.setCode(rs.getString("code"));
        course.setName(rs.getString("name"));
        course.setDescription(rs.getString("description"));
        course.setCredits(rs.getInt("credits"));
        course.setDepartmentId(rs.getInt("departmentID"));
        course.setLevel(rs.getString("level"));
        course.setActive(rs.getBoolean("is_active"));
        return course;
    }
}