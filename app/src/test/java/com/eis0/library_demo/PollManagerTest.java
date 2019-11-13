package com.eis0.library_demo;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import org.junit.Before;
import org.junit.Test;

public class PollManagerTest {
    private Context fakeContext;
    private PollManager pollManager;

    @Before
    public void setUp() {
        fakeContext = ApplicationProvider.getApplicationContext();
        pollManager = PollManager.getInstance(fakeContext);
    }

    @Test
    public void createPoll() {
    }

    @Test
    public void answerPoll() {
    }

    @Test
    public void onNewPollReceived() {
        SMSPeer sender = new SMSPeer("3337235485");
        String text = "03331727754\r38\rQuestion\r3493619544\r3335436782\r+396662838864";
        SMSMessage message = new SMSMessage(sender, text);
        pollManager.onMessageReceived(message);



    }
}