package com.eis0.kademlia;

import com.eis.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ContactTest {
    private final SMSPeer peer = new SMSPeer("+393497364511");
    private final SMSKademliaNode toTest = new SMSKademliaNode(peer);
    private final Contact test = new Contact(toTest);

    @Test
    public void getNodeTest(){
      assertEquals(test.getNode(), toTest);
    }

    @Test
    public void equalsTest(){
        assertEquals(test, test);
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

    @Test
    public void compareToTest(){
        assertEquals(test.compareTo(test), 0);
    }

    @Test
    public void hashCodeTest(){
        int expected = 1889745535;
        assertEquals(test.hashCode(), expected);
    }
}