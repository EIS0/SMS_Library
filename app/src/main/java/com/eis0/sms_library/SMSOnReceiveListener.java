package com.eis0.sms_library;

public interface SMSOnReceiveListener {

    /**
     * Called by SMSHandler whenever an SMS is received.
     * @param from Phone number of the user who sent the text message.
     * @param message Text message of the SMS.
     */
    void SMSOnReceive(String from, String message);
}