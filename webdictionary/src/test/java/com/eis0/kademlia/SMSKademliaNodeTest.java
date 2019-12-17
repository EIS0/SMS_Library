package com.eis0.kademlia;

import com.eis.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class SMSKademliaNodeTest {

    private final SMSPeer PEER1 = new SMSPeer("5554");
    private final SMSPeer PEER2 = new SMSPeer("5556");
    private final KademliaId RANDOM_ID1 = new KademliaId();
    private final SMSKademliaNode NODE1 = new SMSKademliaNode(PEER1);
    private final SMSKademliaNode NODE1_V2 = new SMSKademliaNode(PEER1);
    private final SMSKademliaNode NODE2 = new SMSKademliaNode(PEER2);
    private final KademliaId KAD_ID = new KademliaId("12345678");
    private final KademliaId KAD_ID2 = new KademliaId("11223344");
    private final SMSKademliaNode NODE_BY_ID = new SMSKademliaNode(KAD_ID);
    private final SMSKademliaNode NODE_BY_ID_V2 = new SMSKademliaNode(KAD_ID);
    private final SMSKademliaNode NODE_BY_ID2 = new SMSKademliaNode(KAD_ID2);

    @Test
    public void getNodeIdTest() {
        assertNotEquals(NODE1.getId(), RANDOM_ID1);
    }

    @Test
    public void equalsTest_sameIsEqual() {
        assertEquals(NODE1, NODE1);
    }

    @Test
    public void equalsTest_differentAreDifferent() {
        assertNotEquals(NODE1, NODE2);
    }

    @Test
    public void equalsTest_differentAreEqual() {
        assertEquals(NODE1, NODE1_V2);
    }

    @Test
    public void getPeer_returnCorrect() {
        assertEquals(PEER1, NODE1.getPeer());
    }

    @Test
    public void getPeer_doNotReturnWrong() {
        assertNotEquals(PEER2, NODE1.getPeer());
    }

    @Test
    public void nodeById_getNodeIdTest() {
        assertEquals(NODE_BY_ID.getId(), KAD_ID);
    }

    @Test
    public void nodeById_equalsTest_sameIsEqual() {
        assertEquals(NODE_BY_ID, NODE_BY_ID);
    }

    @Test
    public void nodeById_equalsTest_differentAreDifferent() {
        assertNotEquals(NODE_BY_ID, NODE_BY_ID2);
    }

    @Test
    public void nodeById_equalsTest_differentAreEqual() {
        assertEquals(NODE_BY_ID, NODE_BY_ID_V2);
    }

    @Test
    public void nodeById_getPeer_returnNull() {
        assertNull(NODE_BY_ID.getPeer());
    }
}