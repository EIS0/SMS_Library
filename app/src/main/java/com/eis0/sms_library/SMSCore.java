package com.eis0.sms_library;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSCore extends BroadcastReceiver {

    //Singleton Design Pattern
    private SMSCore() { }
    private static SMSCore instance = null;
    public static SMSCore getInstance(){
        if(instance == null){
            instance = new SMSCore();
        }
        return instance;
    }

    private SmsManager manager = SmsManager.getDefault();
    private final String LOG_KEY = "SMS_CORE";

    /**
     * Sends a message (SMS) to the specified target, with sent and delivery confirmation.
     * @param message Message to send to the destination Peer.
     * @param sent PendingIntent to activate when the message is sent.
     * @param delivered PendingIntent to activate when the message is delivered.
     */
    protected void sendMessage(Message message, PendingIntent sent, PendingIntent delivered) {
        String destination = message.getPeer().getDestination();
        String textMessage = message.getMessage();
        manager.sendTextMessage(destination,null, textMessage, sent, delivered);
        Log.i(LOG_KEY, "Message \"" + message + "\" sent to \"" + destination + "\"");
    }

    /**
     * Function called when a message is received. It delegates the message to the SMS Message
     * Handler which analyzes its content.
     * @param context Received message context.
     * @param intent Received message Intent.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[])intent.getExtras().get("pdus");
        SmsMessage shortMessage = SmsMessage.createFromPdu((byte[])pdus[0]);
        Log.i(LOG_KEY, "Message received");
        SMSHandler.getInstance().handleMessage(shortMessage);
    }
}
