package com.eis0.smslibrary;

public interface DeliveredMessageListener {
    /**
     * Function called when a message for the library is delivered
     * @param resultCode result code of the operation (delivered or not)
     * @param message the message linked to the operation
     */
    void onMessageDelivered(int resultCode, SMSMessage message);
}
