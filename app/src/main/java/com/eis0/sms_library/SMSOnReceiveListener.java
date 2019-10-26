package com.eis0.sms_library;

public interface SMSOnReceiveListener {

    /**
     * You should implement this method, then pass it to SMSCore.addOnReceiveListener
     * to create a callback method which gets called when a mesasge is received
     *
     * @param from phone number of the user who sent the text message
     * @param message text messaged inside the SMS
     */
    void SMSOnReceive(String from, String message);
}