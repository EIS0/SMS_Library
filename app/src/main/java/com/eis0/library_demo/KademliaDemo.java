package com.eis0.library_demo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.eis0.kademlianetwork.KademliaNetwork;
import com.eis0.smslibrary.SMSPeer;

import java.util.Set;

/**
 * App's main activity.
 *
 * @author Matteo Carnelos
 */
public class KademliaDemo extends AppCompatActivity {

    private static final String[] PERMISSIONS = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS
    };

    TextView myPhoneNumberLbl;

    KademliaNetwork kademliaNetwork;

    /**
     * Called on the creation of the activity.
     * Asks for permissions and initialize network.
     *
     * @param savedInstanceState Instance saved from a previous activity destruction.
     * @author Matteo Carnelos
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kademlia_demo);

        myPhoneNumberLbl = findViewById(R.id.myPhoneNumberLbl);

        // Asks the user the permission to catch notifications, if not already granted
        if (!isNotificationListenerEnabled(getApplicationContext()))
            openNotificationListenSettings();

        // Requests app permissions, if not already granted
        ActivityCompat.requestPermissions(this, PERMISSIONS, 1);

        // Instantiates a new kademlia network with only this phone
        SMSPeer myPhoneNumber = getPhoneNumber();
        myPhoneNumberLbl.setText(myPhoneNumber.toString());
        kademliaNetwork = KademliaNetwork.getInstance();
    }

    public void addButtonOnClick(View v) {

    }

    /**
     * Get the actual phone number.
     * NOTE: This implementation of the method is known to cause issues in some rare cases, it
     * should be modified and improved in next versions.
     *
     * @author Matteo Carnelos
     */
    private SMSPeer getPhoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        SMSPeer phoneNumber = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.error_dialog_title)
                .setCancelable(false)
                .setNeutralButton(R.string.exit_button_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
        try {
            phoneNumber = new SMSPeer(telephonyManager.getLine1Number());
        } catch(SecurityException e) {
            builder.setMessage(R.string.permissions_not_granted_message);
            builder.show();
        } catch(IllegalArgumentException e) {
            builder.setMessage(R.string.unknown_phone_number_dialog_message);
            builder.show();
        }
        return phoneNumber;
    }

    /**
     * Check if the notification listener is enabled.
     *
     * @param context Context where the notification listener should be active.
     * @return Returns if the notification listener is enabled.
     * @author Marco Cognolato
     */
    private boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        return packageNames.contains(context.getPackageName());
    }

    /**
     * Open the notification settings menu for the user to enable notifications.
     *
     * @author Marco Cognolato
     */
    private void openNotificationListenSettings() {
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        startActivity(intent);
    }

}
