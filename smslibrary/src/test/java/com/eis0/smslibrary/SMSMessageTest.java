package com.eis0.smslibrary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for SMSMessage.
 *
 * @author Matteo Carnelos
 */
public class SMSMessageTest {

    private static final String VALID_DATA = "Hello";
    private static final String EMPTY_DATA = "";

    /**
     * Checks that a valid message is created correctly.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void validMessage_isCreated() {
        SMSPeer testPeer = new SMSPeer(SMSPeerTest.VALID_ADDR);
        SMSMessage testMessage = new SMSMessage(testPeer, VALID_DATA);
        assertEquals(testPeer, testMessage.getPeer());
        assertEquals(VALID_DATA, testMessage.getData());
    }

    /**
     * Tests if the creation of an empty-body SMSMessage fails.
     *
     * @author Matteo Carnelos
     */
    @Test(expected = IllegalArgumentException.class)
    public void emptyMessage_isNotCreated() {
        SMSPeer testPeer = new SMSPeer(SMSPeerTest.VALID_ADDR);
        new SMSMessage(testPeer, EMPTY_DATA);
        fail();
    }

    /**
     * Tests if the creation of an SMSMessage with more than 160 characters fails.
     *
     * @author Matteo Carnelos
     */
    @Test(expected = IllegalArgumentException.class)
    public void longMessage_isNotCreated() {
        SMSPeer testPeer = new SMSPeer(SMSPeerTest.VALID_ADDR);
        StringBuilder testBody = new StringBuilder();
        for (int i = 0; i <= SMSMessage.MAX_MESSAGE_LENGTH; i++) testBody.append("A");
        new SMSMessage(testPeer, testBody.toString());
        fail();
    }
}
