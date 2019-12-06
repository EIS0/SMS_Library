package com.eis0.kademlia;

import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SMSNetVocabulary;

import org.junit.Test;


import static org.junit.Assert.*;

public class ContactTest {

    KademliaId random = new KademliaId(); //create a random Id
    SMSNetVocabulary dic = new SMSNetVocabulary();
    SMSKademliaNode toTest = new SMSKademliaNode(random,
            new SMSPeer("3497364511"), dic);
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

    @Test
    public void lastSeenTest(){
        assertEquals(test.lastSeen(), System.currentTimeMillis()/1000L);
    }

    @Test
    public void setSeenNowTest(){
        test.setSeenNow();
        assertEquals(test.lastSeen(),System.currentTimeMillis()/1000L );
    }

}