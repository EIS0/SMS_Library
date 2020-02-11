package com.eis0.kademlianetwork.activitystatus;

import androidx.annotation.NonNull;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlianetwork.informationdeliverymanager.FindIdRequest;

import java.util.TimerTask;

/**
 * Timer class used to wait for a find id resourceRequest
 *
 * @author Marco Cognolato
 */
public class FindIdTimer extends TimerTask {

    private static final int ONE_SECOND = 1000;
    private static final int NUMBER_OF_SECONDS = KademliaId.ID_LENGTH;

    private int currentSecond = 0;
    private FindIdRequest request;

    public FindIdTimer(@NonNull FindIdRequest request){
        this.request = request;
    }

    @Override
    public void run() {
        completeTask();
    }

    /**
     * Waits for at most {@link #NUMBER_OF_SECONDS} seconds, or for the FindIdRequest to finish
     */
    private void completeTask() {
        try {
            //assuming it takes 10 secs to complete the task
            while (currentSecond < NUMBER_OF_SECONDS && !request.isCompleted()) {
                Thread.sleep(ONE_SECOND);
                currentSecond++;
            }
        } catch (InterruptedException e) {
        }
    }
}