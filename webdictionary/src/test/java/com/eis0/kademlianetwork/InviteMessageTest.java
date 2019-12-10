package com.eis0.kademlianetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SMSNetVocabulary;

import org.junit.Test;

import static org.junit.Assert.*;

public class InviteMessageTest {

    SMSPeer peer = new SMSPeer("3408140326");
    KademliaId ID = new KademliaId(peer);
    SMSNetVocabulary dic = new SMSNetVocabulary();
    SMSKademliaNode from = new SMSKademliaNode(ID, peer, dic);
    InviteMessage test = new InviteMessage(from);

    /*Start testing*/
    @Test
    public void getPeer() { assertTrue(test.getPeer().equals(peer)); }

    @Test
    public void getCode() {
        byte shouldBe = 0x01;
        assertEquals(test.getCode(), shouldBe);
    }

    @Test
    public void getData() {
        String ShouldBe = "Node to connect";
        assertEquals(test.getData(), ShouldBe);
    }

}