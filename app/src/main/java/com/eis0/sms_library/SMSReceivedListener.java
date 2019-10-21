package com.eis0.sms_library;

interface SMSReceivedListener {
    void onReceive(String from, String msg);
}
