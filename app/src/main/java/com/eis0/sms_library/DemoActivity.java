package com.eis0.sms_library;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Lifecycle;

import java.util.ArrayList;
import java.util.Set;

public class DemoActivity extends AppCompatActivity implements SMSListener {

    private EditText destText;
    private SharedPreferences sharedPreferences;
    private boolean deliveryReport = false;
    private BroadcastReceiver onSend = null;
    private BroadcastReceiver onDeliver = null;
    private static NotificationManager notificationManager;
    private static final String CHANNEL_ID = "eis0_notification_channel";
    private static int notificationID = 0;
    private static ArrayList<String[]> pendingDialogs = new ArrayList<>();

    /**
     * Demo start function.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        // get app settings
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Asks the user for permission if not already granted
        if(!isNotificationListenerEnabled(getApplicationContext()))
            openNotificationListenSettings();

        destText = findViewById(R.id.recipientNumber);

        if (Build.VERSION.SDK_INT >= 23) {
            notificationManager = getSystemService(NotificationManager.class);
        }

        //SMSHandler.SMSCheckPermissions(this);
        //SMSHandler.setSMSOnReceiveListener(this);
        SMSManager.checkPermissions(this);
        SMSManager.setSmsReceiver(this);

        createNotificationChannel();
        for(final String[] pendingDialog : pendingDialogs) {
            new AlertDialog.Builder(this)
                    .setTitle(pendingDialog[0] + getString(R.string.says_hi))
                    .setPositiveButton(getString(R.string.say_hi_back), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendHello(pendingDialog[0]);
                        }
                    })
                    .setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            notificationManager.cancel(Integer.parseInt(pendingDialog[1]));
                        }
                    })
                    .setIcon(R.drawable.ic_hello_received)
                    .show();
        }
        pendingDialogs.clear();
    }

    /**
     * Creates a notification channel (only for API >= 26)
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Every time this activity appears on the foreground
     */
    @Override
    protected void onResume() {
        super.onResume();
        deliveryReport = sharedPreferences.getBoolean("deliveryReport", false);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Sends a message to the target received in input from the demo.
     * @param view View that sends the onClick event.
     */
    public void sendButtonOnClick(View view) {
        String destination = destText.getText().toString();
        if(destination.isEmpty()) {
            Toast.makeText(this, getString(R.string.to_field_cannot_be_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        sendHello(destination);
    }

    /**
     * Sends a message (SMS) to the specified target.
     * @param to Target who will receive the message with the APP_ID.
     */
    private void sendHello(String to) {
        //SMSHandler.SMSCheckPermissions(this);
        SMSManager.checkPermissions(this);
        String message = (char)0x02 + "";

        PendingIntent sent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_SENT"), 0);
        onSend = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getResultCode()== Activity.RESULT_OK)
                    Toast.makeText(context, getString(R.string.message_sent), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, getString(R.string.send_message_error), Toast.LENGTH_SHORT).show();
            }
        };
        registerReceiver(onSend, new IntentFilter("SMS_SENT"));

        if (deliveryReport) {
            PendingIntent delivered = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent("SMS_DELIVERED"), 0);
            onDeliver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (getResultCode() == Activity.RESULT_OK)
                        Toast.makeText(context, getString(R.string.message_delivered), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, getString(R.string.deliver_message_error), Toast.LENGTH_SHORT).show();
                }
            };
            registerReceiver(onDeliver, new IntentFilter("SMS_DELIVERED"));
            //SMSHandler.SMSSendMessage(to, message, sent, delivered);
            SMSManager.sendTrackingSms(to, message, sent, delivered);
        } else {
            SMSManager.sendTrackingSms(to, message, sent, null);
            //SMSHandler.SMSSendMessage(to, message, sent, null);
        }
    }

    /**
     * Creates and shows an Alert when a message is received. If the app is running in background shows
     * a notification.
     * @param from Phone number of the user who sent the message.
     * @param message Text of the SMS message.
     */
    public void onReceiveSMS(final String from, String message) {
        final int notID = notificationID++;
        if(getLifecycle().getCurrentState() != Lifecycle.State.RESUMED) {
            Intent intent = new Intent(this, DemoActivity.class);
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSmallIcon(R.drawable.ic_hello_received)
                    .setContentTitle(from + getString(R.string.says_hi))
                    .setContentText(getString(R.string.open_app))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            notificationManager.notify(notID, builder.build());
        }

        if(getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
            String[] pendingDialog = {from, notID + ""};
            pendingDialogs.add(pendingDialog);
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle(from + getString(R.string.says_hi))
                    .setPositiveButton(getString(R.string.say_hi_back), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendHello(from);
                        }
                    })
                    .setNegativeButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            notificationManager.cancel(notID);
                        }
                    })
                    .setIcon(R.drawable.ic_hello_received)
                    .show();
        }
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
}
