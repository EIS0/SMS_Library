package com.eis0.smslibrary;

public abstract class CommunicationHandler<T extends  Message> {
    /**
     * sends a valid message to a valid Peer
     * @param message the message to send
     */
    public abstract void sendMessage(T message);

    /**
     * adds a listener that gets called when a message for the library is received
     * @param listener the listener to wake up when a message is received
     */
    public abstract void addReceiveListener(ReceivedMessageListener<T> listener);

    /**
     * removes the listener waiting for incoming messages
     */
    public abstract void removeReceiveListener();
}
