package com.eis0.kademlianetwork.activitystatus;

import java.util.TimerTask;


/**
 * Defines a 2 hours timer
 *
 * @author Edoardo Raimondi
 * @author edits by Giovanni Velludo
 * @author Marco Cognolato
 */

public class RefreshTimer extends TimerTask {

    private static final int ONE_SECOND = 1000;
    private static final int ONE_MINUTE = 60 * ONE_SECOND;
    private static final int ONE_HOUR = 60 * ONE_MINUTE;
    private static final long TIME = 2 * ONE_HOUR;

    @Override
    public void run() {
        completeTask();
    }

    /**
     * Defines the 2 hours interval
     */
    private void completeTask() {
        try {
            //assuming it takes 2hours to complete the task
            Thread.sleep(TIME);
        } catch (InterruptedException e) {
        }
    }
}
