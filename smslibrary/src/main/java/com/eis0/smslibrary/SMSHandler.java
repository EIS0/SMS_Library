package com.eis0.smslibrary;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;

@TargetApi(21)
public class SMSHandler extends NotificationListenerService {

    private static final char APP_ID = '\r';
    private static final String LOG_KEY = "SMS_HANDLER";
    private static ArrayList<SMSMessage> pendingMessages = new ArrayList<>();

    private static ReceivedMessageListener smsReceivedListener;

    /**
     * Sends a valid SMSmessage, with sent and delivery confirmation.
     * @param message SMSMessage to send to the SMSPeer.
     * @param sent PendingIntent listening for message sent
     * @param delivered PendingIntent listening for message delivery
     * @throws IllegalArgumentException if the destination is invalid
     */
    protected static void sendMessage(SMSMessage message, PendingIntent sent, PendingIntent delivered) {
        SMSPeer destination = message.getPeer();
        SMSMessage msg = new SMSMessage(destination, APP_ID + message.getData());
        if(!destination.isValid()) {
            Log.e(LOG_KEY,"Invalid destination \"" + destination + "\"");
            throw new IllegalArgumentException("Invalid destination \"" + destination + "\"");
        }
        SMSCore.sendMessage(msg, sent, delivered);
    }

    /**
     * Sets the listener, that is the object to be called when an SMS with the APP_ID is received.
     * @param listener Must be an object that implements the ReceivedMessageListener<SMSMessage> interface.
     */
    protected static void setReceiveListener(ReceivedMessageListener<SMSMessage> listener) {
        smsReceivedListener = listener;
        for (SMSMessage pendingMessage : pendingMessages) smsReceivedListener.onMessageReceived(pendingMessage);
        pendingMessages.clear();
    }

    /**
     * Removes a listener from listening to incoming messages
     */
    protected static void removeReceiveListener() {
        smsReceivedListener = null;
    }

    /**
     * Analyze the message received by SMSCore, if the APP_ID is recognized it calls the listener.
     * @param sms The object representing the short message.
     */
    protected static void handleMessage(SmsMessage sms) {
        String content = sms.getDisplayMessageBody();
        if(content.charAt(0) != APP_ID) return;
        SMSMessage message = new SMSMessage(
                new SMSPeer(sms.getDisplayOriginatingAddress()),
                content.substring(1));
        if(smsReceivedListener == null) pendingMessages.add(message);
        else smsReceivedListener.onMessageReceived(message);
    }

    /**
     * Returns true if there's no pending message, false otherwise
     */
    protected static boolean isPendingMessagesEmpty() {
        return pendingMessages.isEmpty();
    }

    /**
     * Overridden method that catches the notifications. If a messaging notification is
     * recognized and it contains the APP_ID than it will be cancelled.
     * @param sbn StatusBarNotification object that contains all the notification informations.
     */
    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        if(sbn.getPackageName().equals("com.google.android.apps.messaging")
                && sbn.getNotification().tickerText.toString().contains(APP_ID + ""))
            cancelNotification(sbn.getKey());
    }
}
