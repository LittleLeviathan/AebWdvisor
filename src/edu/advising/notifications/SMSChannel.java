package edu.advising.notifications;

import edu.advising.users.User;

class SMSChannel implements NotificationChannel {
    @Override
    public void send(Notification notification, User user) {
        // Simulate SMS sending
        System.out.printf("SMS sent: %s%n", notification.getMessage());
    }
}
