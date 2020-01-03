package com.eis0.kademlianetwork.ActivityStatus;

import java.util.TimerTask;


/**
 * Define a 2 hours timer
 *
 * @author Edoardo Raimondi
 */
public class RefreshTimer extends TimerTask {

     private static final long time = 72000000;


    @Override
    public void run() {
        completeTask();
    }

    /**
     * Define the 2 hours interval
     *
     * @throws InterruptedException when the thread is interrupted during the execution
     */
    private void completeTask() {
        try {
            //assuming it takes 10 secs to complete the task
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }
}
