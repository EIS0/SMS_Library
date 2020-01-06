package com.eis0.kademlia;

import com.eis.smslibrary.SMSPeer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SMSKademliaBucketTest {
    private final KadConfiguration config = new DefaultConfiguration();

    private final SMSPeer PEER1 = new SMSPeer("+393408140326");
    private final SMSPeer PEER2 = new SMSPeer("+393497364511");
    private final SMSPeer PEER3 = new SMSPeer("+393497312345");

    private final SMSKademliaNode NODE1 = new SMSKademliaNode(PEER1);
    private final SMSKademliaNode NODE2 = new SMSKademliaNode(PEER2);
    private final SMSKademliaNode NODE3 = new SMSKademliaNode(PEER3);

    private final Contact CONTACT1 = new Contact(NODE1);
    private final Contact CONTACT2 = new Contact(NODE2);
    private final Contact CONTACT3 = new Contact(NODE3);

    private SMSKademliaBucket bucket;

    @Before
    public void setup(){
        bucket = new SMSKademliaBucket(5, config);
        CONTACT1.resetStaleCount();
    }

    @Test
    public void bucketConstruction_noErrors(){
        new SMSKademliaBucket(5, config);
    }

    @Test
    public void insertContact_getsInserted(){
        bucket.insert(CONTACT1);
        assertTrue(bucket.containsContact(CONTACT1));
    }

    @Test
    public void insertNode_getsInserted(){
        bucket.insert(NODE1);
        assertTrue(bucket.containsContact(CONTACT1));
    }

    @Test
    public void emptyBucket_doesNotContainContact(){
        assertFalse(bucket.containsContact(CONTACT1));
    }

    @Test
    public void emptyBucket_doesNotContainNode(){
        assertFalse(bucket.containsNode(NODE1));
    }

    @Test
    public void removeContact_getsRemoved(){
        bucket.insert(CONTACT1);
        bucket.removeContact(CONTACT1);
        assertFalse(bucket.containsContact(CONTACT1));
    }

    @Test
    public void getFromContactTest(){
        bucket.insert(CONTACT1);
        Contact toReturn = bucket.getFromContacts(NODE1);
        assertEquals(toReturn, CONTACT1);
    }

    @Test
    public void containsNodeOfAnInsertedContact(){
        bucket.insert(CONTACT1);
        assertTrue(bucket.containsNode(NODE1));
    }

    @Test
    public void removeNode_removed(){
        bucket.insert(CONTACT1);
        bucket.removeNode(NODE1);
        assertFalse(bucket.containsNode(NODE1));
    }

    @Test
    public void addContacts_IfFullBucket(){
        /*with default configuration buckets are 2 units capacious*/
        Contact c1 = new Contact(NODE1);
        Contact c2 = new Contact(NODE2);
        /*I expect this CONTACT1 to be add to the replacement cache*/
        Contact c3 = new Contact(NODE3);
        bucket.insert(c1);
        bucket.insert(c2);
        bucket.insert(c3);
        assertEquals(bucket.getReplacementCacheSize(), 1);
    }

    @Test
    public void getReplacementCacheSizeTest(){
        assertEquals(bucket.getReplacementCacheSize(), 0);
    }

    @Test(expected = NoSuchElementException.class)
    public void getFromContact_NotInBucket(){
        bucket.getFromContacts(NODE1);
    }

    @Test
    public void insertAlreadyPresent_getsUpdatedStaleCount(){
        bucket.insert(CONTACT1);
        CONTACT1.incrementStaleCount();
        bucket.insert(CONTACT1);
        assertEquals(CONTACT1.staleCount(), 0);
    }

    @Test
    public void insertAlreadyPresent_isStillPresent(){
        bucket.insert(CONTACT1);
        bucket.insert(CONTACT1);
        assertTrue(bucket.containsContact(CONTACT1));
    }

    @Test
    public void checkNumContacts_asExpected(){
        bucket.insert(CONTACT1);
        bucket.insert(CONTACT2);
        assertEquals(bucket.numContacts(), 2);
    }

    @Test
    public void getContactList(){
        ArrayList<Contact> contList = new ArrayList<>();
        contList.add(CONTACT1);
        //contList.add(CONTACT2);
        bucket.insert(CONTACT1);
        //bucket.insert(CONTACT2);
        assertArrayEquals(contList.toArray(), bucket.getContacts().toArray());
    }

    @Test
    public void getContactList_withCache(){
        ArrayList<Contact> contList = new ArrayList<>();
        contList.add(CONTACT2);
        contList.add(CONTACT1);
        bucket.insert(CONTACT1);
        bucket.insert(CONTACT2);
        bucket.insert(CONTACT3);
        assertArrayEquals(contList.toArray(), bucket.getContacts().toArray());
        assertEquals(bucket.getReplacementCacheSize(), 1);
    }
}