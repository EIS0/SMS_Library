package com.eis0.smslibrary;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;

@TargetApi(21)
public class SMSHandler extends NotificationListenerService{

    private static final char APP_ID = (char)0x02;
    private static final String LOG_KEY = "SMS_HANDLER";
    private static ArrayList<SmsMessage> pendingMessages = new ArrayList<>();

    //listeners related variables
    private static ReceivedMessageListener smsReceivedListener;



    /**
     * Sends a message (SMS) to the specified target, with sent and delivery confirmation.
     * @param message SMSMessage to send to the destination SMSPeer.
     */
    public static void sendMessage(SMSMessage message, PendingIntent sent, PendingIntent delivered) {
        SMSPeer destination = message.getPeer();
        if(!destination.isValid()) {
            Log.e(LOG_KEY,"Invalid destination \"" + destination + "\"");
            throw new IllegalArgumentException("Invalid destination \"" + destination + "\"");
        }
        try {
            SMSCore.sendMessage(message, sent, delivered);
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
    public static void addReceiveListener(ReceivedMessageListener listener) {
        smsReceivedListener = listener;
        for (SmsMessage pendingMessage : pendingMessages) handleMessage(pendingMessage);
        pendingMessages.clear();
    }

    public static void removeReceiveListener(){
        smsReceivedListener = null;
    }




    /**
     * Analyze the message received by SMSCore, if the APP_ID is recognized it calls the listener.
     * @param sms The object representing the short message.
     */
    protected static void handleMessage(SmsMessage sms) {
        String content = sms.getDisplayMessageBody();
        if(content.charAt(0) != APP_ID) return;
        if(smsReceivedListener == null) pendingMessages.add(sms);
        else smsReceivedListener.onMessageReceived(new SMSMessage(new SMSPeer(sms.getDisplayOriginatingAddress()), content.substring(1)));
    }

    /**
     * Create only to test an sms insert if smsListener is null
     * @return true if is empty
     */
    public static boolean isPendingMessagesEmpty(){
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
