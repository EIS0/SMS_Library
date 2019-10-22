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

    private static ArrayList<SMSReceivedListener> listeners = new ArrayList<>();
    private static SmsManager manager = SmsManager.getDefault();
    private static final String[] PERMISSIONS = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS
    };

    public boolean hasPermissions(Activity activity) {
        boolean hasPermissions = true;
        for (String permission : PERMISSIONS)
            if(ContextCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) hasPermissions = false;
        return hasPermissions;
    }

    public void requestPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, 1);
    }

    public void addOnReceiveListener(SMSReceivedListener listener) {
        listeners.add(listener);
    }

    public void removeOnReceiveListener(SMSReceivedListener listener) {
        listeners.remove(listener);
    }

    public void clearOnReceiveListeners() {
        listeners.clear();
    }

    public void sendMessage(String to, String message) {
        try {
            manager.sendTextMessage(to,null, message,null,null);
            Log.i("SMS_SEND", "Message \"" + message + "\" sent to \"" + to + "\"");
        }
        catch (Exception e) {
            Log.e("SMS_SEND", e.getMessage());
            throw e;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[])intent.getExtras().get("pdus");
        SmsMessage shortMessage = SmsMessage.createFromPdu((byte[])pdus[0]);
        String from = shortMessage.getDisplayOriginatingAddress();
        String text = shortMessage.getDisplayMessageBody();
        if(text.contains("1163993")) {
            Log.i("SMS_RECEIVE", "Message \"" + text + "\" received from \"" + from + "\"");
            for(SMSReceivedListener listener : listeners) listener.SMSOnReceive(from, text);
        }
    }
}
