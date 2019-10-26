package com.eis0.sms_library;

public interface SMSReceivedListener {
    /**
     * Function to implement to decide if the activity should get started
     * @param text text received in the SMS message
     * @return returns a boolean indicating if the activity should be started (true) or not (false)
     */
    boolean shouldWakeWith(String text);


    /**
     * You should implement this method, then pass it to SMSLib.addOnReceiveListener
     * to create a callback method which gets called when a mesasge is received
     *
     * @param from phone number of the user who sent the text message
     * @param message text messaged inside the SMS
     */
    void SMSOnReceive(String from, String message);
}