package edu.advising.notifications;

import edu.advising.users.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Enhanced Student with Observer implementation
 *
 * ORM PERSISTENCE NOTE:
 *   ObservableStudent intentionally carries NO @Table annotation.  It is a
 *   runtime behavioural wrapper, not a DB entity.  Use toSubType() whenever
 *   you need to persist this object via DatabaseManager.upsert().
 *
 *   toSubType():     creates a plain Student whose getClass() == Student.class
 *   fromSuperType(): promotes a plain Student fetched from the DB into a fully initialised ObservableStudent
 *                    copying ALL fields.
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
     * Factory method: creates a plain Student just fetched from the DB via
     * DatabaseManager.fetchOne(Student.class, ...)) into an ObservableStudent.
     *
     * NOTE: I copy every field explicitly as I realized things were missing:
     *   The private constructor only accepts the subset of fields needed to
     *   reconstruct the identity of the object, but ALL mutable fields in
     *   the Student and User super classes must be transferred so that
     *   object memory reads are consistent with what is in the database. If I
     *   omitted a field here, that field would silently return null after conversion.
     */
    public static ObservableStudent fromSuperType(Student s) {
        // Use the private constructor to set the primary key
        ObservableStudent obs = new ObservableStudent(
                s.getId(),
                s.getUsername(),
                s.getPassword(),
                s.getEmail(),
                s.getFirstName(),
                s.getLastName(),
                s.getStudentId()
        );

        // Set User class fields
        obs.userType  = s.getUserType();
        obs.isActive  = s.isActive();  // Had to add getter to User to allow this.
        obs.phone     = s.getPhone();
        obs.lastLogin = s.getLastLogin();  // Had to add getter and change access modifier to allow this.

        // Set Student class fields
        obs.gpa              = s.getGpa();
        obs.enrollmentStatus = s.getEnrollmentStatus();
        obs.academicStanding = s.getAcademicStanding();
        obs.classification   = s.getClassification();
        obs.major            = s.getMajor();
        obs.minor            = s.getMinor();
        obs.advisorId        = s.getAdvisorId();

        return obs;
    }

    /**
     * Creates a plain Student instance populated with all fields from this ObservableStudent.
     *
     * WHY?
     *   Java's getClass() always returns the true underlying class type regardless of type cast.
     *   DatabaseManager.upsertAll() calls items.get(0).getClass() to call getTableHierarchy().
     *   If that returns ObservableStudent.class, which is intentionally not annotated, the
     *   hierarchy is empty and nothing is stored to the database. This method allows the ORM to
     *   receive a genuine Student object so getClass() is Student.class and the hierarchy is
     *   resolved correctly.
     *
     * USAGE:
     *   dbManager.upsert(student.toSubType());
     */
    public Student toSubType() {
        Student s = new Student(
                this.username,
                this.password,
                this.email,
                this.firstName,
                this.lastName,
                this.studentId
        );

        // Set User class fields
        s.setId(this.id);
        s.setUserType(this.userType); // Had to add setter to User to do this
        s.setActive(this.isActive); // Had to add setter to User to do this
        s.setPhone(this.phone);
        s.setLastLogin(this.lastLogin);  // Had to add setter to User to do this

        // Set Student class fields
        s.setGpa(this.gpa);
        s.setEnrollmentStatus(this.enrollmentStatus);
        s.setAcademicStanding(this.academicStanding);
        s.setClassification(this.classification);
        s.setMajor(this.major);
        s.setMinor(this.minor);
        s.setAdvisorId(this.advisorId);

        return s;
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
