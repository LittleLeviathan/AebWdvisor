package edu.advising.notifications;

import edu.advising.users.Faculty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Enhanced Faculty with Observer implementation
 */
class ObservableFaculty extends Faculty implements Observer {
    private List<Notification> notifications;
    private NotificationPreferences preferences;

    /*
     * Internal constructor allowing internal objects to set id during factory method copy.
     */
    private ObservableFaculty(int id, String username, String password, String email,
                             String firstName, String lastName, String employeeId, String department) {
        super(username, password, email, firstName, lastName, employeeId, department);
        this.setId(id);
        this.notifications = new ArrayList<>();
        this.preferences = new NotificationPreferences(this.getId());
    }

    public ObservableFaculty(String username, String password, String email,
                             String firstName, String lastName, String employeeId, String department) {
        super(username, password, email, firstName, lastName, employeeId, department);
        this.notifications = new ArrayList<>();
        this.preferences = new NotificationPreferences(this.getId());
    }

    /**
     * Factory Method to convert/copy Super student-Type Faculty into an ObservableFaculty.
     * @param superObj is the Super-Type Faculty that ObservableFaculty extends, and we want to convert.
     * @return ObservableFaculty with same fields as superObj but extended like the Sub-Type.
     */
    public static ObservableFaculty fromSuperType(Faculty superObj) {
        return new ObservableFaculty(superObj.getId(), superObj.getUsername(), superObj.getPassword(), superObj.getEmail(),
                superObj.getFirstName(), superObj.getLastName(), superObj.getEmployeeId(), superObj.getDepartment());
    }

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

    public NotificationPreferences getPreferences() {
        return preferences;
    }
}
