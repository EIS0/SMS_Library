package com.eis0.sms_library;


import android.util.Log;

public class TestReceiveMethod implements SMSReceivedListener {

    @Override
    public void onReceive(String from, String msg){
        Log.d("UNITTEST", "Received a message from " + from);
        Log.d("UNITTEST", "Received a message saying " + msg);
    }
}
