package com.eis0.sms_library;

public interface SMSReceivedListener {
    void SMSOnReceive(String from, String message);
}