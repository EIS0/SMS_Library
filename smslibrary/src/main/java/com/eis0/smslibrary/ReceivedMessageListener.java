package com.eis0.smslibrary;

public interface ReceivedMessageListener<T extends Message> {

    /**
     * Called by SMSHandler whenever a message is received.
     * @param message the message received
     */
    void onMessageReceived(T message);
}