// Week 4: OBSERVER PATTERN
// Features Implemented: Communication - My Documents, Grade Notifications, 
//                       Financial Aid alerts, Payment confirmations
// Why Now: Need event-driven notifications across the system

package edu.advising.notifications;

/**
 * Observer - Interface for objects that want to receive notifications
 */
public interface Observer {
    void update(Notification notification);
    int getUserId();
}

