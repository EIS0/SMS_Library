package com.eis0.smslibrary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SMSPeer_Tests {
    @Test
    public void checkEqualPeers() {
        SMSPeer peer1 = new SMSPeer("12345");
        SMSPeer peer2 = new SMSPeer("12345");
        assertEquals(peer1, peer2);
    }
}