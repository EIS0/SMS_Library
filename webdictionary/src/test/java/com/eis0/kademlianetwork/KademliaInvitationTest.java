package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.*;

public class KademliaInvitationTest {

    private final SMSPeer PEER1 = new SMSPeer("+393423541601");
    private final SMSPeer PEER2 = new SMSPeer("+393423541602");
    private final KademliaInvitation INVITATION1 = new KademliaInvitation(PEER1);
    private final KademliaInvitation INVITATION2 = new KademliaInvitation(PEER2);

    @Test
    public void getInviterPeer() {
         assertEquals(INVITATION1.getInviterPeer(), PEER1);
    }

    @Test
    public void isEqual_sameObject_isEqual(){
        assertEquals(INVITATION1, INVITATION1);
    }

    @Test
    public void isEqual_differentInvitations_areDifferent(){
        assertNotEquals(INVITATION1, INVITATION2);
    }

    @Test
    public void isEqual_differentObjects_areDifferent(){
        assertNotEquals(INVITATION1, "a different object");
    }

    @Test
    public void isEqual_nullObject_isDifferent(){
        assertNotEquals(INVITATION1, null);
    }
}