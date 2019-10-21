package com.eis0.sms_library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ReceiveMessage extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        /*
        DEPRECATA: controlla SMSLib.java e SMSReceivedListener.java

        Log.d("RECEIVE", "Message Received");
        Object[] pdus = (Object[])intent.getExtras().get("pdus");
        SmsMessage shortMessage = SmsMessage.createFromPdu((byte[])pdus[0]);
        String text = shortMessage.getDisplayMessageBody();
        Log.d("RECEIVE", "Message Received: " + text);
        if(text.contains("ses")){
            abortBroadcast();
            Log.d("RECEIVE", "Blocked a message with text " + text);
        }
         */
    }
}
