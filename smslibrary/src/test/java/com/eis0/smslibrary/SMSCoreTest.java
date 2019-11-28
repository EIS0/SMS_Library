package com.eis0.smslibrary;

import org.junit.Test;

public class SMSCoreTest {

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
        SMSCore.sendMessage(testMessage, null, null);
    }
}
