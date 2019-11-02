package com.eis0.sms_library;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;

@TargetApi(21)
public class SMSHandler extends NotificationListenerService {

    private static final char APP_ID = (char)0x02;
    private static ReceivedMessageListener smsListener;
    private static ArrayList<SmsMessage> pendingMessages = new ArrayList<>();
    private static final String LOG_KEY = "SMS_HANDLER";

    /**
     * Sends a message (SMS) to the specified target, with sent and delivery confirmation.
     * @param message Message to send to the destination Peer.
     * @param sent PendingIntent to activate when the message is sent.
     * @param delivered PendingIntent to activate when the message is delivered.
     */
    public static void SMSSendMessage(Message message, PendingIntent sent, PendingIntent delivered) {
        Peer destination = message.getPeer();
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
    public static void setSMSOnReceiveListener(ReceivedMessageListener listener) {
        smsListener = listener;
        for (SmsMessage pendingMessage : pendingMessages) handleMessage(pendingMessage);
        pendingMessages.clear();
    }

    /**
     * Analyze the message received by SMSCore, if the APP_ID is recognized it calls the listener.
     * @param sms The object representing the short message.
     */
    protected static void handleMessage(SmsMessage sms) {
        String content = sms.getDisplayMessageBody();
        if(content.charAt(0) != APP_ID) return;
        if(smsListener == null) pendingMessages.add(sms);
        else smsListener.onMessageReceived(new Message(new Peer(sms.getDisplayOriginatingAddress()), content.substring(1)));
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
