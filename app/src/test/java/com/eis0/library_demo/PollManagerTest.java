package com.eis0.library_demo;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;

import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class PollManagerTest implements PollListener {
    private static Context fakeContext;
    private static PollManager pollManager;

    @BeforeClass
    public static void classSetUp() {
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
    public void onMessageReceived() {
        // CASE 0: received new poll

        // registers listener which will be called by PollManager
        pollManager.addPollListener(this);
        SMSPeer sender = new SMSPeer("3337235485");
        String text = "03337235485\r38\rQuestion\r3493619544\r3335436782\r+396662838864";
        SMSMessage message = new SMSMessage(sender, text);
        pollManager.onMessageReceived(message);
        /* TODO: assert if onNewPollReceived() is being called (with Mockito?). If there's a way of
         *  doing it, then remove the implementation of PollListener and its methods from this class
         */
    }

    @Override
    public void onNewPollReceived(TernaryPoll poll) {
        ArrayList<SMSPeer> users = new ArrayList<>();
        users.add(new SMSPeer("3493619544"));
        users.add(new SMSPeer("3335436782"));
        users.add(new SMSPeer("+396662838864"));
        TernaryPoll verificationPoll =
                new TernaryPoll("Question", new SMSPeer("3337235485"),
                        users, 38);
        assertThat(poll).isEqualTo(verificationPoll);
    }

    @Override
    public void onPollUpdated(TernaryPoll poll) {

    }
}