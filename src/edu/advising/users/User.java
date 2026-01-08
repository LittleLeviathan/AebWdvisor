// Week 2: FACTORY PATTERN
// Features Implemented: User Account Creation (Student/Faculty)
// Why Now: Need to create different user types polymorphically

package edu.advising.users;

import edu.advising.core.Column;
import edu.advising.core.Id;
import edu.advising.core.Table;

import java.time.LocalDateTime;

/**
 * ADD ANNOTATIONS during Command Pattern Week
 * -
 * User - Base class for all user types
 */
@Table(name = "users")
public class User {
    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    protected int id;
    @Column(name = "username")
    protected String username;
    @Column(name = "password")
    protected String password;
    @Column(name = "user_type")
    protected String userType;
    @Id
    @Column(name = "email")
    protected String email;
    @Column(name = "first_name")
    protected String firstName;
    @Column(name = "last_name")
    protected String lastName;
    @Column(name = "is_active")
    protected boolean isActive;
    @Column(name = "last_login")
    LocalDateTime lastLogin;
    /*
    // Missing fields ATM...
    created_at TIMESTAMP
    updated_at TIMESTAMP
    */
    public User() {} // Empty constructor needed for ORM features later.

    public User(String username, String password, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Template for displaying user info (will expand in Template Pattern week)
    public void displayInfo() {
        System.out.println("User: " + username + " (" + userType + ")");
        System.out.println("Email: " + email);
    }

    public void showDashboard() {} // A hook for Student or Faculty

    // Getters
    public int getId() { return this.id; }
    public String getUsername() { return this.username; }
    public String getEmail() { return this.email; }
    public String getUserType() { return userType; }
    public String getPassword() { return this.password; }
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
    public void setId(int id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}