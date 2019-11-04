package com.eis0.smslibrary;

public interface Message {
    /**
     * returns the data contained in the message
     */
    String getData();

    /**
     * returns the Peer of the message
     */
    Peer getPeer();
}
