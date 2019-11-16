package com.eis0.library_demo;

import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PollManagerTest {
    private static PollManager pollManager;
    private static PollListener mockListener;
    private String sep = PollManager.FIELD_SEPARATOR;

    @BeforeClass
    public static void classSetUp() {
        pollManager = PollManager.getInstance();
        mockListener = mock(PollListener.class);
        pollManager.setPollListener(mockListener);
    }

    @Test
    public void createPoll() {
    }

    @Test
    public void answerPoll() {
    }

    /**
     * Tests handling of message containing a new poll.
     * @author Giovanni Velludo
     */
    @Test
    public void onMessageContainingNewPollReceived() {
        int messageCode = Integer.parseInt(PollManager.NEW_POLL_MSG_CODE);
        SMSPeer pollAuthor = new SMSPeer("3337235485");
        int pollId = 38;
        String pollName = "Pizza";
        String pollQuestion = "Should we get takeout pizza for dinner?";

        String text = messageCode + sep + pollId + sep + pollName + sep + pollQuestion;
        SMSMessage message = new SMSMessage(pollAuthor, text);

        TernaryPoll verificationPoll =
                new TernaryPoll(pollAuthor, pollId, pollName, pollQuestion);

        pollManager.onMessageReceived(message);
        verify(mockListener).onPollReceived(verificationPoll);
    }

    /**
     * Creates a new poll with only one user and simulates an answer from the user.
     * @author Giovanni Velludo
     */
    @Test
    public void onMessageContainingAnswerReceived() {
        int messageCode = Integer.parseInt(PollManager.ANSWER_MSG_CODE);
        SMSPeer pollAuthor = new SMSPeer("3337235485");
        String pollName = "Pizza";
        String pollQuestion = "Should we get takeout pizza for dinner?";
        ArrayList<SMSPeer> pollUsers = new ArrayList<>(1);
        SMSPeer voter = new SMSPeer("3498257155");
        pollUsers.add(voter);

        ArgumentCaptor valueCapture = ArgumentCaptor.forClass(TernaryPoll.class);
        pollManager.createPoll(pollName, pollQuestion, pollUsers);
        doNothing().when(mockListener).onSentPollUpdate((TernaryPoll) valueCapture.capture());
        TernaryPoll createdPoll = (TernaryPoll) valueCapture.getValue();

        TernaryPoll verificationPoll =
                new TernaryPoll(pollAuthor, createdPoll.getPollId(), pollName, pollQuestion);
        // TODO: act based on value of answer
        verificationPoll.setYes(voter);

        int answer = 1;
        String text = messageCode + sep + createdPoll.getPollId() + sep + answer;
        SMSMessage message = new SMSMessage(voter, text);

        pollManager.onMessageReceived(message);
        verify(mockListener).onSentPollUpdate(verificationPoll);
    }
}