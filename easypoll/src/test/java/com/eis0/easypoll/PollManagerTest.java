package com.eis0.easypoll;

import com.eis0.easypoll.poll.BinaryPoll;
import com.eis0.easypoll.poll.PollManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static com.eis0.easypoll.BinaryPollTest.VALID_ID;
import static com.eis0.easypoll.BinaryPollTest.VALID_PEER_1;
import static com.eis0.easypoll.BinaryPollTest.VALID_PEER_2;
import static com.eis0.easypoll.BinaryPollTest.VALID_PEER_3;
import static com.eis0.easypoll.BinaryPollTest.VALID_POLL_NAME;
import static com.eis0.easypoll.BinaryPollTest.VALID_POLL_QUESTION;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

/**
 * Test class for PollManager.
 *
 * @author Giovanni Velludo
 * @author Matteo Carnelos
 */
@RunWith(MockitoJUnitRunner.class)
public class PollManagerTest {

    private static final String SEP = PollManager.FIELD_SEPARATOR;

    private static ArrayList<SMSPeer> testUsers = new ArrayList<>();
    private static PollManager pollManager;
    @Mock PollListener mockListener;

    /**
     * Setup for this test class, it instantiates the poll manager and populates testUsers list.
     *
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    @BeforeClass
    public static void setup() {
        pollManager = PollManager.getInstance();
        testUsers.add(VALID_PEER_1);
        testUsers.add(VALID_PEER_2);
        testUsers.add(VALID_PEER_3);
        assertNotNull(pollManager);
    }

    /**
     * Tries to create a valid poll. Check if the sms message is sent.
     *
     * @author Matteo Carnelos
     */
    @Test(expected = NullPointerException.class)
    public void validPoll_isCreated() {
        pollManager.createPoll(VALID_POLL_NAME, VALID_POLL_QUESTION, testUsers);
        fail();
    }

    /**
     * Checks that a valid poll is answered by checking that a sms is sent.
     *
     * @author Matteo Carnelos
     */
    @Test(expected = NullPointerException.class)
    public void validPoll_isAnswered() {
        BinaryPoll testPoll = new BinaryPoll(VALID_PEER_1, VALID_ID, VALID_POLL_NAME, VALID_POLL_QUESTION);
        pollManager.answerPoll(testPoll, true);
        fail();
    }

    /**
     * Checks that an owned poll is not answered.
     *
     * @author Matteo Carnelos
     */
    @Test(expected = IllegalArgumentException.class)
    public void ownedPoll_isNotAnswered() {
        BinaryPoll testPoll = new BinaryPoll(VALID_POLL_NAME, VALID_POLL_QUESTION, testUsers);
        pollManager.answerPoll(testPoll, true);
        fail();
    }

    /**
     * Tests handling of messages received by the application.
     *
     * @author Giovanni Velludo
     */
    @Test
    public void validPoll_isReceived() {
        // CASE 0: received new poll
        pollManager.setPollListener(mockListener);
        String messageCode = PollManager.NEW_POLL_MSG_CODE;

        String text = messageCode + SEP + VALID_ID + SEP + VALID_POLL_NAME + SEP + VALID_POLL_QUESTION;
        SMSMessage message = new SMSMessage(VALID_PEER_1, text);

        BinaryPoll verificationPoll =
                new BinaryPoll(VALID_PEER_1, VALID_ID, VALID_POLL_NAME, VALID_POLL_QUESTION);

        pollManager.onMessageReceived(message);
        verify(mockListener).onPollReceived(verificationPoll);
    }
}
