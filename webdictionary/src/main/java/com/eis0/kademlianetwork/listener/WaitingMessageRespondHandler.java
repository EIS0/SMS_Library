package com.eis0.kademlianetwork.listener;

import com.eis0.kademlia.Contact;

import java.io.IOException;

/**
 * This class provides a synchronized way to manage responds waiting.
 */
final class WaitingMessageRespondHandler implements MessageRespondhandler{
    private boolean hasFinished;
    private IOException myIoException;
    private Contact contact;

    /**
     * At the beginning I haven't responds
     *
     * @param contact The contact
     */
    WaitingMessageRespondHandler(Contact contact){
        hasFinished = false;
        this.contact = contact;
    }

    /**
     * Method to call whenever there are respond problems
     *
     * @param exception
     */
    public synchronized void onRespondError(IOException exception){
        myIoException = exception;
        finish();
    }


    /**
     * Waits a respond from someone
     *
     * @throws InterruptedException whenever the waiting thread is interrupted
     */
    public synchronized void waitForRespond() throws InterruptedException{
        while(!hasFinished){
            this.wait();
        }

        if(myIoException != null){ //the other node is unresponsive
            contact.incrementStaleCount();
        }
        this.notifyAll();
    }


    /**
     * Set hasRespond as true
     */
    private synchronized void finish(){
        hasFinished = true;
        this.notifyAll();
    }
}