package com.eis0.kademlia;

import com.eis0.smslibrary.SMSPeer;

import org.junit.Test;


import static org.junit.Assert.*;

public class ContactTest {
    KademliaId random = new KademliaId(); //create a random Id
    SMSKademliaNode toTest = new SMSKademliaNode(random, 123,
            new SMSPeer("3497364511"));
    Contact test = new Contact(toTest);

    @Test
    public void getNodeTest(){
      assertTrue(test.getNode().equals(toTest));
    }

    @Test
    public void equalsTest(){
        assertTrue(test.equals(test));
    }

    @Test
    public void resetStaleCountTest(){
        test.resetStaleCount();
        assertEquals(test.staleCount(), 0);
    }

    @Test
    public void incrementStaleCountTest(){
        test.incrementStaleCount();
        assertEquals(test.staleCount(), 1);
    }

}