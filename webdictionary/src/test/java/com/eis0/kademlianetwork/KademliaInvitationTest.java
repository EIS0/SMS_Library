package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.*;

public class KademliaInvitationTest {

     private final SMSPeer PEER = new SMSPeer("1234567");
     private final KademliaInvitation INVITATION = new KademliaInvitation(PEER);

    @Test
    public void getInviterPeer() {
         assertEquals(INVITATION.getInviterPeer(), PEER);
    }
}