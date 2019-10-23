package com.eis0.sms_library;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.telephony.SmsManager;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

public class SMS_Library{
    private BroadcastReceiver suInvio;
    private BroadcastReceiver suConsegna;
    private String numero = "3301234567"; // di pura fantasia
    private String testo = "Ciao, come stai?";
    private SmsManager smsManager = SmsManager.getDefault();
    private PendingIntent inviato = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_INVIATO"), 0);
    private PendingIntent consegnato = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_CONSEGNATO"), 0);
    suInvio=new BroadcastReceiver() {
        @Override
        public void onReceive (Context arg0, Intent arg1)
        {
            if (getResultCode() == Activity.RESULT_OK)
                Toast.makeText(arg0, "SMS inviato correttamente", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(arg0, "Errore in invio", Toast.LENGTH_LONG).show();
        }
    }

    ;
    suConsegna=new BroadcastReceiver() {
        @Override
        public void onReceive (Context arg0, Intent arg1)
        {
            if (getResultCode() == Activity.RESULT_OK)
                Toast.makeText(arg0, "SMS consegnato", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(arg0, "Errore", Toast.LENGTH_LONG).show();
        }
    }

    ;

    registerReceiver(suInvio, new IntentFilter("SMS_INVIATO"));

    registerReceiver(suConsegna, new IntentFilter("SMS_CONSEGNATO"));
        smsManager.sendTextMessage(numero,null,testo,inviato,consegnato);


}