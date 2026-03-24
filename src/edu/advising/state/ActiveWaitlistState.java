package edu.advising.state;

public class ActiveWaitlistState implements WaitlistState {

    private static final ActiveWaitlistState INSTANCE = new ActiveWaitlistState();

    private ActiveWaitlistState(){

    }

    public static ActiveWaitlistState getInstance(){
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
