package com.eis0.kademlia;

import com.eis.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ContactTest {
    private SMSPeer peer = new SMSPeer("+393497364511");
    private SMSKademliaNode toTest = new SMSKademliaNode(peer);
    private Contact test = new Contact(toTest);

    @Test
    public void getNodeTest(){
      assertTrue(test.getNode().equals(toTest));
    }

    @Test
    public void equalsTest(){
        assertTrue(test.equals(test));
    }

    @Test
    public void resetStaleCountTest(){
        test.resetStaleCount();
        assertEquals(test.staleCount(), 0);
    }

    @Test
    public void incrementStaleCountTest(){
        test.incrementStaleCount();
        assertEquals(test.staleCount(), 1);
    }

    @Test
    public void lastSeenTest(){
        assertEquals(test.lastSeen(), System.currentTimeMillis()/1000L);
    }

    @Test
    public void setSeenNowTest(){
        test.setSeenNow();
        assertEquals(test.lastSeen(),System.currentTimeMillis()/1000L );
    }

}