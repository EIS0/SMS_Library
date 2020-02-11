package com.eis0.kademlia;

import com.eis.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

public class SMSKademliaNodeTest {

    private final SMSPeer PEER1 = new SMSPeer("+5554");
    private final SMSPeer PEER2 = new SMSPeer("+5556");
    private final KademliaId RANDOM_ID1 = new KademliaId();
    private final SMSKademliaNode NODE1 = new SMSKademliaNode(PEER1);
    private final SMSKademliaNode NODE1_V2 = new SMSKademliaNode(PEER1);
    private final SMSKademliaNode NODE2 = new SMSKademliaNode(PEER2);
<<<<<<< HEAD
=======
    private final KademliaId KAD_ID = new KademliaId("12345678");
    private final KademliaId KAD_ID2 = new KademliaId("11223344");
    private final SMSKademliaNode NODE_BY_ID = new SMSKademliaNode(PEER1);
    private final SMSKademliaNode NODE_BY_ID_V2 = new SMSKademliaNode(PEER1);
    private final SMSKademliaNode NODE_BY_ID2 = new SMSKademliaNode(PEER2);
>>>>>>> 6acf762ace63bb2f1f879e68c606b7d6aac9d1c0

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
    public void hasCodeTest() {
        int expected = -1199924090;
        assertEquals(NODE1.hashCode(), expected);
    }

    @Test
    public void toStringTest(){
        String expected = "8B9D0EAE850B323A";
        assertEquals(NODE1.toString(), expected);
    }
}