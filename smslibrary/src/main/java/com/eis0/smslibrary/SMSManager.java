package com.eis0.smslibrary;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class SMSManager extends CommunicationHandler<SMSMessage> {
    //Singleton Design Pattern
    private SMSManager() { }
    private static SMSManager instance = null;
    private static Context context;
    public static SMSManager getInstance(Context context){
        if(instance == null){
            instance = new SMSManager();
        }
        SMSManager.context = context;
        return instance;
    }

    private static SentMessageListener smsSentListener;
    private static BroadcastReceiver onSend = null;
    private static DeliveredMessageListener smsDeliveredListener;
    private static BroadcastReceiver onDeliver = null;
    private static PendingIntent sent;
    private static PendingIntent delivered;

    public void addReceiveListener(ReceivedMessageListener<SMSMessage> listener){
        SMSHandler.addReceiveListener(listener);
    }

    public void removeReceiveListener(){
        SMSHandler.removeReceiveListener();
    }

    public void sendMessage(SMSMessage message){
        SMSHandler.sendMessage(message, sent, delivered);
    }

    public void sendMessage(final SMSMessage message, SentMessageListener listener) {
        smsSentListener = listener;

        sent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), 0);
        onSend = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                smsSentListener.onMessageSent(getResultCode(), message);
            }
        };
        context.registerReceiver(onSend, new IntentFilter("SMS_SENT"));
    }


    public void sendMessage(final SMSMessage message, DeliveredMessageListener listener) {
        smsDeliveredListener = listener;

        delivered = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED"), 0);
        onDeliver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                smsDeliveredListener.onMessageDelivered(getResultCode(), message);
            }
        };
        context.registerReceiver(onDeliver, new IntentFilter("SMS_DELIVERED"));
    }
}
