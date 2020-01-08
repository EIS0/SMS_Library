package com.eis0.kademlia;

import com.eis.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ContactTest {
    private final SMSPeer VALID_PEER1 = new SMSPeer("+393497364511");
    private final SMSPeer VALID_PEER2 = new SMSPeer("+393479281192");
    private final SMSKademliaNode VALID_NODE1 = new SMSKademliaNode(VALID_PEER1);
    private final SMSKademliaNode VALID_NODE2 = new SMSKademliaNode(VALID_PEER2);
    private final Contact VALID_CONTACT1 = new Contact(VALID_NODE1);
    private final Contact VALID_CONTACT2 = new Contact(VALID_NODE1);
    private final Contact VALID_CONTACT3 = new Contact(VALID_NODE2);
    private final String RANDOM_OBJECT = new String("Random");

    @Test
    public void getNodeTest(){
      assertEquals(VALID_CONTACT1.getNode(), VALID_NODE1);
    }

    @Test
    public void equalsTest_equality(){
        boolean equals = VALID_CONTACT1.equals(VALID_CONTACT2);
        assertTrue(equals);
    }

    @Test
    public void equalsTest_inequality() {
        boolean equals = VALID_CONTACT1.equals(VALID_CONTACT3);
        assertFalse(equals);
    }

    @Test
    public void equalsTest_notInstaceOf() {
        boolean equals = VALID_CONTACT1.equals(RANDOM_OBJECT);
        assertFalse(equals);
    }
    @Test
    public void resetStaleCountTest(){
        VALID_CONTACT1.resetStaleCount();
        assertEquals(VALID_CONTACT1.staleCount(), 0);
    }

    @Test
    public void incrementStaleCountTest(){
        VALID_CONTACT1.incrementStaleCount();
        assertEquals(VALID_CONTACT1.staleCount(), 1);
    }

    @Test
    public void lastSeenTest(){
        assertEquals(VALID_CONTACT1.lastSeen(), System.currentTimeMillis()/1000L);
    }

    @Test
    public void setSeenNowTest(){
        VALID_CONTACT1.setSeenNow();
        assertEquals(VALID_CONTACT1.lastSeen(),System.currentTimeMillis()/1000L );
    }

    @Test
    public void compareToTest(){
        assertEquals(VALID_CONTACT1.compareTo(VALID_CONTACT1), 0);
    }

    @Test
    public void hashCodeTest(){
        int expected = 1889745535;
        assertEquals(VALID_CONTACT1.hashCode(), expected);
    }
}