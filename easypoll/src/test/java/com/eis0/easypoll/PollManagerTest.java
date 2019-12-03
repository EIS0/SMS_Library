package com.eis0.easypoll;

import com.eis0.easypoll.poll.BinaryPoll;
import com.eis0.easypoll.poll.PollListener;
import com.eis0.easypoll.poll.PollManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;
import com.eis0.smslibrary.SMSPeerTest;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

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

    /**
     * Tests handling of messages received by the application.
     *
     * @author Giovanni Velludo
     */
    @Test
    public void onMessageReceived() {
        // CASE 0: received new poll
        PollListener mockListener = mock(PollListener.class);
        pollManager.setPollListener(mockListener);

        String sep = PollManager.FIELD_SEPARATOR;
        String messageCode = PollManager.NEW_POLL_MSG_CODE;
        SMSPeer pollAuthor = new SMSPeer(SMSPeerTest.VALID_ADDR);
        int pollId = 38;
        String pollName = "Pizza";
        String pollQuestion = "Should we get takeout pizza for dinner?";

        String text = messageCode + sep + pollId + sep + pollName + sep + pollQuestion;
        SMSMessage message = new SMSMessage(pollAuthor, text);

        BinaryPoll verificationPoll =
                new BinaryPoll(pollAuthor, pollId, pollName, pollQuestion);

        pollManager.onMessageReceived(message);
        verify(mockListener).onPollReceived(verificationPoll);
    }
}
