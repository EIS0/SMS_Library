package com.eis0.easypoll;

import com.eis0.easypoll.poll.BinaryPoll;
import com.eis0.smslibrary.SMSPeer;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Test class for BinaryPoll.
 *
 * @author Matteo Carnelos
 */
public class BinaryPollTest {

    static final String VALID_POLL_NAME = "Name";
    static final String VALID_POLL_QUESTION = "Poll question?";
    static final SMSPeer VALID_PEER_1 = new SMSPeer("3401234567");
    static final SMSPeer VALID_PEER_2 = new SMSPeer("3401234568");
    static final SMSPeer VALID_PEER_3 = new SMSPeer("3401234569");
    static final int VALID_ID = 1;

    private static ArrayList<SMSPeer> testUsers = new ArrayList<>();

    /**
     * Setup for this test class, it populates testUsers list.
     *
     * @author Matteo Carnelos
     */
    @BeforeClass
    public static void setup() {
        testUsers.add(VALID_PEER_1);
        testUsers.add(VALID_PEER_2);
        testUsers.add(VALID_PEER_3);
    }

    /**
     * Checks if a valid poll is created.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void validPoll_isCreated() {
        BinaryPoll testPoll = new BinaryPoll(VALID_POLL_NAME, VALID_POLL_QUESTION, testUsers);
        assertTrue(testPoll.hasUser(VALID_PEER_1));
        assertTrue(testPoll.hasUser(VALID_PEER_2));
        assertTrue(testPoll.hasUser(VALID_PEER_3));
        assertEquals(VALID_POLL_NAME, testPoll.getName());
        assertEquals(VALID_POLL_QUESTION, testPoll.getQuestion());
    }

    /**
     * Checks that a poll with no user fail to create.
     *
     * @author Matteo Carnelos
     */
    @Test(expected = IllegalArgumentException.class)
    public void pollWithNoUsers_isNotCreated() {
        new BinaryPoll(VALID_POLL_NAME, VALID_POLL_QUESTION, new ArrayList<SMSPeer>());
        fail();
    }

    /**
     * Checks that a poll with no name fail to create.
     *
     * @author Matteo Carnelos
     */
    @Test(expected = IllegalArgumentException.class)
    public void pollWithNoName_isNotCreated() {
        new BinaryPoll("", VALID_POLL_QUESTION, testUsers);
        fail();
    }

    /**
     * Checks that a poll with no question fail to create.
     *
     * @author Matteo Carnelos
     */
    @Test(expected = IllegalArgumentException.class)
    public void pollWithNoQuestion_isNotCreated() {
        new BinaryPoll(VALID_POLL_NAME, "", testUsers);
        fail();
    }

    /**
     * Checks that a valid user is added correctly.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void validUser_isAdded() {
        testUsers.remove(VALID_PEER_3);
        BinaryPoll testPoll = new BinaryPoll(VALID_POLL_NAME, VALID_POLL_QUESTION, testUsers);
        testPoll.addUser(VALID_PEER_3);
        assertTrue(testPoll.hasUser(VALID_PEER_3));
    }

    /**
     * Tries to setAnswer in different ways to a poll and get the different results.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void validPoll_isClosed() {
        BinaryPoll testPoll = new BinaryPoll(VALID_POLL_NAME, VALID_POLL_QUESTION, testUsers);
        assertFalse(testPoll.isClosed());
        assertEquals(0, testPoll.getClosedPercentage());
        testPoll.setYes(VALID_PEER_1);
        testPoll.setNo(VALID_PEER_2);
        assertEquals(BinaryPoll.PollResult.YES.toString(), testPoll.getAnswer(VALID_PEER_1));
        assertEquals(BinaryPoll.PollResult.NO.toString(), testPoll.getAnswer(VALID_PEER_2));
        assertEquals(BinaryPoll.PollResult.UNAVAILABLE.toString(), testPoll.getAnswer(VALID_PEER_3));
        testPoll.setYes(VALID_PEER_3);
        assertEquals(2, testPoll.countYes());
        assertEquals(1, testPoll.countNo());
        assertEquals(100, testPoll.getClosedPercentage());
        assertTrue(testPoll.isClosed());
    }

    /**
     * Tests that two polls with same data are equals.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void equalValidPolls_areEquals() {
        BinaryPoll testPoll1 = new BinaryPoll(VALID_PEER_1, VALID_ID, VALID_POLL_NAME, VALID_POLL_QUESTION);
        BinaryPoll testPoll2 = new BinaryPoll(VALID_PEER_1, VALID_ID, VALID_POLL_NAME, VALID_POLL_QUESTION);
        assertEquals(testPoll1, testPoll2);
    }

    /**
     * Tests that two polls with different data are not equals.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void differentValidPolls_areNotEquals() {
        BinaryPoll testPoll1 = new BinaryPoll(VALID_PEER_1, VALID_ID + 1, VALID_POLL_NAME, VALID_POLL_QUESTION);
        BinaryPoll testPoll2 = new BinaryPoll(VALID_PEER_1, VALID_ID, VALID_POLL_NAME, VALID_POLL_QUESTION);
        assertNotEquals(testPoll1, testPoll2);
    }
}
