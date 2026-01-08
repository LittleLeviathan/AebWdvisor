package edu.advising.commands;

import edu.advising.core.*;

import java.sql.SQLException;
import java.util.List;

/**
 * ADD ANNOTATIONS during Command Pattern Week
 * -
 * Course Section - Represents a course section
 */
@Table(name = "courses")
public class Course {
    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "credits")
    private double credits;
    @Column(name = "department_id")
    private int departmentId;
    @Column(name = "level")
    private String level;
    @Column(name = "is_active")
    private boolean isActive;
    @OneToMany(targetEntity = Section.class, mappedBy = "course_id")
    private List<Section> sections; // Cached list of available sections.

    public Course() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getCredits() {
        return credits;
    }

    public void setCredits(double credits) {
        this.credits = credits;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Section> getSections() throws SQLException {
        if (this.sections == null) {
            // Lazy Load: Use the generic fetchMany from DatabaseManager
            this.sections = DatabaseManager.getInstance()
                    .fetchMany(Section.class, "course_id", this.id);
        }
        return this.sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
