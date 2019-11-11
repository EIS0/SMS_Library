package com.eis0.library_demo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.view.View;

import com.eis0.library_demo.ui.main.SectionsPagerAdapter;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final String[] PERMISSIONS = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Asks the user for permission if not already granted
        if(!isNotificationListenerEnabled(getApplicationContext()))
            openNotificationListenSettings();

        // Request app permissions, if not already granted
        ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
    }

    public void newPollOnClick(View view) {
        Intent newPollIntent = new Intent(this, CreatePollActivity.class);
        startActivity(newPollIntent);
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