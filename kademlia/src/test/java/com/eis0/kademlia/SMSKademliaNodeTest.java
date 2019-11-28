package com.eis0.kademlia;

import com.eis0.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.*;

public class SMSKademliaNodeTest {

    /*Test initialization*/
    KademliaId random = new KademliaId();
    SMSKademliaNode Node1 = new SMSKademliaNode(random, 123,
            new SMSPeer("3497364511"));

    @Test
    public void getNodeIdTest(){
        assertTrue(Node1.getNodeId().equals(random));
    }

    @Test
    public void equalsTest(){
        assertTrue(Node1.equals(Node1));
    }


}