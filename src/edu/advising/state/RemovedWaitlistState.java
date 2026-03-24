package edu.advising.state;

public class RemovedWaitlistState implements WaitlistState {

    private static final RemovedWaitlistState INSTANCE = new RemovedWaitlistState();

    private RemovedWaitlistState(){

    }

    public static RemovedWaitlistState getInstance(){
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
