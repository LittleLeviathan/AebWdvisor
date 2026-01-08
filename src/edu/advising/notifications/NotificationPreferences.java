package edu.advising.notifications;

import edu.advising.core.DatabaseManager;

import java.sql.SQLException;
import java.util.*;

/**
 * NotificationPreferences - User notification settings
 */
public class NotificationPreferences {
    private int userId;
    private DatabaseManager dbManager;
    private List<NotificationPref> collection;

    public NotificationPreferences(int userId) {
        // TODO: Store various channels in database too, maybe in EAV like structure, allowing channels to be dynamic.
        //   Or use a factory to load a NotificationChannel(s) per Notification.
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
        this.collection = loadNotificationPreferences(userId);
    }

    /*
     * Set user notification preferences.
     */
    public void saveNotificationPreferences()
            throws SQLException {
        try {
            dbManager.upsertAll(this.collection);
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
            System.out.println("Error upserting to database because model is not annotated.");
        }
    }

    /*
     * Add a new preference to NotificationPreferences
     */
    public void addNotificationPref(NotificationPref pref) {
        this.collection.add(pref);
    }

    /*
     * Check preferences before sending
     */
    private List<NotificationPref> loadNotificationPreferences(int userId) {
        // TODO: Add frequncy to this SQL, IMMEDIATE, DIGEST, DISABLED.
        String sql = "SELECT * FROM notification_preferences WHERE user_id = ?";

        try {
            return dbManager.fetchList(sql, rs -> {
                // This lambda runs ONCE per row found in the database
                NotificationPref n = new NotificationPref(
                        rs.getString("notification_type"),
                        rs.getInt("user_id"),
                        rs.getBoolean("email_enabled"),
                        rs.getBoolean("sms_enabled"),
                        rs.getBoolean("push_enabled"),
                        rs.getString("frequency")
                );
                n.setId(rs.getInt("id"));
                return n;
            }, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error loading notification preferences");
            return new ArrayList<NotificationPref>();
        }
    }

    public Optional<NotificationPref> getNotificationPref(String type) {
        return this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
    }

    public boolean shouldNotify(String type) {
        Optional<NotificationPref> onp =
                this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
        return onp.map(np -> np.isEmailEnabled() || np.isSmsEnabled() || np.isPushEnabled())
                .orElse(false);
    }

    public void disableNotificationTypeChannel(String type, String channel) {
        Optional<NotificationPref> onp =
                this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
        onp.ifPresent(np -> {
            switch (channel) {
                case "EMAIL":
                    np.setEmailEnabled(false);
                    break;
                case "SMS":
                    np.setSmsEnabled(false);
                    break;
                case "PUSH":
                    np.setPushEnabled(false);
                    break;
            }
        });
    }

    public void disableNotificationType(String type) {
        Optional<NotificationPref> onp =
                this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
        onp.ifPresent(np -> {
                            np.setEmailEnabled(false); np.setSmsEnabled(false); np.setPushEnabled(false);
        });
    }

    public void enableNotificationTypeChannel(String type, String channel) {
        Optional<NotificationPref> onp =
                this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
        onp.ifPresent(np -> {
            switch (channel) {
                case "EMAIL":
                    np.setEmailEnabled(true);
                    break;
                case "SMS":
                    np.setSmsEnabled(true);
                    break;
                case "PUSH":
                    np.setPushEnabled(true);
                    break;
            }
        });
    }

    public void enableNotificationType(String type) {
        Optional<NotificationPref> onp =
                this.collection.stream().filter(n -> n.getNotificationType().equals(type)).findAny();
        onp.ifPresent(np -> {
            np.setEmailEnabled(true); np.setSmsEnabled(true); np.setPushEnabled(true);
        });
    }
}