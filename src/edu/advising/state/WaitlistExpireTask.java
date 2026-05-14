package edu.advising.state;

import java.util.TimerTask;

public class WaitlistExpireTask extends TimerTask {

    WaitlistContext context;

    public WaitlistExpireTask(WaitlistContext context){
        this.context = context;
    }

    @Override
    public void run() {
        context.setState(ExpiredWaitlistSate.getInstance());
    }
}
