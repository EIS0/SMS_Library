package com.eis0.sms_library;

public interface SMSReceivedListener {
    void onReceive(String from, String msg);
}
