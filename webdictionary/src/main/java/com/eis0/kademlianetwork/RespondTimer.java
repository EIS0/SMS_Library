package com.eis0.kademlianetwork;

import android.util.Log;

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
    private static final String LOG_KEY = "TIMER";

    @Override
    public void run() {
        Log.i(LOG_KEY, ": started");
        completeTask();
        Log.i(LOG_KEY, ": stopped");
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