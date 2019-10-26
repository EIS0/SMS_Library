package com.eis0.sms_library;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SMSCore extends BroadcastReceiver {

    private static SmsManager manager = SmsManager.getDefault();
    private static final String[] PERMISSIONS = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS
    };

    /**
     * Verifies if the launching activity owns the permissions to use the library
     * @param activity must be an Activity Object
     * @return boolean
     */
    public static boolean hasPermissions(Activity activity) {
        boolean hasPermissions = true;
        for (String permission : PERMISSIONS)
            if(ContextCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) hasPermissions = false;
        return hasPermissions;
    }

    /**
     * Request permissions to use the library
    * @param activity Activity which is asking for permissions
    * */
    public static void requestPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, 1);
    }

    /**
     * Sends a message (SMS) to the specified target
     * @param to destinatario del messaggio (con codice nazionale facoltativo)
     * @param message messaggio da inviare al destinatario (massimo 180 caratteri)
     */
    public static void sendMessage(String to, String message) {
        if(to.length() > 15) {
            Log.e("SMS_SEND","Invalid destination \"" + to + "\"");
            throw new IllegalArgumentException("Invalid destination \"" + to + "\"");
        }
        try {
            manager.sendTextMessage(to,null, message,null,null);
            Log.i("SMS_SEND", "Message \"" + message + "\" sent to \"" + to + "\"");
        }
        catch (Exception e) {
            Log.e("SMS_SEND", e.getMessage());
            throw e;
        }
    }

    /**
     * Function called when a message is received, calls the specified listeners' function when it
     * need to be executed
     * @param context Contesto per ricevere i messaggi (necessaria per BroadcastReceiver)
     * @param intent Intent di ricezione messaggi (necessaria per BroadcastReceiver)
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
