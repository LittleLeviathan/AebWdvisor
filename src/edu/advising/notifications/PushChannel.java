package edu.advising.notifications;

import edu.advising.users.User;

class PushChannel implements NotificationChannel {
    @Override
    public void send(Notification notification, User user) {
        // Simulate push notification
        System.out.printf("Push notification: %s%n",
                notification.getMessage());
    }
}
