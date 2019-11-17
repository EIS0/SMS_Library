package com.eis0.library_demo;

import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;

import static org.mockito.Mockito.times;
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

    /**
     * Tests whether a new poll is created correctly and passed to listeners.
     * @author Giovanni Velludo
     */
    @Test
    public void createPoll() {
        String pollName = "Pizza";
        String pollQuestion = "Should we get takeout pizza for dinner?";
        ArrayList<SMSPeer> pollUsers = new ArrayList<>(1);
        pollUsers.add(new SMSPeer("3498257155"));
        pollUsers.add(new SMSPeer("3428475620"));
        pollUsers.add(new SMSPeer("3422222222"));
        pollUsers.add(new SMSPeer("3339999000"));

        /* Creates a poll with the above arguments and captures the poll given by PollManager to
         * listeners.
         */
        ArgumentCaptor pollCapture = ArgumentCaptor.forClass(TernaryPoll.class);
        try {
            pollManager.createPoll(pollName, pollQuestion, pollUsers);
        }
        catch (NullPointerException e) {
            /* exception launched because PollManager tries to send the poll with an SMS to all
             * pollUsers, but this is not possible in unit tests
             */
            verify(mockListener).onSentPollUpdate((TernaryPoll) pollCapture.capture());
        }
        TernaryPoll verificationPoll = (TernaryPoll) pollCapture.getValue();
        assertEquals(verificationPoll.getPollName(), pollName);
        assertEquals(verificationPoll.getPollQuestion(), pollQuestion);
        HashSet<SMSPeer> pollUsersSet = new HashSet<>();
        for (SMSPeer user : pollUsers) pollUsersSet.add(user);
        assertEquals(verificationPoll.getPollUsers(), pollUsersSet);
    }

    @Test
    public void answerPoll() {
    }

    /**
     * Tests handling of a message containing a new poll.
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
     * Tests the handling of a message containing an answer to a poll created by the current user.
     * The poll must first be created.
     * @author Giovanni Velludo
     */
    @Test
    public void onMessageContainingAnswerReceived() {
        int messageCode = Integer.parseInt(PollManager.ANSWER_MSG_CODE);
        String pollName = "Pizza";
        String pollQuestion = "Should we get takeout pizza for dinner?";
        ArrayList<SMSPeer> pollUsers = new ArrayList<>(1);
        SMSPeer voter = new SMSPeer("3498257155");
        pollUsers.add(voter);
        int answer = 0;

        /* Creates a poll with the above arguments and captures the poll given by PollManager to
         * listeners.
         */
        ArgumentCaptor pollCapture = ArgumentCaptor.forClass(TernaryPoll.class);
        try {
            pollManager.createPoll(pollName, pollQuestion, pollUsers);
        }
        catch (NullPointerException e) {
            /* exception launched because PollManager tries to send the poll with an SMS to all
             * pollUsers, but this is not possible in unit tests
             */
            verify(mockListener).onSentPollUpdate((TernaryPoll) pollCapture.capture());
        }
        TernaryPoll verificationPoll = (TernaryPoll) pollCapture.getValue();

        String text = messageCode + sep + verificationPoll.getPollId() + sep + answer;
        SMSMessage message = new SMSMessage(voter, text);

        if (answer == 1) verificationPoll.setYes(voter);
        else verificationPoll.setNo(voter);

        pollManager.onMessageReceived(message);
        // verifies that PollManager has correctly updated the poll by registering the new vote
        verify(mockListener, times(2)).onSentPollUpdate(verificationPoll);
    }
}