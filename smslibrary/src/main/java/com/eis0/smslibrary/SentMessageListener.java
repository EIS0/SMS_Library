package com.eis0.smslibrary;

public interface SentMessageListener {
    /**
     * Called when a message is sent
     * @param resultCode result code of the operation (valid or not)
     * @param message the message linked to the operation
     */
    void onMessageSent(int resultCode, SMSMessage message);
}
