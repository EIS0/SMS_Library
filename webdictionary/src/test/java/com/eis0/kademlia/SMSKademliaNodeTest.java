package com.eis0.kademlia;

import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SMSNetVocabulary;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SMSKademliaNodeTest {

    /*Test initialization*/

    private final KademliaId RANDOM_ID1 = new KademliaId();
    private final SMSKademliaNode NODE1 =
            new SMSKademliaNode(new SMSPeer("3497364511"));
    private final SMSKademliaNode NODE1_V2 =
            new SMSKademliaNode(new SMSPeer("3497364511"));
    private final SMSKademliaNode NODE2 =
            new SMSKademliaNode(new SMSPeer("3497364511"));


    @Test
    public void getNodeIdTest(){
        assertEquals(NODE1.getId(), RANDOM_ID1);
    }

    @Test
    public void equalsTest_sameIsEqual(){
        assertEquals(NODE1, NODE1);
    }

    @Test
    public void equalsTest_differentAreDifferent(){
        assertNotEquals(NODE1, NODE2);
    }

    @Test
    public void equalsTest_differentAreEqual(){
        assertEquals(NODE1, NODE1_V2);
    }

}