package com.eis0.smslibrary;

/**
 * Generic interface to implement to create a new listener for the Message Received event.
 * @param <T> The type of message to receive
 */
public interface ReceivedMessageListener<T extends Message> {

    /**
     * Called by SMSHandler whenever a message is received.
     * @param message The message received
     */
    void onMessageReceived(T message);
}