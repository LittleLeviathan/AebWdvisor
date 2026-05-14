package edu.advising.state;

import edu.advising.commands.CommandExecutor;

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
        System.out.println("ERROR: Cannot offer seat, window to register has already expired.");
    }
    public void offer(WaitlistContext context, long expiryHours) {
        System.out.println("ERROR: Cannot offer seat, window to register has already expired.");
    }

    @Override
    public void accept(WaitlistContext context) {
        System.out.println("ERROR: Cannot accept seat offer, window to register has already expired.");
    }

    @Override
    public void accept(WaitlistContext context, CommandExecutor executor) {}

    @Override
    public void decline(WaitlistContext context) {
        System.out.println("ERROR: Cannot decline seat offer, window to register has already expired.");
    }

    @Override
    public void remove(WaitlistContext context, String Reason) {
        System.out.println("ERROR: Cannot remove, window to register has already expired.");
    }

    @Override
    public void expire(WaitlistContext context) {
        System.out.println("ERROR: Cannot expire, window to register has already expired.");
    }

    @Override
    public boolean isActivelyWaiting() {
        return false;
    }
}
