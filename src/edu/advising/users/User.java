package edu.advising.users;

// ============================================================================
// WEEK 2: FACTORY PATTERN  (originally)
// WEEK 5: COMMAND PATTERN  (additions marked ★)
// ============================================================================
// WEEK 5 CHANGES:
//   ★ Added `phone` field with @Column annotation — required by UpdateContactCommand
//     for storing and restoring phone numbers during undo/redo.
//   ★ Added `updatedAt` field — needed for audit trail in contact update undo.
//   ★ Added setEmail(), setPhone(), setUpdatedAt() — mutators needed by command undo.
//   ★ Added getPhone(), getUpdatedAt() — accessors for serialization.
//
// NOTE: The `users` table in DatabaseManager must be migrated to add the `phone`
//       column. Add this line to initializeDatabase() or run as a migration:
//
//   ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(20);
//   ALTER TABLE users ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
//
//   (H2 syntax: ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(20))
//   updated_at is already in the CREATE TABLE definition — no migration needed for that.
// ============================================================================

import edu.advising.core.Column;
import edu.advising.core.Id;
import edu.advising.core.Table;

import java.time.LocalDateTime;

/**
 * User - Base class for all user types in the CRAdvisor system.
 *
 * Uses ORM annotations (@Table, @Column, @Id) so DatabaseManager.upsert()
 * can persist any User subclass without manual SQL strings.
 */
@Table(name = "users")
public class User {

    @Id(isPrimary = true)
    @Column(name = "id", upsertIgnore = true)
    protected int id;

    @Id
    @Column(name = "username")
    protected String username;

    @Column(name = "password")
    protected String password;

    @Column(name = "user_type")
    protected String userType;

    @Column(name = "email")
    protected String email;

    @Column(name = "first_name")
    protected String firstName;

    @Column(name = "last_name")
    protected String lastName;

    @Column(name = "is_active")
    protected boolean isActive;

    @Column(name = "last_login")
    protected LocalDateTime lastLogin;

    // ★ WEEK 5 ADDITION — required by UpdateContactCommand
    // Requires: ALTER TABLE users ADD COLUMN IF NOT EXISTS phone VARCHAR(20);
    @Column(name = "phone")
    protected String phone;

    // ★ WEEK 5 ADDITION — for audit trail in command undo
    // Already exists in DB schema as: updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /** No-arg constructor required by ORM reflective instantiation. */
    public User() {}

    public User(String username, String password, String email, String firstName, String lastName) {
        this.username  = username;
        this.password  = password;
        this.email     = email;
        this.firstName = firstName;
        this.lastName  = lastName;
    }

    // -------------------------------------------------------------------------
    // Business Methods
    // -------------------------------------------------------------------------

    /** Template for displaying user info (expanded further in Template Pattern week). */
    public void displayInfo() {
        System.out.println("User: " + username + " (" + userType + ")");
        System.out.println("Email: " + email);
        if (phone != null && !phone.isEmpty()) {
            System.out.println("Phone: " + phone);
        }
    }

    /** Hook method for subclass dashboards (Student / Faculty). */
    public void showDashboard() {}

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public int getId()           { return id; }
    public String getUsername()  { return username; }
    public String getEmail()     { return email; }
    public String getUserType()  { return userType; }
    public String getPassword()  { return password; }
    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public Boolean isActive() { return isActive; }
    public LocalDateTime getLastLogin()  { return lastLogin; }
    public String getPhone()     { return phone; }      // ★ WEEK 5
    public LocalDateTime getUpdatedAt() { return updatedAt; } // ★ WEEK 5

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // -------------------------------------------------------------------------
    // Setters
    // -------------------------------------------------------------------------

    public void setId(int id)                { this.id = id; }
    public void setFirstName(String firstName){ this.firstName = firstName; }
    public void setLastName(String lastName)  { this.lastName = lastName; }

    // ★ WEEK 5 — needed by UpdateContactCommand.execute() and undo()
    public void setEmail(String email)            { this.email = email; }
    public void setUserType(String userType)      { this.userType = userType; }
    public void setActive(Boolean isActive)     { this.isActive = isActive; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    public void setPhone(String phone)            { this.phone = phone; }
    public void setUpdatedAt(LocalDateTime ts)    { this.updatedAt = ts; }
}