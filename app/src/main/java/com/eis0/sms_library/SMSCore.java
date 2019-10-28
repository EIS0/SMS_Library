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

    /**
     * Check if permissions are granted, if not requests the required ones.
     * @param activity Activity which is asking for permissions.
     */
    protected static void checkPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, 1);
    }

    /**
     * Sends a message (SMS) to the specified target.
     * @param to Destination phone number.
     * @param message Message to send to the destination number.
     * @param sent PendingIntent to activate when the message is sent.
     * @param delivered PendingIntent to activate when the message is delivered.
     */
    protected static void sendMessage(String to, String message, PendingIntent sent, PendingIntent delivered) {
        if(to.length() > 15) {
            Log.e("SMS_SEND","Invalid destination \"" + to + "\"");
            throw new IllegalArgumentException("Invalid destination \"" + to + "\"");
        }
        try {
            manager.sendTextMessage(to,null, message, sent, delivered);
            Log.i("SMS_SEND", "Message \"" + message + "\" sent to \"" + to + "\"");
        }
        catch (Exception e) {
            Log.e("SMS_SEND", e.getMessage());
            throw e;
        }
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
        String from = shortMessage.getDisplayOriginatingAddress();
        String message = shortMessage.getDisplayMessageBody();
        Log.i("SMS_RECEIVE", "Message \"" + message + "\" received from \"" + from + "\"");
        SMSHandler.handleMessage(from, message);
    }
}
