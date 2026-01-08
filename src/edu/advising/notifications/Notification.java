package edu.advising.notifications;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Notification - Represents a notification message
 */
public class Notification {
    private int id;
    private String type;
    private String message;
    private String priority; // HIGH, MEDIUM, LOW
    private LocalDateTime timestamp;
    private int userId;
    private boolean read;
    private Map<String, String> metadata;

    public Notification(String type, String message, int userId) {
        this(type, message, userId, "MEDIUM");
    }

    public Notification(String type, String message, int userId, String priority) {
        this.type = type;
        this.message = message;
        this.userId = userId;
        this.priority = priority;
        this.timestamp = LocalDateTime.now();
        this.read = false;
        this.metadata = new HashMap<>();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getPriority() {
        return priority;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isRead() {
        return read;
    }

    public void markAsRead() {
        this.read = true;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void addMetadata(String key, String value) {
        metadata.put(key, value);
    }

    @Override
    public String toString() {
        String icon = getIconForType();
        return String.format("[%s] %s - %s (Priority: %s)",
                icon, type, message, priority);
    }

    private String getIconForType() {
        switch (type) {
            case "GRADE_CHANGE":
                return "📝";
            case "REGISTRATION":
                return "📚";
            case "PAYMENT":
                return "💳";
            case "FINANCIAL_AID":
                return "💰";
            case "DOCUMENT":
                return "📄";
            case "RESTRICTION":
                return "⚠️";
            case "WAITLIST":
                return "⏳";
            default:
                return "🔔";
        }
    }
}
