package edu.advising.state;

import edu.advising.commands.WaitlistEntry;
import edu.advising.core.DatabaseManager;
import edu.advising.notifications.NotificationManager;
import edu.advising.users.Student;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class WaitlistContext {

    private WaitlistState state;

    WaitlistEntry entry;
    Student student;

    private WaitlistContext(WaitlistEntry entry){
        this.entry = entry;
        this.state = StateFactory.waitlistStateFor(entry.getStatus());
        try {
            this.student = entry.getStudent();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static WaitlistContext load(int id){
        WaitlistEntry entry = null;
        try {
            entry = DatabaseManager.getInstance().fetchOne(WaitlistEntry.class,"id",id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new WaitlistContext(entry);
    }

    public static WaitlistEntry create(int studentId, int sectionId, int position){
        WaitlistEntry entry = new WaitlistEntry(studentId, sectionId, position);
        entry.setStatus("ACTIVE");
        return entry;
    }

    private void persist(){
        try {
            DatabaseManager.getInstance().upsert(entry);
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRemovedDate(LocalDateTime removedDate){entry.setRemovedDate(removedDate);}

    public void setState(WaitlistState state) {
        this.state = state;
        entry.setStatus(state.getName());
    }

    public void offer(){
        state.offer(this);
    }
    public void offer(long expiryHours){
        state.offer(this, expiryHours);
    }
    public void accept(){

        state.accept(this);
    }
    public void decline(){

        state.decline(this);
    }
    public void remove(String reason){
        state.remove(this, reason);
    }
    public void expire(){
        state.expire(this);
    }
    public boolean isActivelyWaiting(){return state.isActivelyWaiting();}
}
