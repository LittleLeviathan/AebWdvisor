package edu.advising.state;

public class ExpiredWaitlistSate implements WaitlistState {

    private static final ExpiredWaitlistSate INSTANCE = new ExpiredWaitlistSate();

    private ExpiredWaitlistSate(){

    }

    public static ExpiredWaitlistSate getInstance(){
        return INSTANCE;
    }
    public String getName(){

        return "EXPIRED";
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
