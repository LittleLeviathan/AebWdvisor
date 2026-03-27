package edu.advising.state;

import edu.advising.commands.WaitlistEntry;
import edu.advising.core.DatabaseManager;

public class WaitlistContext {

    private WaitlistState state;

    WaitlistEntry entry;

    private WaitlistContext(WaitlistEntry entry){
        this.entry = entry;
        this.state = StateFactory.waitlistStateFor(entry.getStatus());
    }

    private static WaitlistContext load(int id){
        entry = DatabaseManager.getInstance().fetchOne(WaitlistEntry.class,"id",id);
        retrurn new WaitlistContext(entry);
    }

    private void persist(){
        DatabaseManager.getInstance().upsert(entry);
    }

    public void setState(WaitlistState state) {
        this.state = state;
        entry.setStatus(state.getName);
    }

    public void offer(){
        state.offer();
    }
    public void accept(){
        state.accept();
    }
    public void decline(){
        state.decline();
    }
    public void remove(){
        state.remove();
    }
    public void expire(){
        state.expire();
    }
    public void isActivelyWaiting(){
        state.isActivelyWaiting();
    }


}
