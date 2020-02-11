package com.eis0.kademlianetwork.informationdeliverymanager;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;

import org.junit.Test;

import static org.junit.Assert.*;

public class FindIdRequestTest {

    private final SMSPeer peer = new SMSPeer("+12345678");
    private final KademliaId id = new KademliaId(peer);
    private final FindIdRequest findIdRequest = new FindIdRequest(id);

    private final SMSPeer peer2 = new SMSPeer("+4567890");
    private final KademliaId id2 = new KademliaId(peer2);
    private final FindIdRequest findIdRequest2 = new FindIdRequest(id2);

    @Test
    public void isCompleted() {
        findIdRequest.setCompleted(peer); //2 test in 1. Not so elegant but necessary
        assertTrue(findIdRequest.isCompleted());
    }

    @Test
    public void getPeerFound() {
        assertNull(findIdRequest.getPeerFound());
    }

    @Test
    public void equals_true() {
        assertTrue(findIdRequest.equals(new FindIdRequest(id)));
    }

    @Test
    public void equals_false() {
        assertFalse(findIdRequest.equals(findIdRequest2));
    }
    @Test
    public void hashCode1() {
        int expected = 1154726495;
        assertEquals(findIdRequest.hashCode(), expected);
    }
}