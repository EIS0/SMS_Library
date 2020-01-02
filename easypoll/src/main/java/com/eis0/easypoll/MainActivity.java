package com.eis0.easypoll;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import com.eis0.easypoll.poll.PollManager;
import com.eis0.easypoll.ui.SectionsPagerAdapter;
import com.eis0.smslibrary.SMSPeer;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Set;

/**
 * Main Activity view controller, it basically manages permissions granting and initializes tabs.
 *
 * @author Matteo Carnelos
 * @author Marco Cognolato
 */
public class MainActivity extends AppCompatActivity {

    private static final int NEW_POLL_REQUEST_CODE = 0;
    private static final String[] PERMISSIONS = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS
    };

    private PollManager pollManager = PollManager.getInstance();

    /**
     * Called on the creation of the activity.
     * Asks for permissions and initialize UI elements.
     *
     * @param savedInstanceState Instance saved from a previous activity destruction.
     * @author Matteo Carnelos
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializes the tabbed view.
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Asks the user the permission to catch notifications, if not already granted
        if (!isNotificationListenerEnabled(getApplicationContext()))
            openNotificationListenSettings();

        // Requests app permissions, if not already granted
        ActivityCompat.requestPermissions(this, PERMISSIONS, 1);

        // Load previously saved data and set the directory to which data will be saved
        // DataProvider.setOutputFilesDir(getFilesDir());
        // BinaryPoll.setSharedPreferences(getPreferences(Context.MODE_PRIVATE));
        // DataProvider.loadDataFromInternal(this);
        // BinaryPoll.loadPollsCountFromInternal();
    }

    /**
     * Called when the fab button in the main activity is pressed.
     * Opens the new poll activity as ActivityForResult.
     *
     * @param view The view on which the onClick event is coming from.
     * @author Matteo Carnelos
     */
    public void newPollOnClick(View view) {
        Intent newPollIntent = new Intent(this, CreatePollActivity.class);
        startActivityForResult(newPollIntent, NEW_POLL_REQUEST_CODE);
    }

    /**
     * Called when an ActivityForResult calls the finish() method.
     * Checks the requestCode and the resultCode, and in case creates a new poll.
     *
     * @param requestCode The request code linked with the startActivityForResult(...) method.
     * @param resultCode  An integer representing the result of the operations.
     * @param data        An intent with all the data sent from the ActivityForResult back to the starting
     *                    activity (i.e. MainActivity).
     * @author Matteo Carnelos
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_POLL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra(CreatePollActivity.ARG_POLL_NAME);
                String question = data.getStringExtra(CreatePollActivity.ARG_POLL_QUESTION);
                // There's no need for a checked cast, see CreatePollActivity
                ArrayList<SMSPeer> peers = (ArrayList<SMSPeer>) data.getSerializableExtra(CreatePollActivity.ARG_POLL_PEERS);
                pollManager.createPoll(name, question, peers);
            }
        }
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
