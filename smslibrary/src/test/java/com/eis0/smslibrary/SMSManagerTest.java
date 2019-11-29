package com.eis0.smslibrary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for SMSManager.
 *
 * @author Matteo Carnelos
 */
public class SMSManagerTest {

    /**
     * Checks if an instance is created correctly.
     *
     * @author Matteo Carnelos
     */
    @Test
    public void instance_isObtained() {
        assertNotNull(SMSManager.getInstance());
    }

    /**
     * Checks if a message is sent correctly. The API call will launch a NullPointerException
     * (enabled in the build.gradle file), so I'm expecting that.
     *
     * @author Matteo Carnelos
     */
    @Test(expected = NullPointerException.class)
    public void message_isSent() {
        SMSPeer testPeer = new SMSPeer("0123456789");
        SMSMessage testMessage = new SMSMessage(testPeer, "Hello");
        SMSManager.getInstance().sendMessage(testMessage);
    }
}
