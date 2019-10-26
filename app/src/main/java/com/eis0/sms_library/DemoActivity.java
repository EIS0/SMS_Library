package com.eis0.sms_library;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import java.util.Set;


public class DemoActivity extends AppCompatActivity implements SMSReceivedListener {

    private static final String WAKE_MESSAGE = "1163993";
    private SMSLib SMS = new SMSLib();
    private EditText destText;

    /**
    * Demo start function
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        //asks the user for permission if not already granted
        if(!isNotificationListenerEnabled(getApplicationContext())) {
            openNotificationListenSettings();
        }

        destText = findViewById(R.id.destinatarioText);

        SMS.requestPermissions(this);
        SMS.addOnReceiveListener(this);
    }

    /**
     * Sends a message to the target received in input from the demo
     * @param view
     */
    public void inviaButtonOnClick(View view) {
        String destination = destText.getText().toString();
        if(destination.isEmpty()) {
            Toast.makeText(this, "Il campo \"Destinatario\" non può essere vuoto.", Toast.LENGTH_LONG).show();
            return;
        }
        sendHello(destination);
    }

    /**
     * Sends a message (SMS) to the specified target
     * @param to target who will receive the message "1163993"
     */
    public void sendHello(String to) {
        SMS.requestPermissions(this);
        String toastMessage;
        try {
            SMS.sendMessage(to, "1163993");
            toastMessage = "Saluto inviato a " + to;
        } catch(Exception e) {
            toastMessage = "Errore durante l'invio del messaggio:\n" + e.getMessage();
        }
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }




    /**
     * Creates and shows an Alert when a message is received
     * @param from phone number of the user who sent the message
     * @param message text of the SMS message
     */
    public void SMSOnReceive(final String from, String message) {
        new AlertDialog.Builder(this)
            .setTitle("Saluto ricevuto da " + from + "!")
            .setPositiveButton("Contraccambia", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                sendHello(from);
                }
            })
            .setNegativeButton("OK", null)
            .setIcon(R.drawable.ic_saluto_ricevuto)
            .show();
    }

    /**
     *
     * @param wakeKey
     * @return boolean
     */
    public boolean shouldWakeWith(String wakeKey) {
        return wakeKey.contains(WAKE_MESSAGE);
    }

    /**
     * checks if the notification listener is enabled
     * @param context Context where the notification listener should be active
     * @return returns if the notification listener is enabled
     */
    public boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        return packageNames.contains(context.getPackageName());
    }

    /**
     * Opens the notification settings menu for the user to enable notifications
     */
    public void openNotificationListenSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
