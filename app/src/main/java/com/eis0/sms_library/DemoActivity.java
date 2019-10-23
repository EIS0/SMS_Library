package com.eis0.sms_library;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class DemoActivity extends AppCompatActivity implements SMSReceivedListener {

    private SMSLib SMS = new SMSLib();

    private EditText destText;

    /*
    * Funzione di start della demo
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Log.d("DEMO_START", "Starting Demo");

        destText = findViewById(R.id.destinatarioText);

        SMS.requestPermissions(this);
        SMS.addOnReceiveListener(this);
    }

    /*
    * Funzione che invia un messaggio al destinatario scritto in input della demo.
    * Chiamata quanto viene cliccato il tasto "Invia Saluto".
    * */
    public void inviaButtonOnClick(View view) {
        String destination = destText.getText().toString();
        if(destination.isEmpty()) {
            Toast.makeText(this, "Il campo \"Destinatario\" non può essere vuoto.", Toast.LENGTH_LONG).show();
            return;
        }
        sendHello(destination);
    }

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


    /*
    * Funzione che crea e mostra un Alert quando viene ricevuto un messaggio
    * */

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
}
