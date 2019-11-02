package com.eis0.sms_library;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;

@TargetApi(21)
public class SMSHandler extends NotificationListenerService implements CommunicationHandler {

    private final char APP_ID = (char)0x02;
    private final String LOG_KEY = "SMS_HANDLER";
    private ArrayList<SmsMessage> pendingMessages = new ArrayList<>();

    //listeners related variables
    private ReceivedMessageListener smsReceivedListener;
    private SentMessageListener smsSentListener;
    private BroadcastReceiver onSend = null;
    private PendingIntent sent;
    private DeliveredMessageListener smsDeliveredListener;
    private BroadcastReceiver onDeliver = null;
    private PendingIntent delivered;


    //Singleton Design Pattern
    private SMSHandler() { }
    private static SMSHandler instance = null;
    public static SMSHandler getInstance(){
        if(instance == null){
            instance = new SMSHandler();
        }
        return instance;
    }

    /**
     * Sends a message (SMS) to the specified target, with sent and delivery confirmation.
     * @param message Message to send to the destination Peer.
     */
    public void sendMessage(Message message) {
        Peer destination = message.getPeer();
        if(!destination.isValid()) {
            Log.e(LOG_KEY,"Invalid destination \"" + destination + "\"");
            throw new IllegalArgumentException("Invalid destination \"" + destination + "\"");
        }
        try {
            SMSCore.getInstance().sendMessage(message, sent, delivered);
        }
        catch (Exception e) {
            Log.e(LOG_KEY, e.getMessage());
            throw e;
        }
    }

    /**
     * Sets the listener, that is the object to be called when an SMS with the APP_ID is received.
     * @param listener Must be an object that implements the ReceivedMessageListener interface.
     */
    public void addReceiveListener(ReceivedMessageListener listener) {
        smsReceivedListener = listener;
        for (SmsMessage pendingMessage : pendingMessages) handleMessage(pendingMessage);
        pendingMessages.clear();
    }

    public void removeReceiveListener(){
        smsReceivedListener = null;
    }

    public void addSentListener(SentMessageListener listener, Context context) {
        smsSentListener = listener;

        sent = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), 0);
        onSend = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                smsSentListener.onMessageSent(getResultCode());
            }
        };
        context.registerReceiver(onSend, new IntentFilter("SMS_SENT"));
    }

    public void addDeliveredListener(DeliveredMessageListener listener, Context context) {
        smsDeliveredListener = listener;

        delivered = PendingIntent.getBroadcast(context, 0, new Intent("SMS_DELIVERED"), 0);
        onDeliver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                smsDeliveredListener.onMessageDelivered(getResultCode());
            }
        };
        context.registerReceiver(onDeliver, new IntentFilter("SMS_DELIVERED"));
    }

    /**
     * Analyze the message received by SMSCore, if the APP_ID is recognized it calls the listener.
     * @param sms The object representing the short message.
     */
    protected void handleMessage(SmsMessage sms) {
        String content = sms.getDisplayMessageBody();
        if(content.charAt(0) != APP_ID) return;
        if(smsReceivedListener == null) pendingMessages.add(sms);
        else smsReceivedListener.onMessageReceived(new Message(new Peer(sms.getDisplayOriginatingAddress()), content.substring(1)));
    }

    /**
     * Create only to test an sms insert if smsListener is null
     * @return true if is empty
     */
    public boolean isPendingMessagesEmpty(){
        ArrayList<SmsMessage> test =  new ArrayList<>(pendingMessages);
        return test.isEmpty();
    }

    /**
     * Overridden method that catches the notifications. If a notification of type 'sms' is
     * recognized and it contains the APP_ID than it will be cancelled.
     * @param sbn StatusBarNotification object that contains all the notification informations.
     */
    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        if(sbn.getPackageName().equals("com.google.android.apps.messaging") && sbn.getNotification().tickerText.toString().contains(APP_ID + ""))
            cancelNotification(sbn.getKey());
    }
}
