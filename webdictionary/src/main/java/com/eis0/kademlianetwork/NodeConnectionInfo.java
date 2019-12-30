package com.eis0.kademlianetwork;

/**
 * Every node has some connection information.
 * It has to check if another node reply or pong him, in order to consider it alive or less.
 * Then It has to coherently modify the params.
 *
 * @author Edoardo Raimondi
 */
public class NodeConnectionInfo {

    //To know if I had a positive acknowledge respond to a sent command
    private boolean hasRespond;
    //To know if I had a pong for the refreshing task
    private boolean hasPong;


    public NodeConnectionInfo(){
        //At the beginning I will not have responds or pongs
        hasRespond = false;
        hasPong = false;
    }

    /**
     * @return true if I had an acknowledge respond to a sent command, false otherwise
     * @author Edoardo Raimondi
     */
    public boolean hasRespond() { return hasRespond; }

    /**
     * Set the respond state
     *
     * @param  value of the respond state
     * @author Edoardo Raimondi
     */
    public void setRespond(boolean value){ hasRespond = value; }

    /**
     * @return true if I had a pong respond
     * @author Edoardo Raimondi
     */
    public boolean getPongKnown(){ return hasPong; }

    /**
     * Set the pong state
     * @author Edoardo Raimondi
     */
    public void setPong(boolean value) { hasPong = value; }




}
