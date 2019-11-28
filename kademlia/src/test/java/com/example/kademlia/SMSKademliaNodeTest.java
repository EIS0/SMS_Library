package com.example.kademlia;

import org.junit.Test;

import static org.junit.Assert.*;

public class SMSKademliaNodeTest {

    /*Test initialization*/
    KademliaId random = new KademliaId();
    SMSKademliaNode Node1 = new SMSKademliaNode(random, 123);

    @Test
    public void getNodeIdTest(){
        assertTrue(Node1.getNodeId().equals(random));
    }

    @Test
    public void equalsTest(){
        assertTrue(Node1.equals(Node1));
    }


}