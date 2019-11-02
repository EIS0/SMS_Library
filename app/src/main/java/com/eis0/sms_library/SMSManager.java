package com.eis0.sms_library;

import android.app.Activity;
import android.app.PendingIntent;

public class SMSManager {
    public static void sendSms(String to, String message){
        SMSHandler.SMSSendMessage(to, message, null, null);
    }

    public static void sendTrackingSms(String to, String message, PendingIntent sent, PendingIntent delivered){
        SMSHandler.SMSSendMessage(to, message, sent, delivered);
    }

    public static void setSmsReceiver(final SMSListener listener){
        SMSOnReceiveListener newListener = new SMSOnReceiveListener() {
            @Override
            public void SMSOnReceive(String from, String message) {
                listener.onReceiveSMS(from, message);
            }
        };
        SMSHandler.setSMSOnReceiveListener(newListener);
    }

    public static void checkPermissions(Activity activity) {
        SMSCore.checkPermissions(activity);
    }
}
