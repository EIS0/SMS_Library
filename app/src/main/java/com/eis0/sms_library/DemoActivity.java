package com.eis0.sms_library;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;

import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Set;

public class DemoActivity extends AppCompatActivity implements SMSOnReceiveListener {

    private EditText destText;
    protected boolean deliveryConfirmation = false;
    private BroadcastReceiver onSend = null;
    private BroadcastReceiver onDeliver = null;

    /**
     * Demo start function.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Asks the user for permission if not already granted
        if(!isNotificationListenerEnabled(getApplicationContext()))
            openNotificationListenSettings();

        destText = findViewById(R.id.recipientNumber);

        SMSCore.checkPermissions(this);
        SMSHandler.setSMSOnReceiveListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            openSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Unregister BroadcastReceivers used for confirmation of sending and delivery of SMS
     */
    @Override
    protected void onStop() {
        super.onStop();
        try{
            unregisterReceiver(onSend);
            unregisterReceiver(onDeliver);
        }
        catch (IllegalArgumentException e){
            Log.d("DemoActivity", "Can't unregister non-registered BroadcastReceiver");
        }
    }

    /**
     * Sends a message to the target received in input from the demo.
     * @param view View that sends the onClick event.
     */
    public void sendButtonOnClick(View view) {
        String destination = destText.getText().toString();
        if(destination.isEmpty()) {
            Toast.makeText(this, getString(R.string.to_field_cannot_be_empty), Toast.LENGTH_LONG).show();
            return;
        }
        sendHello(destination);
    }

    /**
     * Sends a message (SMS) to the specified target.
     * @param to Target who will receive the message with the APP_ID.
     */
    private void sendHello(String to) {
        SMSCore.checkPermissions(this);
        String message = (char)0x02 + "";

        PendingIntent sent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_SENT"), 0);
        onSend = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getResultCode()== Activity.RESULT_OK)
                    Toast.makeText(context, getString(R.string.message_sent), Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(context, getString(R.string.send_message_error), Toast.LENGTH_LONG).show();
            }
        };
        registerReceiver(onSend, new IntentFilter("SMS_SENT"));

        if (deliveryConfirmation) {
            PendingIntent delivered = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_DELIVERED"), 0);
            onDeliver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (getResultCode() == Activity.RESULT_OK)
                        Toast.makeText(context, getString(R.string.message_delivered), Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(context, getString(R.string.deliver_message_error), Toast.LENGTH_LONG).show();
                }
            };
            registerReceiver(onDeliver, new IntentFilter("SMS_DELIVERED"));
            SMSCore.sendMessage(to, message, sent, delivered);
        } else{
            SMSCore.sendMessage(to, message, sent, null);
        }
    }

    /**
     * Creates and shows an Alert when a message is received.
     * @param from Phone number of the user who sent the message.
     * @param message Text of the SMS message.
     */
    public void SMSOnReceive(final String from, String message) {
        new AlertDialog.Builder(this)
            .setTitle(from + getString(R.string.says_hi))
            .setPositiveButton(getString(R.string.say_hi_back), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    sendHello(from);
                }
            })
            .setNegativeButton(getString(R.string.ok), null)
            .setIcon(R.drawable.ic_hello_received)
            .show();
    }

    /**
     * Checks if the notification listener is enabled.
     * @param context Context where the notification listener should be active.
     * @return Returns if the notification listener is enabled.
     */
    private boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        return packageNames.contains(context.getPackageName());
    }

    /**
     * Opens the notification settings menu for the user to enable notifications.
     */
    private void openNotificationListenSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
