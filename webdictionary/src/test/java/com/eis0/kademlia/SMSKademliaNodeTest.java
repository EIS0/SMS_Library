package com.eis0.kademlia;

import com.eis0.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.*;

public class SMSKademliaNodeTest {

    /*Test initialization*/
    KademliaId random = new KademliaId();
    SMSNetDictionary dic = new SMSNetDictionary();
    SMSKademliaNode Node1 = new SMSKademliaNode(random, new SMSPeer("3497364511"), dic);

    @Test
    public void getNodeIdTest(){
        assertTrue(Node1.getNodeId().equals(random));
    }

    @Test
    public void equalsTest(){
        assertTrue(Node1.equals(Node1));
    }


}