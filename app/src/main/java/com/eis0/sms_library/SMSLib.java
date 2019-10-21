package com.eis0.sms_library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;

class SMSLib extends BroadcastReceiver {

    private ArrayList<SMSReceivedListener> listeners;

    public void addOnReceiveListener(SMSReceivedListener listener) {
        this.listeners.add(listener);
    }

    public void removeOnReceiveListener(SMSReceivedListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent){
        Log.d("RECEIVE", "Message Received");
        Object[] pdus = (Object[])intent.getExtras().get("pdus");
        SmsMessage shortMessage = SmsMessage.createFromPdu((byte[])pdus[0]);
        String text = shortMessage.getDisplayMessageBody();
        Log.d("RECEIVE", "Message Received: " + text);
        for (SMSReceivedListener listener : listeners) listener.onReceive("from", text);
        if(text.contains("ses")){
            abortBroadcast();
            Log.d("RECEIVE", "Blocked a message with text " + text);
        }
    }
}
