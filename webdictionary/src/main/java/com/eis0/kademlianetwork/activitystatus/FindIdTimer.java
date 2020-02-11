package com.eis0.kademlianetwork.activitystatus;

import com.eis0.kademlia.KademliaId;
import java.util.TimerTask;

/**
 * Timer class used to wait for a find id resourceRequest
 *
 * @author Marco Cognolato
 */
class FindIdTimer extends TimerTask {

    private static final int ONE_SECOND = 1000;
    private static final int NUMBER_OF_SECONDS = KademliaId.ID_LENGTH;

    private int currentSecond = 0;

    @Override
    public void run() {
        completeTask();
    }

    /**
     * Waits for at most {@link #NUMBER_OF_SECONDS} seconds.
     */
    private void completeTask() {
        try {
            //assuming it takes 10 secs to complete the task
            while (currentSecond < NUMBER_OF_SECONDS) {
                Thread.sleep(ONE_SECOND);
                currentSecond++;
            }
        } catch (InterruptedException e) {
        }
    }
}