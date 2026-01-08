package edu.advising.notifications;

/**
 * Subject - Interface for objects that send notifications
 */
public interface Subject {
    void attach(Observer observer);

    void detach(Observer observer);

    void notifyObservers(Notification notification);
}
