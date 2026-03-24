package edu.advising.state;

public interface WaitlistState {

    void offer(WaitlistContext context);
    void accept (WaitlistContext context);
    void decline (WaitlistContext context);
    void remove (WaitlistContext context);
    void expire (WaitlistContext context);
    boolean isActivelyWaiting();
}
