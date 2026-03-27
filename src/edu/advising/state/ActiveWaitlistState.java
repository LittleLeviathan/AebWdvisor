package edu.advising.state;

import java.time.LocalDateTime;

public class ActiveWaitlistState implements WaitlistState {

    private static final ActiveWaitlistState INSTANCE = new ActiveWaitlistState();

    private ActiveWaitlistState(){

    }

    public static ActiveWaitlistState getInstance(){
        return INSTANCE;
    }
    public String getName(){

        return "ACTIVE";
    }

    @Override
    public void offer(WaitlistContext context, LocalDateTime expiryHours) {
        INSTANCE.setState(OfferedWaitlistState.getInstance());

    }

    @Override
    public void accept(WaitlistContext context) {

    }

    @Override
    public void decline(WaitlistContext context) {

    }

    @Override
    public void remove(WaitlistContext context) {

    }

    @Override
    public void expire(WaitlistContext context) {

    }

    @Override
    public boolean isActivelyWaiting() {
        return true;
    }
}
