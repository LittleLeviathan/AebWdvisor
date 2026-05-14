package edu.advising.state;

import edu.advising.commands.CommandExecutor;

public class RemovedWaitlistState implements WaitlistState {

    private static final RemovedWaitlistState INSTANCE = new RemovedWaitlistState();

    private RemovedWaitlistState(){

    }

    public static RemovedWaitlistState getInstance(){
        return INSTANCE;
    }

    public String getName(){
        return "REMOVED";
    }

    @Override
    public void offer(WaitlistContext context) {
        System.out.println("ERROR: Cannot offer seat, already removed from waitlist.");
    }
    public void offer(WaitlistContext context, long expiryHours) {
        System.out.println("ERROR: Cannot offer seat, already removed from waitlist.");
    }

    @Override
    public void accept(WaitlistContext context) {
        System.out.println("ERROR: Cannot accept seat offer, already removed from waitlist.");
    }

    @Override
    public void accept(WaitlistContext context, CommandExecutor executor) {}

    @Override
    public void decline(WaitlistContext context) {
        System.out.println("ERROR: Cannot decline seat offer, already removed from waitlist.");
    }

    @Override
    public void remove(WaitlistContext context, String reason) {
        System.out.println("ERROR: Cannot remove from waitlist, already removed from waitlist.");
    }

    @Override
    public void expire(WaitlistContext context) {
        System.out.println("ERROR: Cannot expire, already removed from waitlist.");
    }

    @Override
    public boolean isActivelyWaiting() {
        return false;
    }
}
