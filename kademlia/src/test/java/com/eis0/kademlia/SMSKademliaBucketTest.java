package com.eis0.kademlia;

import com.eis0.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.*;

public class SMSKademliaBucketTest {
    KadConfiguration config = new DefaultConfiguration();
    KademliaId ID = new KademliaId("00000000000000000001");
    KademliaId ID2 = new KademliaId("00000000000000000011");
    SMSKademliaNode test2 = new SMSKademliaNode(ID2, 123, new SMSPeer("3497364511"));
    SMSKademliaNode test = new SMSKademliaNode(ID, 123, new SMSPeer("3497312345"));
    SMSKademliaRoutingTable routingTable = new SMSKademliaRoutingTable(test, config);
    /*configuration test*/
    SMSKademliaBucket toTest = new SMSKademliaBucket(5, config);

    @Test
    public void insertTest(){
        Contact c = new Contact(test);
        toTest.insert(c);
        assertTrue(toTest.containsContact(c));
    }

    @Test
    public void containsContactTest(){
        Contact c = new Contact(test);
        assertFalse(toTest.containsContact(c));
    }

    @Test
    public void containsNodeTest(){
        assertFalse(toTest.containsNode(test));
    }

    @Test
    public void removeContactTest(){
        Contact c = new Contact((test));
        toTest.insert(c);
        assertTrue(toTest.removeContact(c));
    }

    @Test
    public void getFromContactTest(){
        Contact c = new Contact(test);
        toTest.insert(c);
        Contact toReturn = toTest.getFromContacts(test);
        assertTrue(toReturn.equals(c));
    }

    @Test
    public void removeFromContactTest(){
        Contact c = new Contact(test);
        toTest.insert(c);
        Contact toReturn = toTest.removeFromContacts(test);
        assertFalse(toTest.containsNode(test));
    }

    @Test
    public void removeNodeTest(){
        Contact c = new Contact(test);
        toTest.insert(c);
        assertTrue(toTest.removeNode(test));
    }
}