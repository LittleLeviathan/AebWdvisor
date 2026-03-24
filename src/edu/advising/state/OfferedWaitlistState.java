package edu.advising.state;

public class OfferedWaitlistState implements WaitlistState {

    private static final OfferedWaitlistState INSTANCE = new OfferedWaitlistState();

    private OfferedWaitlistState(){

    }

    public static OfferedWaitlistState getInstance(){
        return INSTANCE;
    }

    @Override
    public void offer(WaitlistContext context) {

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
