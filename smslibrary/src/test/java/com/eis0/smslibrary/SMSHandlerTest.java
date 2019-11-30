package com.eis0.smslibrary;

import android.content.Intent;
import android.telephony.SmsMessage;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for SMSHandler.
 *
 * @author Matteo Carnelos
 */
public class SMSHandlerTest {

    // PDU representing a message from "3401234567" containing "Hello"
    private static final byte[] VALID_PDU =
            {0, 32, 10, -127, 67, 16, 50, 84, 118, 0, 0, -111, 17,
                    3, -127, -110, 116, 72, 5, -56, 50, -101, -3, 6};

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
     * Checks if a listener is not called when receiving a message without the APP_ID.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void messageWithoutAppId_isNotHandled() {
        ReceivedMessageListener mockListener = mock(ReceivedMessageListener.class);
        SMSHandler.setReceiveListener(mockListener);
        SmsMessage testMessage = SmsMessage.createFromPdu(VALID_PDU);
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
