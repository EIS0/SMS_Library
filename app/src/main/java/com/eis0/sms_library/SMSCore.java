package com.eis0.sms_library;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class SMSCore extends BroadcastReceiver {

    private static SmsManager manager = SmsManager.getDefault();
    private static final String[] PERMISSIONS = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS
    };
    private static final String LOG_KEY = "SMS_CORE";

    /**
     * Check if permissions are granted, if not requests the required ones.
     * @param activity Activity which is asking for permissions.
     */
    protected static void checkPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, 1);
    }

    /**
     * Sends a message (SMS) to the specified target, with sent and delivery confirmation.
     * @param to Destination phone number.
     * @param message Message to send to the destination number.
     * @param sent PendingIntent to activate when the message is sent.
     * @param delivered PendingIntent to activate when the message is delivered.
     */
    protected static void sendMessage(String to, String message, PendingIntent sent, PendingIntent delivered) {
        manager.sendTextMessage(to,null, message, sent, delivered);
        Log.i(LOG_KEY, "Message \"" + message + "\" sent to \"" + to + "\"");
    }

    /**
     * Function called when a message is received. It delegates the message to the SMS Message
     * Handler which analyzes its content.
     * @param context Received message context.
     * @param intent Received message Intent.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[])intent.getExtras().get("pdus");
        SmsMessage shortMessage = SmsMessage.createFromPdu((byte[])pdus[0]);
        Log.i(LOG_KEY, "Message received");
        SMSHandler.handleMessage(shortMessage);
    }
}
