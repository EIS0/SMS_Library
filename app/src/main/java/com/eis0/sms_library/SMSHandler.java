package com.eis0.sms_library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;

@TargetApi(21)
public class SMSHandler extends NotificationListenerService {

    private static final char APP_ID = (char)0x02;
    private static SMSOnReceiveListener smsListener;
    private static ArrayList<SmsMessage> pendingMessages = new ArrayList<>();
    private static final String LOG_KEY = "SMS_HANDLER";
    /**
     * Check if permissions are granted, if not requests the required ones.
     * (Stun method)
     * @param activity Activity which is asking for permissions.
     */
    public static void SMSCheckPermissions(Activity activity) {
        SMSCore.checkPermissions(activity);
    }

    /**
     * Sends a message (SMS) to the specified target, with sent and delivery confirmation.
     * @param to Destination phone number.
     * @param message Message to send to the destination number.
     * @param sent PendingIntent to activate when the message is sent.
     * @param delivered PendingIntent to activate when the message is delivered.
     */
    public static void SMSSendMessage(String to, String message, PendingIntent sent, PendingIntent delivered) {
        if(to.length() > 15) {
            Log.e(LOG_KEY,"Invalid destination \"" + to + "\"");
            throw new IllegalArgumentException("Invalid destination \"" + to + "\"");
        }
        try {
            SMSCore.sendMessage(to, message, sent, delivered);
        }
        catch (Exception e) {
            Log.e(LOG_KEY, e.getMessage());
            throw e;
        }
    }

    /**
     * Sets the listener, that is the object to be called when an SMS with the APP_ID is received.
     * @param listener Must be an object that implements the SMSOnReceiveListener interface.
     */
    public static void setSMSOnReceiveListener(SMSOnReceiveListener listener) {
        smsListener = listener;
        for (SmsMessage pendingMessage : pendingMessages) handleMessage(pendingMessage);
        pendingMessages.clear();
    }

    /**
     * Analyze the message received by SMSCore, if the APP_ID is recognized it calls the listener.
     * @param sms The object representing the short message.
     */
    private static void handleMessage(SmsMessage sms) {
        String content = sms.getDisplayMessageBody();
        if(content.charAt(0) != APP_ID) return;
        if(smsListener == null) pendingMessages.add(sms);
        else smsListener.SMSOnReceive(sms.getDisplayOriginatingAddress(), content.substring(1));
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
