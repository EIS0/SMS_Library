package com.eis0.KademliaNetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConnectMessageTest {

    SMSPeer peer = new SMSPeer("3408140326");
    KademliaId ID = new KademliaId(peer);
    SMSNetDictionary dic = new SMSNetDictionary();
    SMSKademliaNode Node = new SMSKademliaNode(ID, peer, dic);
    ConnectMessage test = new ConnectMessage(Node);

    @Test
    public void getNode() {
        assertTrue(test.getNode().equals(Node));
    }

    @Test
    public void getPeer() {
        assertTrue(test.getPeer().equals(peer));
    }

    @Test
    public void getData() {
        String ShouldBe = "108877ECC3A9B2C286E5CD813119910F6DF43EC4";
        assertEquals(test.getData(),ShouldBe);
    }

    @Test
    public void code() {
        byte ShouldBe = 0x02;
        assertEquals(test.code(), ShouldBe);
    }

}