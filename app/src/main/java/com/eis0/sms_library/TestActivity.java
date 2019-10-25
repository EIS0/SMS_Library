package com.eis0.sms_library;

import android.util.Log;

public class TestActivity implements SMSReceivedListener {

    private static final String WAKE_MESSAGE = "WakeTestActivity";

    public boolean shouldWakeWith(String text) {
        return text.contains(WAKE_MESSAGE);
    }

    public void SMSOnReceive(String from, String message) {
        Log.d("SMS_RECEIVE", "Hello, World!");
    }
}
