package com.eis0.library_demo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.KademliaFailReason;
import com.eis0.kademlianetwork.KademliaJoinableNetwork;
import com.eis0.kademlianetwork.KademliaNetwork;
import com.eis0.netinterfaces.listeners.InviteListener;

import java.util.Set;

/**
 * App's main activity.
 *
 * @author Matteo Carnelos
 */
public class KademliaDemo extends AppCompatActivity implements InviteListener<SMSPeer, KademliaFailReason> {

    private static final String[] PERMISSIONS = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS
    };

    EditText phoneNumberTxt;
    TextView myPhoneNumberLbl;
    TextView myIdLbl;
    RecyclerView routingTableRclView;

    RoutingTableAdapter routingTableAdapter;
    KademliaNetwork network = (KademliaNetwork) KademliaJoinableNetwork.getInstance();

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
        myIdLbl = findViewById(R.id.myIdLbl);
        routingTableRclView = findViewById(R.id.routingTableRclView);
        phoneNumberTxt = findViewById(R.id.phoneNumberTxt);

        // Asks the user the permission to catch notifications, if not already granted
        if (!isNotificationListenerEnabled(getApplicationContext()))
            openNotificationListenSettings();

        // Requests app permissions, if not already granted
        ActivityCompat.requestPermissions(this, PERMISSIONS, 1);

        // Initialize routing table recycler view
        routingTableRclView.setLayoutManager(new LinearLayoutManager(this));

        // Instantiates a new kademlia network with only this phone
        network.init(new SMSKademliaNode(getPhoneNumber()), this);

        // Display initialization data
        myPhoneNumberLbl.setText(network.getLocalNode().getPeer().toString());
        myIdLbl.setText(network.getLocalNode().getId().toString());
        routingTableAdapter = new RoutingTableAdapter(network.getLocalRoutingTable().getAllContacts());
        routingTableRclView.setAdapter(routingTableAdapter);
    }

    /**
     * Called when the button to add a new peer to the network is pressed, it adds the new peer to
     * the network.
     *
     * @param v The from which the event is coming from.
     * @author Matteo Carnelos
     */
    public void addButtonOnClick(View v) {
        String address = phoneNumberTxt.getText().toString();
        network.invite(new SMSPeer(address), this);
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
            String phoneNumberText = telephonyManager.getLine1Number();
            // if(phoneNumberText.contains("5554")) phoneNumberText = "5556";
            // else if(phoneNumberText.contains("5556")) phoneNumberText = "5554";
            phoneNumber = new SMSPeer(phoneNumberText);
            Log.e("PHONE_GET", "Phone Number: " + phoneNumber.getAddress());
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


    /**
     * Callback for successful sent invitation.
     *
     * @param invitedPeer Who has been invited.
     * @author Marco Cognolato
     */
    public void onInvitationSent(SMSPeer invitedPeer){

    }

    /**
     * Callback for failed sending of invitation.
     *
     * @param notInvitedPeer Who were to be invited.
     * @param failReason     The reason for the failed invitation send.
     * @author Marco Cognolato
     */
    public void onInvitationNotSent(SMSPeer notInvitedPeer, KademliaFailReason failReason){

    }
}
