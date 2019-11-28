package com.example.kademlia;

import org.junit.Test;


import static org.junit.Assert.*;

public class ContactTest {
    KademliaId random = new KademliaId();
    SMSKademliaNode toTest = new SMSKademliaNode(random, 123 ); //crete a random id
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