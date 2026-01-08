package edu.advising.notifications;

import edu.advising.users.User;

class EmailChannel implements NotificationChannel {
    @Override
    public void send(Notification notification, User user) {
        // Simulate email sending
        System.out.printf("Email sent to %s: %s%n", user.getEmail(), notification.getMessage());
    }
}
