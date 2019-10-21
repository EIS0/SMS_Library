package com.eis0.sms_library;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.os.Bundle;

public class SendSMS extends AppCompatActivity {

    private EditText txtMobile;
    private EditText txtMessage;
    private Button btnSms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        txtMobile = (EditText)findViewById(R.id.mblTxt);
        txtMessage = (EditText)findViewById(R.id.msgTxt);
        btnSms = (Button)findViewById(R.id.btnSend);
        ActivityCompat.requestPermissions(SendSMS.this,
                new String[]{
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECEIVE_SMS
                }, 1);
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(txtMobile.getText().toString(),null,txtMessage.getText().toString(),null,null);
                    Toast.makeText(SendSMS.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(SendSMS.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                    Log.d("SMSERROR", e.toString());
                }
            }
        });
    }
}