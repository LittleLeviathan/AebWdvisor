package edu.advising.state;

import edu.advising.commands.CommandExecutor;
import edu.advising.commands.Section;
import edu.advising.core.DatabaseManager;
import edu.advising.notifications.NotificationManager;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public  class ActiveWaitlistState implements WaitlistState {

    private static final ActiveWaitlistState INSTANCE = new ActiveWaitlistState();


    private ActiveWaitlistState(){

    }

    Timer timer = new Timer();
    WaitlistExpireTask task;
    public static ActiveWaitlistState getInstance(){
        return INSTANCE;
    }
    public String getName(){

        return "ACTIVE";
    }

    @Override
    public void offer(WaitlistContext context) throws SQLException {
        String course = DatabaseManager.getInstance().fetchOne(Section.class, "id", context.entry.getSectionId()).getCourseCode();
        context.setState(OfferedWaitlistState.getInstance());
        context.setRemovedDate(LocalDateTime.now().plusHours(24));
        System.out.println("Seat has been offered. Offer expires in 24 hours");
        NotificationManager.getInstance().notifyWaitlistUpdate(context.student, course, context.entry.getPosition());
        task = new WaitlistExpireTask(context);
        timer.schedule(task, TimeUnit.MILLISECONDS.convert(1L, TimeUnit.DAYS));
    }
    public void offer(WaitlistContext context, long expiryHours) throws SQLException {
        String course = DatabaseManager.getInstance().fetchOne(Section.class, "id", context.entry.getSectionId()).getCourseCode();
        context.setState(OfferedWaitlistState.getInstance());
        context.setRemovedDate(LocalDateTime.now().plusHours(expiryHours));
        System.out.println("Seat has been offered. Offer expires in " + expiryHours + " hours");
        NotificationManager.getInstance().notifyWaitlistUpdate(context.student, course, context.entry.getPosition());
        task = new WaitlistExpireTask(context);
        timer.schedule(task, TimeUnit.MILLISECONDS.convert(expiryHours, TimeUnit.HOURS));
    }

    @Override
    public void accept(WaitlistContext context) {
        System.out.println("ERROR: Cannot accept seat offer, seat has not been offered.");
    }

    @Override
    public void accept(WaitlistContext context, CommandExecutor executor) {}

    @Override
    public void decline(WaitlistContext context) {
        System.out.println("ERROR: Cannot decline seat offer, seat has not been offered.");
    }

    @Override
    public void remove(WaitlistContext context, String reason) {
        context.setState(RemovedWaitlistState.getInstance());
        System.out.println("Successfully removed from waitlist. REASON: " + reason);
    }

    @Override
    public void expire(WaitlistContext context) {
        context.setState(ExpiredWaitlistSate.getInstance());
        System.out.println("Registration period has ended.");
    }

    @Override
    public boolean isActivelyWaiting() {
        return true;
    }
}
