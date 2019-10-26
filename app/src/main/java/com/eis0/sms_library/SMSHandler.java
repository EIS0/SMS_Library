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
     * Initializes the handler and sets the listener object
     * @param listener must be an object that implements the SMSOnReceiveListener interface
     */
    public static void setSMSOnReceiveListener(SMSOnReceiveListener listener) {
        l = listener;
    }

    protected static void handleMessage(String from, String message) {
        if(message.charAt(0) == APP_ID) l.SMSOnReceive(from, message.substring(1));
    }

    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        if(notification.category.equals("msg") && notification.tickerText.toString().contains(APP_ID + ""))
            cancelNotification(sbn.getKey());
    }
}
