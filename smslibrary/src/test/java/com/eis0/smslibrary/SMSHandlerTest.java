package com.eis0.smslibrary;

import org.junit.Test;

import static org.junit.Assert.*;

public class SMSHandlerTest {

    @Test
    public void simpleMessage_isSent() {
        SMSPeer testPeer = new SMSPeer("3401230280");
        SMSMessage testMessage = new SMSMessage(testPeer, "Ciao");
        SMSHandler.sendMessage(testMessage, null, null);
    }

    @Test
    public void isPendingMessagesEmpty() {
        boolean result;
        SMSHandler.setReceiveListener(null);
        result = SMSHandler.isPendingMessagesEmpty();
        assertEquals(result, true);
    }
}
