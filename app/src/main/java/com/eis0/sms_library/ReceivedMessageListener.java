package com.eis0.sms_library;

public interface ReceivedMessageListener {

    /**
     * Called by SMSHandler whenever a message is received.
     * @param message the message received
     */
    void onMessageReceived(Message message);
}