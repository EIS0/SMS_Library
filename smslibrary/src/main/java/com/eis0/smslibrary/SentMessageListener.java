package com.eis0.smslibrary;

/**
 * Interface to implement to create a listener for a Message Sent event
 * @author Marco Cognolato
 */
public interface SentMessageListener {
    /**
     * Called when a message is sent
     * @param resultCode Result code of the operation (valid or not)
     * @param message The message linked to the operation
     */
    void onMessageSent(int resultCode, SMSMessage message);
}
