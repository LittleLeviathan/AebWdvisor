package edu.advising.state;

import java.sql.SQLException;

public interface WaitlistState {

    void offer(WaitlistContext context) throws SQLException;
    void offer(WaitlistContext context, long expiryHours) throws SQLException;
    void accept (WaitlistContext context);
    void decline (WaitlistContext context);
    void remove (WaitlistContext context, String reason);
    void expire (WaitlistContext context);
    boolean isActivelyWaiting();
    String getName();
}
