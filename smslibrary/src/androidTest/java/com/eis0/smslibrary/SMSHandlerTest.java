package com.eis0.smslibrary;

import org.junit.Test;

import static org.junit.Assert.*;

public class SMSHandlerTest {

    @Test
    public void isPendingMessagesEmpty() {
        boolean result;
        SMSHandler.setReceiveListener(null);
        result = SMSHandler.isPendingMessagesEmpty();
        assertTrue(result);
    }

    @Test
    public void sendValidMessage_doesNotThrow(){
        SMSMessage message = new SMSMessage(new SMSPeer("3423541601"), "Message to send");
        SMSHandler.sendMessage(message, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void sendInvalidMessage_doesThrow(){
        SMSMessage message = new SMSMessage(new SMSPeer("jkasgk1"), "Message to send");
        SMSHandler.sendMessage(message, null, null);
    }


    @Test //test to Handle message (case smsListener null)
    public void testArrayList(){
        boolean isEmpty = true;
        boolean result;
        SMSHandler.setReceiveListener(null);
        result = SMSHandler.isPendingMessagesEmpty();
        assertEquals(isEmpty, result);
    }
}
