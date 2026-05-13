package edu.advising.repository;

import edu.advising.commands.Course;

import java.util.List;

public class CourseFilter {

    private CourseFilter() {}

    public static List<Course> filterByCode(List<Course> courses, String query) {
        String lower = query.toLowerCase();
        return courses.stream()
                .filter(c -> c.getCode().toLowerCase().contains(lower)
                        || c.getName().toLowerCase().contains(lower))
                .toList();
    }

    public static List<Course> filterByLevel(List<Course> courses, String level) {
        return courses.stream()
                .filter(c -> c.getLevel().equalsIgnoreCase(level))
                .toList();
    }

    public static List<Course> filterByDepartment(List<Course> courses, int departmentId) {
        return courses.stream()
                .filter(c -> c.getDepartmentId() == departmentId)
                .toList();
    }
}