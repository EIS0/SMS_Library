package com.eis0.smslibrary;

public interface Message {
    /**
     * returns the data contained in the message
     */
    public String getData();

    /**
     * returns the Peer of the message
     */
    public SMSPeer getPeer();
}
