package edu.advising.notifications;

import edu.advising.users.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Enhanced Student with Observer implementation
 */
public class ObservableStudent extends Student implements Observer {
    //TODO: Should this notifications list be updated from the notifications or notifications_history table?
    private List<Notification> notifications;
    private NotificationPreferences preferences;

    /*
     * Internal constructor allowing internal objects to set id during factory method copy.
     */
    private ObservableStudent(int id, String username, String password, String email,
                             String firstName, String lastName, String studentId) {
        super(username, password, email, firstName, lastName, studentId);
        this.setId(id);
        this.notifications = new ArrayList<>();
        this.preferences = new NotificationPreferences(this.getId());
    }

    public ObservableStudent(String username, String password, String email,
                             String firstName, String lastName, String studentId) {
        super(username, password, email, firstName, lastName, studentId);
        this.notifications = new ArrayList<>();
        this.preferences = new NotificationPreferences(this.getId());
    }

    /**
     * Factory Method to convert/copy Super-Type Student into an ObservableStudent.
     * @param superObj is the Super-Type Student that ObservableStudent extends, and we want to convert.
     * @return ObservableStudent with same fields as superObj but extended like the Sub-Type.
     */
    public static ObservableStudent fromSuperType(Student superObj) {
        return new ObservableStudent(superObj.getId(), superObj.getUsername(), superObj.getPassword(),
                superObj.getEmail(), superObj.getFirstName(), superObj.getLastName(), superObj.getStudentId());
    }

    @Override
    public void update(Notification notification) {
        // Check preferences
        Optional<NotificationPref> oPreference = preferences.getNotificationPref(notification.getType());
        if(oPreference.isEmpty()) { return; }
        NotificationPref preference = oPreference.get();
        if (!preference.shouldNotify()) { return; }

        notifications.add(notification);

        // Display notification with priority-based formatting
        String prefix = notification.getPriority().equals("HIGH") ? "❗" : "ℹ️";
        System.out.printf("%s New notification for %s %s: %s%n",
                prefix, getFirstName(), getLastName(), notification);

        // Simulate different delivery channels based on preferences
        if (preference.isEmailEnabled()) {
            new EmailChannel().send(notification, this);
        }
        if (preference.isSmsEnabled()) {
            new SMSChannel().send(notification, this);
        }
        if (preference.isPushEnabled()) {
            new PushChannel().send(notification, this);
        }
    }

    @Override
    public int getUserId() {
        return this.getId();
    }

    public List<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public List<Notification> getUnreadNotifications() {
        return notifications.stream()
                .filter(n -> !n.isRead())
                .collect(java.util.stream.Collectors.toList());
    }

    public void viewNotifications() {
        System.out.println("\n=== MY DOCUMENTS / NOTIFICATIONS ===");
        System.out.println("Total: " + notifications.size() +
                " | Unread: " + getUnreadNotifications().size());

        if (notifications.isEmpty()) {
            System.out.println("No notifications");
            return;
        }

        for (Notification n : notifications) {
            String status = n.isRead() ? "✓" : "○";
            System.out.printf("%s %s%n", status, n);
        }
    }

    public NotificationPreferences getPreferences() {
        return preferences;
    }
}
