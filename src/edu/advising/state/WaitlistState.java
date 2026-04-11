package edu.advising.state;

public interface WaitlistState {

    void offer(WaitlistContext context);
    void offer(WaitlistContext context, long expiryHours);
    void accept (WaitlistContext context);
    void decline (WaitlistContext context);
    void remove (WaitlistContext context, String reason);
    void expire (WaitlistContext context);
    boolean isActivelyWaiting();
    String getName();
}
