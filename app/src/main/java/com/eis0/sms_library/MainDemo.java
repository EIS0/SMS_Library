package com.eis0.sms_library;

import android.Manifest;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

//import android.support.v7.app.AppCompatActivity;

public class MainDemo extends AppCompatActivity {

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
        ActivityCompat.requestPermissions(MainDemo.this,
                new String[]{
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECEIVE_SMS
                }, 1);

        TestReceiveMethod tm1 = new TestReceiveMethod();
        TestReceiveMethod tm2 = new TestReceiveMethod();

        SMSLib.addOnReceiveListener(tm1);
        SMSLib.addOnReceiveListener(tm2);

        /*
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(txtMobile.getText().toString(),null,txtMessage.getText().toString(),null,null);
                    Toast.makeText(MainDemo.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(MainDemo.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                    Log.d("SMSERROR", e.toString());
                }
            }
        });
        */
    }
}