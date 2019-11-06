package com.eis0.smslibrary;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSCore extends BroadcastReceiver {

    private static SmsManager manager = SmsManager.getDefault();
    private static final String LOG_KEY = "SMS_CORE";

    /**
     * Sends a message (SMS) to the specified target, with sent and delivery confirmation.
     * @param message SMSMessage to send to the destination SMSPeer.
     * @param sent PendingIntent to activate when the message is sent.
     * @param delivered PendingIntent to activate when the message is delivered.
     */
    protected static void sendMessage(SMSMessage message, PendingIntent sent, PendingIntent delivered) {
        String destination = message.getPeer().getAddress();
        String textMessage = message.getData();
        manager.sendTextMessage(destination,null, textMessage, sent, delivered);
        Log.i(LOG_KEY, "SMSMessage \"" + message + "\" sent to \"" + destination + "\"");
    }

    /**
     * Function called when a message is received. It delegates the message to the SMSMessage
     * Handler which analyzes its content.
     * @param context Received message context.
     * @param intent Received message Intent.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[])intent.getExtras().get("pdus");
        SmsMessage shortMessage = SmsMessage.createFromPdu((byte[])pdus[0]);
        Log.i(LOG_KEY, "SMSMessage received");
        SMSHandler.handleMessage(shortMessage);
    }
}
