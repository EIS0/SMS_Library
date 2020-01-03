package com.eis0.kademlianetwork.ActivityStatus;

import java.util.TimerTask;

/**
 * Class that define a 10 secs timer
 * If a send a command and I don't receive an acknowledge message
 * in that time, then the receiver is considered broken.
 *
 * @author Edoardo Raimondi
 */

public class RespondTimer extends TimerTask {

    private static final long time  = 10000;

    @Override
    public void run() {
        completeTask();
    }

    /**
     * Define the 10 seconds interval
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