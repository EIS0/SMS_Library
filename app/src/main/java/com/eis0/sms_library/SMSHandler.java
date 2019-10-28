package com.eis0.sms_library;

import android.annotation.TargetApi;
import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

@TargetApi(21)
public class SMSHandler extends NotificationListenerService {

    private static final char APP_ID = (char)0x02;
    private static SMSOnReceiveListener l;

    /**
     * Sets the listener, that is the object to be called when an SMS with the APP_ID is received.
     * @param listener Must be an object that implements the SMSOnReceiveListener interface.
     */
    protected static void setSMSOnReceiveListener(SMSOnReceiveListener listener) {
        l = listener;
    }

    /**
     * Analyze the message received by SMSCore, if the APP_ID is recognized it calls the listener.
     * @param from Phone number of the SMS sender.
     * @param message Message received by SMS.
     */
    protected static void handleMessage(String from, String message) {
        if(message.charAt(0) == APP_ID) l.SMSOnReceive(from, message.substring(1));
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
