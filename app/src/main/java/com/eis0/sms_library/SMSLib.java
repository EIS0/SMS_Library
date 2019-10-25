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

import java.util.ArrayList;

public class SMSLib extends BroadcastReceiver {

    private static ArrayList<DemoActivity> listeners = new ArrayList<>();
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
    public boolean hasPermissions(Activity activity) {
        // Probabilmente inutile
        boolean hasPermissions = true;
        for (String permission : PERMISSIONS)
            if(ContextCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) hasPermissions = false;
        return hasPermissions;
    }

    /**
     * Request permissions to use the library
    * @param activity Activity where to ask permission tu the user
    * */
    public void requestPermissions(Activity activity) {
        Log.d("SMS_PERMISSION_REQUEST", "Requesting Permissions");
        ActivityCompat.requestPermissions(activity, PERMISSIONS, 1);
    }

    /**
     * Adds a listener object to the callbacks list
     * @param listener must be DemoActivity
     */
    public void addOnReceiveListener(DemoActivity listener) {
        listeners.add(listener);
    }

    /**
     * Removes a callback object from the list
     * @param listener must be DemoActivity
     */
    public void removeOnReceiveListener(DemoActivity listener) {
        listeners.remove(listener);
    }

    /**
    * Cleans the entire callbacks list
    * */
    public void clearOnReceiveListeners() {
        listeners.clear();
    }

    /**
     * Sends a message (SMS) to the specified target
     * @param to destinatario del messaggio (con codice nazionale facoltativo)
     * @param message messaggio da inviare al destinatario (massimo 180 caratteri)
     */
    public void sendMessage(String to, String message) {
        if(to.length() > 13) {
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
        String text = shortMessage.getDisplayMessageBody();
        Log.i("SMS_RECEIVE", "Message \"" + text + "\" received from \"" + from + "\"");
        for(DemoActivity listener : listeners)
            if(listener.shouldWakeWith(text)) listener.SMSOnReceive(from, text);
    }
}
