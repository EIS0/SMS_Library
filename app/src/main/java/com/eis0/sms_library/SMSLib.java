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
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS
    };

    /**
    * Funzione per controllare se sono stati inviati tutti i permessi
    * necessari per ricezione e invio messaggi
    * */
    public boolean hasPermissions(Activity activity) {
        // Probabilmente inutile
        boolean hasPermissions = true;
        for (String permission : PERMISSIONS)
            if(ContextCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) hasPermissions = false;
        return hasPermissions;
    }

    /**
    * Funzione per richiedere i permessi necessari
    * */
    public void requestPermissions(Activity activity) {
        Log.d("SMS_PERMISSION_REQUEST", "Requesting Permissions");
        ActivityCompat.requestPermissions(activity, PERMISSIONS, 1);
    }

    /**
    * Dato un oggetto di tipo SMSReceivedListener questo viene aggiunto
    * alla lista di oggetti di callback
    * */
    public void addOnReceiveListener(SMSReceivedListener listener) {
        listeners.add(listener);
    }

    /**
    * Rimuove un oggetto di callback dalla lista
    * */
    public void removeOnReceiveListener(SMSReceivedListener listener) {
        listeners.remove(listener);
    }

    /**
    * Pulisce l'intero array di callback dalla lista
    * */
    public void clearOnReceiveListeners() {
        listeners.clear();
    }

    /**
     * Funzione di invio di un messaggio dati destinatario e messaggio da inviare
     *
     * @param to destinatario del messaggio (con codice nazionale facoltativo)
     * @param message messaggio da inviare al destinatario (massimo 180 caratteri)
     */
    public void sendMessage(String to, String message) {
        if(to.length()>13){
            Log.d("ERROR_DESTINATION_INFO:", "invalid destination\"" + to + "\"");
            throw new IllegalArgumentException();
        }
        try {
            manager.sendTextMessage(to,null, message,null,null);
            Log.d("SMS_SEND_INFO", "Message \"" + message + "\" sent to \"" + to + "\"");
        }
        catch (Exception e) {
            Log.d("SMS_SEND_ERROR", e.getMessage());
            throw e;
        }
    }

    /**
     * Funzione chiamata quando viene ricevuto un messaggio.
     * Vengono chiamati i callback presenti nella lista dei listener
     * @param context Contesto per ricevere i messaggi (necessaria per BroadcastReceiver)
     * @param intent Intent di ricezione messaggi (necessaria per BroadcastReceiver)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[])intent.getExtras().get("pdus");
        SmsMessage shortMessage = SmsMessage.createFromPdu((byte[])pdus[0]);
        String from = shortMessage.getDisplayOriginatingAddress();
        String text = shortMessage.getDisplayMessageBody();
        if(text.contains("1163993")) {
            Log.d("SMS_RECEIVED_INFO", "Message \"" + text + "\" received from \"" + from + "\"");
            for(SMSReceivedListener listener : listeners) listener.SMSOnReceive(from, text);
        }
    }
}
