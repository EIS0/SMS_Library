package com.eis0.kademlianetwork;

import android.util.Log;

import java.util.TimerTask;

/**
 * Class that define a 10 seconds timer
 * If a send a command and I don't receive an acknowledge message
 * in 10 seconds, then the command receiver is considered broken
 *
 * @author Edoardo Raimondi
 */

public class RespondTimer extends TimerTask {

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
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}