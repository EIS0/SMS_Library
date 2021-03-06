package com.eis0.smslibrary;

/**
 * Generic class for handling communications. Extend this to implement a new communication type.
 *
 * @param <T> For a communication to work, this must be specific for a type of message.
 * @author Marco Cognolato
 */
public abstract class CommunicationManager<T extends Message> {

    /**
     * Sends a valid message to a valid Peer.
     * @param message The message to send.
     *
     * @author Marco Cognolato
     */
    public abstract void sendMessage(T message);

    /**
     * Set a listener that gets called when a message for the library is received.
     *
     * @param listener The listener to wake up when a message is received.
     * @author Marco Cognolato
     */
    public abstract void setReceiveListener(ReceivedMessageListener<T> listener);

    /**
     * Removes the listener waiting for incoming messages.
     *
     * @author Marco Cognolato
     */
    public abstract void removeReceiveListener();
}
