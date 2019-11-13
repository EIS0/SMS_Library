package com.eis0.library_demo;

import android.content.Context;

import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PollManagerTest {
    private static PollManager pollManager;

    @Mock
    private static Context fakeContext;
    private static PollListener fakePollListener;

    @BeforeClass
    public static void classSetUp() {
        pollManager = PollManager.getInstance(fakeContext);
        pollManager.addPollListener(fakePollListener);
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

        when(fakePollListener.onNewPollReceived(TernaryPoll poll))
                .thenReturn(poll);


        // registers listener which will be called by PollManager
        pollManager.addPollListener(fakePollListener);
        SMSPeer sender = new SMSPeer("3337235485");
        String text = "03337235485\r38\rQuestion\r3493619544\r3335436782\r+396662838864";
        SMSMessage message = new SMSMessage(sender, text);
        pollManager.onMessageReceived(message);
        /* TODO: assert if onNewPollReceived() is being called (with Mockito?)
         */

        ArrayList<SMSPeer> users = new ArrayList<>();
        users.add(new SMSPeer("3493619544"));
        users.add(new SMSPeer("3335436782"));
        users.add(new SMSPeer("+396662838864"));
        TernaryPoll verificationPoll =
                new TernaryPoll("Question", new SMSPeer("3337235485"),
                        users, 38);
        verify(fakePollListener).onNewPollReceived(verificationPoll);
    }
}