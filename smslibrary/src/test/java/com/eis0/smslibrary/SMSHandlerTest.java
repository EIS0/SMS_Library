package com.eis0.smslibrary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for SMSHandler.
 *
 * @author Matteo Carnelos
 */
public class SMSHandlerTest {

    /**
     * Checks if a message is sent correctly. The API call will launch a NullPointerException
     * (enabled in the build.gradle file), so I'm expecting that.
     *
     * @author Matteo Carnelos
     */
    @Test(expected = NullPointerException.class)
    public void message_isSent() {
        SMSPeer testPeer = new SMSPeer(SMSPeerTest.VALID_ADDR);
        SMSMessage testMessage = new SMSMessage(testPeer, SMSMessageTest.VALID_DATA);
        SMSHandler.sendMessage(testMessage, null, null);
        fail();
    }

    /**
     * Checks if the pending messages list is emptied when a listener is added.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void pendingMessages_isEmptied() {
        ReceivedMessageListener<SMSMessage> testListener = new ReceivedMessageListener<SMSMessage>() {
            @Override
            public void onMessageReceived(SMSMessage message) {}
        };
        SMSHandler.setReceiveListener(testListener);
        assertTrue(SMSHandler.isPendingMessagesEmpty());
    }
}
