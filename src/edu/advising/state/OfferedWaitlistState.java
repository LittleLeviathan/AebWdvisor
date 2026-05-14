package edu.advising.state;

import edu.advising.commands.CommandExecutor;
import edu.advising.commands.RegisterCommand;
import edu.advising.commands.Section;
import edu.advising.commands.WaitlistEntry;
import edu.advising.notifications.ObservableStudent;

import java.sql.SQLException;

public class OfferedWaitlistState implements WaitlistState {

    private static final OfferedWaitlistState INSTANCE = new OfferedWaitlistState();

    private OfferedWaitlistState(){

    }

    public static OfferedWaitlistState getInstance(){
        return INSTANCE;
    }

    public String getName(){
        return "OFFERED";
    }

    @Override
    public void offer(WaitlistContext context) {
        System.out.println("ERROR: Cannot offer, seat has already been offered.");
    }
    public void offer(WaitlistContext context, long expiryHours) {
        System.out.println("ERROR: Cannot offer, seat has already been offered.");
    }

    @Override
    public void accept(WaitlistContext context) {}

    @Override
    public void accept(WaitlistContext context, CommandExecutor executor) {
        WaitlistEntry entry = context.entry;
        Section section = null;
        try {
            section = entry.getSection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableStudent student = null;
        try {
            student = (ObservableStudent) entry.getStudent();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        synchronized (this){
            if (section.hasCapacity()) {
                executor.execute(new RegisterCommand(student, section));
                context.setState(EnrolledFromWaitlistState.getInstance());
                System.out.println("Successfully Enrolled from waitlist.");
            }
            else {System.out.println("ERROR: Cannot enroll from waitlist. Section is at capacity.");}
        }

    }

    @Override
    public void decline(WaitlistContext context) {
        context.setState(RemovedWaitlistState.getInstance());
        System.out.println("Student has declined offer.");
    }

    @Override
    public void remove(WaitlistContext context, String reason) {
        context.setState(RemovedWaitlistState.getInstance());
        System.out.println("Successfully removed from waitlist. REASON: " + reason);
    }

    @Override
    public void expire(WaitlistContext context) {
        context.setState(ExpiredWaitlistSate.getInstance());
        System.out.println("Offer window has expired.");
    }

    @Override
    public boolean isActivelyWaiting() {
        return true;
    }
}
