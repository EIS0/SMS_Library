package com.eis0.library_demo;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import org.junit.Test;

public class PollManagerTest {
    private Context context = ApplicationProvider.getApplicationContext();

    @Test
    public void createPoll() {
    }

    @Test
    public void answerPoll() {
    }

    @Test
    public void onMessageReceived() {
        PollManager pollManager = PollManager.getInstance(context);
        String text = "03331727754\r38\rQuestion\r3493619544"
        SMSMessage message = new SMSMessage(new SMSPeer("3337235485"), text);
        pollManager.onMessageReceived(message);



    }
}