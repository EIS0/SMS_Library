package com.eis0.smslibrary;

/**
 * Interface to implement to create a new Message type
 * @author Marco Cognolato
 */
public interface Message {
    /**
     * Returns the data contained in the message
     */
    String getData();

    /**
     * Returns the Peer of the message
     */
    Peer getPeer();
}
