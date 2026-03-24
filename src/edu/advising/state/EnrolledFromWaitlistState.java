package edu.advising.state;

public class EnrolledFromWaitlistState implements WaitlistState {

    private static final EnrolledFromWaitlistState INSTANCE = new EnrolledFromWaitlistState();

    private EnrolledFromWaitlistState(){

    }

    public static EnrolledFromWaitlistState getInstance(){
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
        return false;
    }
}
