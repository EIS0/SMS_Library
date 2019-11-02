package com.eis0.sms_library;

public interface SMSListener {
    void onReceiveSMS(String from, String message);
}
