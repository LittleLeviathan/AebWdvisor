package edu.advising.notifications;

import edu.advising.core.Column;
import edu.advising.core.DatabaseManager;
import edu.advising.core.Id;
import edu.advising.core.Table;

import javax.xml.crypto.Data;
import java.sql.SQLException;

/**
 * NotificationPref - Represents a user's notification preference
 */
@Table(name = "notification_preferences")
public class NotificationPref {
    @Id
    @Column(name = "id", upsertIgnore = true)
    private int id;
    @Id
    @Column(name = "notification_type")
    private String notificationType;
    @Id
    @Column(name = "user_id")
    private int userId;
    @Column(name = "email_enabled")
    private boolean emailEnabled;
    @Column(name = "sms_enabled")
    private boolean smsEnabled;
    @Column(name = "push_enabled")
    private boolean pushEnabled;
    @Column(name = "frequency")
    private String frequency;  // IMMEDIATE, DIGEST, DISABLED
    private DatabaseManager dbManager;

    public NotificationPref(String type, int userId) {
        this(type, userId, true, true, true, "IMMEDIATE");
    }

    public NotificationPref(String type, int userId, boolean emailEnabled, boolean smsEnabled, boolean pushEnabled,
                            String frequency) {
        this.notificationType = type;
        this.userId = userId;
        this.emailEnabled = emailEnabled;
        this.smsEnabled = smsEnabled;
        this.pushEnabled = pushEnabled;
        // TODO: Should make restricted fields like this enums.
        this.frequency = frequency; // IMMEDIATE, DIGEST, DISABLED
        this.dbManager = DatabaseManager.getInstance();
    }

    public boolean shouldNotify() {
        return emailEnabled || smsEnabled || pushEnabled;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public boolean isSmsEnabled() {
        return smsEnabled;
    }

    public void setSmsEnabled(boolean smsEnabled) {
        this.smsEnabled = smsEnabled;
    }

    public boolean isPushEnabled() {
        return pushEnabled;
    }

    public void setPushEnabled(boolean pushEnabled) {
        this.pushEnabled = pushEnabled;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void saveNotificationPreference()
            throws SQLException {
        try {
            dbManager.upsert(this);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            System.out.println("Error upserting to database because model is not annotated.");
        }
        /*
        // LOOK HOW MUCH HARDER IT USED TO BE!!! //
        String sql = "INSERT INTO notification_preferences " +
                "(user_id, notification_type, email_enabled, sms_enabled, push_enabled) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "email_enabled = ?, sms_enabled = ?, push_enabled = ?";

        dbManager.executeUpdate(
                sql, userId, notificationType,
                emailEnabled, smsEnabled, pushEnabled,
                emailEnabled, smsEnabled, pushEnabled);
         */
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - Email: %s, SMS: %s, Push: %s (Frequency: %s)",
                userId, notificationType, emailEnabled, smsEnabled, pushEnabled, frequency);
    }
}
