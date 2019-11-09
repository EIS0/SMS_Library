package com.eis0.smslibrary;

/**
 * Interface to implement to create a listener for a Message Delivered event
 * @author Marco Cognolato
 */
public interface DeliveredMessageListener {
    /**
     * Function called when a message for the library is delivered
     * @param resultCode Result code of the operation (delivered or not)
     * @param message The message linked to the operation
     */
    void onMessageDelivered(int resultCode, SMSMessage message);
}
