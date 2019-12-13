package com.eis0.kademlia;

import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SMSNetVocabulary;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SMSKademliaBucketTest {
    KadConfiguration config = new DefaultConfiguration();

    /*Creating some Nodes*/
    SMSKademliaNode test3 = new SMSKademliaNode(new SMSPeer("3408140326"));
    SMSKademliaNode test2 = new SMSKademliaNode(new SMSPeer("3497364511"));
    SMSKademliaNode test = new SMSKademliaNode(new SMSPeer("3497312345"));

    /*Creating contact*/
    Contact c = new Contact(test);

    /*setup test*/
    SMSKademliaBucket toTest = new SMSKademliaBucket(5, config);

    @Test
    public void insertTest(){
        toTest.insert(c);
        assertTrue(toTest.containsContact(c));
    }

    @Test
    public void containsContactTest(){
        assertFalse(toTest.containsContact(c));
    }

    @Test
    public void containsNodeTest(){
        assertFalse(toTest.containsNode(test));
    }

    @Test
    public void removeContactTest(){
        toTest.insert(c);
        assertTrue(toTest.removeContact(c));
    }

    @Test
    public void getFromContactTest(){
        toTest.insert(c);
        Contact toReturn = toTest.getFromContacts(test);
        assertTrue(toReturn.equals(c));
    }

    @Test
    public void removeFromContactTest(){
        toTest.insert(c);
        assertFalse(toTest.containsNode(test));
    }

    @Test
    public void removeNodeTest(){
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

    @Test(expected = NoSuchElementException.class)
    public void getFromContact_NotInBucket(){
        toTest.getFromContacts(test);
    }

    @Test(expected =  NoSuchElementException.class)
    public void removeFromContacts_NotInBucket(){
        toTest.removeFromContacts(test);
    }



}