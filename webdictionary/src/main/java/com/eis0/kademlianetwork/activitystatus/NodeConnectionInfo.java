package com.eis0.kademlianetwork.activitystatus;

/**
 * Every node has some connection information.
 * It has to check if another node reply or pong him, in order to consider it alive or less.
 * Then it has to coherently modify the params.
 *
 * @author Edoardo Raimondi
 */

public class NodeConnectionInfo {

    private static final int ONE_SECOND = 1000;
    private static final int NUMBER_OF_SECONDS = 10;
    private int currentSecond = 0;

    //To know if I had a positive acknowledge respond to a sent command
    private boolean hasRespond;
    //To know if I had a pong for the refreshing task
    private boolean hasPong;


    public NodeConnectionInfo() {
        //At the beginning I will not have responds or pongs
        reset();
    }

    /**
     * @return true if I had an acknowledge respond to a sent command, false otherwise
     * @author Edoardo Raimondi
     */
    public boolean hasRespond() {
        return hasRespond;
    }

    /**
     * Set the respond state
     *
     * @param value of the respond state
     * @author Edoardo Raimondi
     */
    public void setRespond(boolean value) {
        hasRespond = value;
    }

    /**
     * @return true if I had a pong respond
     * @author Edoardo Raimondi
     */
    public boolean hasPong() {
        return hasPong;
    }

    /**
     * Set the pong state
     *
     * @author Edoardo Raimondi
     */
    public void setPong(boolean value) {
        hasPong = value;
    }

    /**
     * Reset all the variables to false
     */
    public void reset() {
        hasRespond = false;
        hasPong = false;
    }

    /**
     * Waits for 10s or until the SMSPeer has responded
     *
     * @author Marco Cognolato
     */
    public void run() {
        try {
            //until 10s have passed or until the peer has responded or ponged, I wait
            while (currentSecond < NUMBER_OF_SECONDS && (!hasRespond || !hasPong)) {
                Thread.sleep(ONE_SECOND);
                currentSecond++;
            }
        } catch (InterruptedException e) {
        }
    }
}
