package com.eis0.easypoll;

import com.eis0.easypoll.poll.BinaryPoll;
import com.eis0.easypoll.poll.PollManager;
import com.eis0.networklibrary.Network;
import com.eis0.networklibrary.NetworksPool;
import com.eis0.smslibrary.SMSPeer;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.eis0.easypoll.BinaryPollTest.VALID_ID;
import static com.eis0.easypoll.BinaryPollTest.VALID_PEER_1;
import static com.eis0.easypoll.BinaryPollTest.VALID_PEER_2;
import static com.eis0.easypoll.BinaryPollTest.VALID_PEER_3;
import static com.eis0.easypoll.BinaryPollTest.VALID_POLL_NAME;
import static com.eis0.easypoll.BinaryPollTest.VALID_POLL_QUESTION;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link PollManager}.
 *
 * @author Matteo Carnelos
 */
public class PollManagerTest {

    private static List<SMSPeer> testUsers = new ArrayList<>();
    private static Network testUsersNetwork;
    private static Network testAuthorsNetwork;
    private static PollManager pollManager;

    @BeforeClass
    public static void setup() {
        pollManager = PollManager.getInstance();
        testAuthorsNetwork = NetworksPool.obtainNetwork(VALID_PEER_1);
        testUsers.add(VALID_PEER_1);
        testUsers.add(VALID_PEER_2);
        testUsers.add(VALID_PEER_3);
        testUsersNetwork = NetworksPool.obtainNetwork(testUsers);
        assertNotNull(pollManager);
    }

    @Test(expected = NullPointerException.class)
    public void validPoll_isCreated() {
        pollManager.createPoll(VALID_POLL_NAME, VALID_POLL_QUESTION, testUsers);
    }

    @Test(expected = NullPointerException.class)
    public void validPoll_isAnswered() {
        BinaryPoll testPoll = new BinaryPoll(VALID_ID, VALID_POLL_NAME, VALID_POLL_QUESTION, testAuthorsNetwork, testUsersNetwork);
        pollManager.answerPoll(testPoll, true);
    }
}
