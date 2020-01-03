package com.eis0.easypoll;

import com.eis0.easypoll.poll.BinaryPoll;
import com.eis0.networklibrary.Network;
import com.eis0.networklibrary.NetworksPool;
import com.eis0.smslibrary.SMSPeer;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link BinaryPoll}.
 *
 * @author Matteo Carnelos
 */
public class BinaryPollTest {

    static final String VALID_POLL_NAME = "Name";
    static final String VALID_POLL_QUESTION = "Poll question?";
    static final SMSPeer VALID_PEER_1 = new SMSPeer("3401234567");
    static final SMSPeer VALID_PEER_2 = new SMSPeer("3401234568");
    static final SMSPeer VALID_PEER_3 = new SMSPeer("3401234569");
    static final int VALID_NUMBER = 1;

    private static List<SMSPeer> testUsers = new ArrayList<>();
    private static Network testUsersNetwork;
    private static Network testAuthorNetwork;

    @BeforeClass
    public static void setup() {
        testUsers.add(VALID_PEER_1);
        testUsers.add(VALID_PEER_2);
        testUsers.add(VALID_PEER_3);
        testUsersNetwork = NetworksPool.obtainNetwork(testUsers);
        testAuthorNetwork = NetworksPool.obtainNetwork(VALID_PEER_1);
    }

    @Test
    public void validPoll_isCreated1() {
        BinaryPoll testPoll = new BinaryPoll(VALID_POLL_NAME, VALID_POLL_QUESTION, testUsersNetwork);
        assertEquals(VALID_POLL_NAME, testPoll.getName());
        assertEquals(VALID_POLL_QUESTION, testPoll.getQuestion());
        assertEquals(testUsersNetwork, testPoll.getUsers());
        assertFalse(testPoll.isClosed());
        assertFalse(testPoll.isIncoming());
        assertTrue(testPoll.getAuthor().isLocalNetwork());
        assertEquals(BinaryPoll.SELF_AUTHOR_NAME, testPoll.getAuthorName());
        assertEquals(3, testPoll.getUsersCount());
        assertEquals(testPoll.getNumber(), testPoll.getId());
    }

    @Test
    public void validPoll_isCreated2() {
        BinaryPoll testPoll = new BinaryPoll(VALID_NUMBER, VALID_POLL_NAME, VALID_POLL_QUESTION, testAuthorNetwork, testUsersNetwork);
        long expectedId = Long.parseLong(testAuthorNetwork.getAddresses().get(0) + VALID_NUMBER);
        assertEquals(VALID_POLL_NAME, testPoll.getName());
        assertEquals(VALID_POLL_QUESTION, testPoll.getQuestion());
        assertEquals(testUsersNetwork, testPoll.getUsers());
        assertFalse(testPoll.isClosed());
        assertTrue(testPoll.isIncoming());
        assertFalse(testPoll.getAuthor().isLocalNetwork());
        assertEquals(VALID_PEER_1.getAddress(), testPoll.getAuthorName());
        assertEquals(3, testPoll.getUsersCount());
        assertEquals(VALID_NUMBER, testPoll.getNumber());
        assertEquals(expectedId, testPoll.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void pollWithNoUsers_isNotCreated() {
        Network emptyNetwork = NetworksPool.obtainLocalNetwork();
        new BinaryPoll(VALID_POLL_NAME, VALID_POLL_QUESTION, emptyNetwork);
    }

    @Test(expected = IllegalArgumentException.class)
    public void pollWithNoName_isNotCreated() {
        new BinaryPoll("", VALID_POLL_QUESTION, testUsersNetwork);
    }

    @Test(expected = IllegalArgumentException.class)
    public void pollWithNoQuestion_isNotCreated() {
        new BinaryPoll(VALID_POLL_NAME, "", testUsersNetwork);
    }

    @Test
    public void validPoll_isClosed() {
        BinaryPoll testPoll = new BinaryPoll(VALID_POLL_NAME, VALID_POLL_QUESTION, testUsersNetwork);
        assertFalse(testPoll.isClosed());
        assertEquals(0, testPoll.getClosedPercentage());
        testPoll.setAnswer(true);
        testPoll.setAnswer(true);
        testPoll.setAnswer(false);
        assertEquals(2, testPoll.getYesCount());
        assertEquals(1, testPoll.getNoCount());
        assertEquals(100, testPoll.getClosedPercentage());
        assertTrue(testPoll.isClosed());
    }

    @Test
    public void equalValidPolls_areEquals() {
        BinaryPoll testPoll1 = new BinaryPoll(VALID_NUMBER, VALID_POLL_NAME, VALID_POLL_QUESTION, testAuthorNetwork, testUsersNetwork);
        BinaryPoll testPoll2 = new BinaryPoll(VALID_NUMBER, VALID_POLL_NAME, VALID_POLL_QUESTION, testAuthorNetwork, testUsersNetwork);
        assertEquals(testPoll1, testPoll2);
        assertEquals(testPoll1.getId(), testPoll2.getId());
    }

    @Test
    public void differentValidPolls_areNotEquals() {
        BinaryPoll testPoll1 = new BinaryPoll(VALID_NUMBER + 1, VALID_POLL_NAME, VALID_POLL_QUESTION, testAuthorNetwork, testUsersNetwork);
        BinaryPoll testPoll2 = new BinaryPoll(VALID_NUMBER, VALID_POLL_NAME, VALID_POLL_QUESTION, testAuthorNetwork, testUsersNetwork);
        assertNotEquals(testPoll1, testPoll2);
    }
}
