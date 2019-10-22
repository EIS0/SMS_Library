package com.eis0.sms_library;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DemoActivity extends AppCompatActivity implements SMSReceivedListener {

    private SMSLib SMS = new SMSLib();

    private EditText destText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        destText = findViewById(R.id.destinatarioText);

        SMS.requestPermissions(this);
        SMS.addOnReceiveListener(this);
    }

    public void inviaButtonOnClick(View view) {
        String toastMessage;
        String destination = destText.getText().toString();
        if(destination.isEmpty()) return;
        try {
            SMS.sendMessage(destination, "1163993");
            toastMessage = "Saluto inviato a " + destination;
        } catch(Exception e) {
            toastMessage = "Errore durante l'invio del messaggio, maggiori informazioni sul log";
        }
        Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
    }

    public void SMSOnReceive(String from, String message) {
        findViewById(R.id.ciaoLabel).setVisibility(View.VISIBLE);
        Toast.makeText(this, "Saluto ricevuto da " + from, Toast.LENGTH_LONG).show();
    }

}
