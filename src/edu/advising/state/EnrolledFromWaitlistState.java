package edu.advising.state;

import edu.advising.commands.CommandExecutor;

public class EnrolledFromWaitlistState implements WaitlistState {

    private static final EnrolledFromWaitlistState INSTANCE = new EnrolledFromWaitlistState();

    private EnrolledFromWaitlistState(){

    }

    public static EnrolledFromWaitlistState getInstance(){
        return INSTANCE;
    }
    public String getName(){

        return "ENROLLED";
    }

    @Override
    public void offer(WaitlistContext context) {
        System.out.println("ERROR: Cannot Offer seat, already enrolled for this section.");
    }
    public void offer(WaitlistContext context, long expiryHours) {
        System.out.println("ERROR: Cannot Offer seat, already enrolled for this section.");
    }

    @Override
    public void accept(WaitlistContext context) {
        System.out.println("ERROR: Cannot accept seat offer, already enrolled for this section.");
    }

    @Override
    public void accept(WaitlistContext context, CommandExecutor executor) {}

    @Override
    public void decline(WaitlistContext context) {
        System.out.println("ERROR: Cannot decline seat offer, already enrolled for this section.");
    }

    @Override
    public void remove(WaitlistContext context, String reason) {
        System.out.println("ERROR: Cannot remove, already enrolled for this section.");
    }

    @Override
    public void expire(WaitlistContext context) {
        System.out.println("ERROR: Cannot expire, already enrolled for this section.");
    }

    @Override
    public boolean isActivelyWaiting() {
        return false;
    }
}
