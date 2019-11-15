package com.eis0.library_demo;

import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    /**
     * @author Giovanni Velludo
     */
    public void onMessageReceived() {
        // CASE 0: received new poll
        PollListener mockListener = mock(PollListener.class);
        pollManager.setPollListener(mockListener);

        String sep = PollManager.FIELD_SEPARATOR;
        int messageCode = 0;
        SMSPeer pollAuthor = new SMSPeer("3337235485");
        int pollId = 38;
        String pollName = "Pizza";
        String pollQuestion = "Should we get takeout pizza for dinner?";
        ArrayList<SMSPeer> pollUsers = new ArrayList<>(3);
        pollUsers.add(new SMSPeer("3493619544"));
        pollUsers.add(new SMSPeer("3335436782"));
        pollUsers.add(new SMSPeer("+396662838864"));

        String text = messageCode + sep + pollId + sep + pollName + sep + pollQuestion;
        for (SMSPeer user : pollUsers) {
            text += sep + user;
        }
        SMSMessage message = new SMSMessage(pollAuthor, text);

        TernaryPoll verificationPoll =
                new TernaryPoll(pollName, pollQuestion, pollAuthor, pollId, pollUsers);

        pollManager.onMessageReceived(message);
        verify(mockListener).onIncomingPoll(verificationPoll);
    }
}