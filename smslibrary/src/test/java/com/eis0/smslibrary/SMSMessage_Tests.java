package com.eis0.smslibrary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SMSMessage_Tests {

    private SMSMessage validMessage;

    @Before
    public void SMSMessagesSetup() {
        validMessage = new SMSMessage(new SMSPeer("5554"), "a given message");
    }

    @Test
    public void checkPeer_equals(){
        assertEquals(validMessage.getPeer(), new SMSPeer("5554"));
    }

    @Test
    public void checkPeer_notEquals(){
        assertNotEquals(validMessage.getPeer(), new SMSPeer("1234"));
    }

    @Test
    public void checkData_equals(){
        assertEquals(validMessage.getData(), "a given message");
    }

    @Test
    public void checkData_notEquals(){
        assertNotEquals(validMessage.getData(), "a different message");
    }

    @Test
    public void toStringAnSMSMessage(){
        assertEquals(validMessage.toString(), "SMSPeer: 5554, SMSMessage: a given message");
    }
}
