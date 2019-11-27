package com.eis0.smslibrary;

/**
 * Interface to implement to create a new Message type.
 *
 * @author Marco Cognolato
 */
public interface Message {

    /**
     * Returns the data contained in the message.
     *
     * @return A String containing data of the message.
     * @author Marco Cognolato
     */
    String getData();

    /**
     * Returns the Peer of the message.
     *
     * @return A Peer object containing all the infos about the peer.
     * @author Marco Cognolato
     */
    Peer getPeer();
}
