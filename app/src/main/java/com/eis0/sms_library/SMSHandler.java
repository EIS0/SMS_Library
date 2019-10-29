package com.eis0.sms_library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.SmsMessage;

import androidx.lifecycle.Lifecycle;

import java.util.ArrayList;
import java.util.Collection;

@TargetApi(21)
public class SMSHandler extends NotificationListenerService {

    private static final char APP_ID = (char)0x02;
    private static SMSOnReceiveListener smsListener;
    private static ArrayList<SmsMessage> pendingMessages = new ArrayList<>();

    /**
     * Sets the listener, that is the object to be called when an SMS with the APP_ID is received.
     * @param listener Must be an object that implements the SMSOnReceiveListener interface.
     */
    public static void setSMSOnReceiveListener(SMSOnReceiveListener listener) {
        smsListener = listener;
        ArrayList<SmsMessage> pendMsgs = new ArrayList<>();
        pendMsgs.addAll(pendingMessages);
        pendingMessages.clear();
        for (SmsMessage pendingMessage : pendMsgs) handleMessage(pendingMessage);
    }

    /**
     * Analyze the message received by SMSCore, if the APP_ID is recognized it calls the listener.
     * @param sms The object representing the short message.
     */
    protected static void handleMessage(SmsMessage sms) {
        String content = sms.getDisplayMessageBody();
        if(content.charAt(0) != APP_ID) return;
        if(smsListener == null || smsListener.isDestroyed()) pendingMessages.add(sms);
        else smsListener.SMSOnReceive(sms.getDisplayOriginatingAddress(), content.substring(1));
    }

    /**
     * Overridden method that catches the notifications. If a notification of type 'sms' is
     * recognized and it contains the APP_ID than it will be cancelled.
     * @param sbn StatusBarNotification object that contains all the notification informations.
     */
    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        if(notification.category != null && notification.category.equals("msg") && notification.tickerText.toString().contains(APP_ID + ""))
            cancelNotification(sbn.getKey());
    }
}
