package com.eis0.kademlia;

import android.content.Context;

import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SMSNetDictionary;

import org.junit.Test;

import static org.junit.Assert.*;

public class SMSKademliaBucketTest {
    KadConfiguration config = new DefaultConfiguration();
    /*Crating some ID*/
    KademliaId ID = new KademliaId("00000000000000000001");
    KademliaId ID2 = new KademliaId("00000000000000000011");
    KademliaId ID3 = new KademliaId();
    SMSNetDictionary dic = new SMSNetDictionary(); //creating a dictionary
    /*Creating some Nodes*/
    SMSKademliaNode test3 = new SMSKademliaNode(ID3, new SMSPeer("3408140326"), dic);
    SMSKademliaNode test2 = new SMSKademliaNode(ID2, new SMSPeer("3497364511"), dic);
    SMSKademliaNode test = new SMSKademliaNode(ID, new SMSPeer("3497312345"), dic);
    /*Creating the routing table*/
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

    @Test
    public void addContacts_IfFullBucket(){
        /*with default configuration buckets are 2 units capacious*/
        Contact c1 = new Contact(test);
        Contact c2 = new Contact(test2);
        /*I expect this contact to be add to the replacement cache*/
        Contact c3 = new Contact(test3);
        toTest.insert(c1);
        toTest.insert(c2);
        toTest.insert(c3);
        assertEquals(toTest.getReplacementCacheSize(), 1);
    }

    @Test
    public void getReplacementCacheSizeTest(){
        assertEquals(toTest.getReplacementCacheSize(), 0);
    }


}