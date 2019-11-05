package com.eis0.library_demo;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Lifecycle;

import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;
import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSManager;

import java.util.ArrayList;
import java.util.Set;

public class DemoActivity extends AppCompatActivity implements ReceivedMessageListener<SMSMessage> {

    private EditText destText;
    private SharedPreferences sharedPreferences;
    private boolean deliveryReport = false;

    private static NotificationManager notificationManager;
    private static final String CHANNEL_ID = "eis0_notification_channel";
    private static int notificationID = 0;
    private static ArrayList<String[]> pendingDialogs = new ArrayList<>();
    private static final String[] PERMISSIONS = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS
    };

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

        requestPermissions();
        SMSManager.getInstance(this).addReceiveListener(this);

        createNotificationChannel();
        for(final String[] pendingDialog : pendingDialogs) {
            new AlertDialog.Builder(this)
                    .setTitle(pendingDialog[0] + getString(R.string.says_hi))
                    .setPositiveButton(getString(R.string.say_hi_back), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendHello(new SMSPeer(pendingDialog[0]));
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
    }

    /**
     * Called when the option menu is created
     * @param menu Option menu created
     * @return Returns true when the option menu is created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Called when an item is selected in the option menu
     * @param item Item that gets selected
     * @return Returns the state of the option menu
     */
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
        SMSPeer destination = new SMSPeer(destText.getText().toString());
        if(destination.isEmpty()) {
            Toast.makeText(this, getString(R.string.to_field_cannot_be_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        //TODO: check if destination is valid
        sendHello(destination);
    }

    /**
     * Sends a message (SMS) to the specified target.
     * @param destination Target who will receive the message with the APP_ID.
     */
    private void sendHello(SMSPeer destination) {
        requestPermissions();
        String message = (char)0x02 + "";
        SMSManager.getInstance(this).sendMessage(new SMSMessage(destination, message));
    }

    /**
     * Requests permissions for the library/app to work if not granted
     */
    private void requestPermissions(){
        ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
    }

    /**
     * Creates and shows an Alert when a message is received. If the app is running in background shows
     * a notification.
     * @param message The message received.
     */
    public void onMessageReceived(SMSMessage message) {
        final SMSPeer from = message.getPeer();
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
            String[] pendingDialog = {from.getAddress(), notID + ""};
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
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        startActivity(intent);
    }
}
