package com.eis0.library_demo;

import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PollManagerTest {
    private static PollManager pollManager;

    @BeforeClass
    public static void classSetUp() {
        pollManager = PollManager.getInstance();
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
        PollListener mockListener = mock(PollListener.class);
        pollManager.setPollListener(mockListener);

        String sep = PollManager.FIELD_SEPARATOR;
        SMSPeer sender = new SMSPeer("3337235485");
        String text = "0" + sep +"38" + sep + "Pizza" + sep + "Should we get takeout pizza for" +
                "dinner?" + sep + "3493619544" + sep + "3335436782" + sep + "+396662838864";
        SMSMessage message = new SMSMessage(sender, text);

        ArrayList<SMSPeer> users = new ArrayList<>();
        users.add(new SMSPeer("3493619544"));
        users.add(new SMSPeer("3335436782"));
        users.add(new SMSPeer("+396662838864"));
        TernaryPoll verificationPoll =
                new TernaryPoll("Pizza", "Should we get takeout pizza for dinner?",
                        sender, 38, users);

        pollManager.onMessageReceived(message);
        /* TODO: assert if onNewPollReceived() is being called (with Mockito?)
         */
        verify(mockListener, times(1)).onIncomingPoll(verificationPoll);

    }
}