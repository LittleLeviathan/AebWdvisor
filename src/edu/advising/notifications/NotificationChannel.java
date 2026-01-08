package edu.advising.notifications;

import edu.advising.users.User;

/**
 * NotificationChannel - Different delivery methods (Strategy-like)
 */
public interface NotificationChannel {
    void send(Notification notification, User user);
}
