package com.eis0.kademlia;

import com.eis.smslibrary.SMSPeer;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private final SMSPeer PEER4 = new SMSPeer("+393479281192");

    private final SMSKademliaNode NODE1 = new SMSKademliaNode(PEER1);
    private final SMSKademliaNode NODE2 = new SMSKademliaNode(PEER2);
    private final SMSKademliaNode NODE3 = new SMSKademliaNode(PEER3);
    private final SMSKademliaNode NODE4 = new SMSKademliaNode(PEER4);

    private final Contact CONTACT1 = new Contact(NODE1);
    private final Contact CONTACT2 = new Contact(NODE2);
    private final Contact CONTACT3 = new Contact(NODE3);
    private final Contact CONTACT4 = new Contact(NODE4);

    private SMSKademliaBucket bucket;

    private Method insertIntoReplacementCache;
    private Method removeFromReplacementCache;

    @Before
    public void setup() throws NoSuchMethodException{
        bucket = new SMSKademliaBucket(5, config);
        CONTACT1.resetStaleCount();
        CONTACT2.resetStaleCount();
        CONTACT3.resetStaleCount();
        CONTACT4.resetStaleCount();

        // insert firstNode
        bucket.insert(NODE1);   //-> insert(CONTACT1);


        insertIntoReplacementCache = SMSKademliaBucket.class.getDeclaredMethod("insertIntoReplacementCache", Contact.class);
        insertIntoReplacementCache.setAccessible(true);
        removeFromReplacementCache = SMSKademliaBucket.class.getDeclaredMethod("removeFromReplacementCache", SMSKademliaNode.class);
        removeFromReplacementCache.setAccessible(true);
    }

    @Test
    public void bucketConstruction_noErrors() {
        new SMSKademliaBucket(5, config);
    }

    @Test
    public void insertContact_getsInserted() {
        bucket.insert(CONTACT1);
        assertTrue(bucket.containsContact(CONTACT1));
    }

    @Test
    public void insertNode_getsInserted() {
        //bucket.insert(NODE1);
        assertTrue(bucket.containsContact(CONTACT1));
    }

    @Test
    public void emptyBucket_doesNotContainContact() {
        assertFalse(bucket.containsContact(CONTACT2));
    }

    @Test
    public void emptyBucket_doesNotContainNode() {
        assertFalse(bucket.containsNode(NODE2));
    }

    @Test
    public void removeContact_getsRemoved() {
        //bucket.insert(CONTACT1);
        bucket.removeContact(CONTACT1);
        assertFalse(bucket.containsContact(CONTACT1));
    }

    /**
     * Insert a Contact in the ReplacementCache, insert a Contact in the bucket, remove a Contact
     * from the bucket
     * Removing the contact starts the verification of possible substitute inside of the
     * replacementCache
     * Find the CONTACT2
     * Place it inside of the Bucket
     * Extract if from the bucket, verify its identity
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Test
    public void removeContact_ReplacementCacheNotEmpty() throws InvocationTargetException, IllegalAccessException {
        insertIntoReplacementCache.invoke(bucket, CONTACT2);
        //bucket.insert(CONTACT1);
        bucket.removeContact(CONTACT1);
        Contact contact2 = bucket.getFromContacts(NODE2);
        assertEquals(contact2, CONTACT2);
    }

    @Test
    public void getFromContactTest() {
        //bucket.insert(CONTACT1);
        Contact toReturn = bucket.getFromContacts(NODE1);
        assertEquals(toReturn, CONTACT1);
    }

    @Test
    public void containsNodeOfAnInsertedContact() {
        //bucket.insert(CONTACT1);
        assertTrue(bucket.containsNode(NODE1));
    }

    @Test
    public void removeNode_notPresent() {
        //bucket.insert(CONTACT1);
        assertFalse(bucket.containsNode(NODE2));
    }

    @Test
    public void removeNode_alreadyRemoved() {
        //bucket.insert(CONTACT1);
        bucket.removeNode(NODE1);
        assertFalse(bucket.containsNode(NODE1));
    }

    @Test
    public void removeNode_nodeNotPresent() {
        bucket.insert(CONTACT1);
        bucket.removeContact(CONTACT2);
        boolean contains = bucket.containsContact(CONTACT1);
        boolean doesNotContain = bucket.containsContact(CONTACT2);
        assertTrue(contains);
        assertFalse(doesNotContain);

    }

    @Test
    public void addContacts_IfFullBucket() {
        /*with default configuration buckets are 2 units capacious*/
        //Contact c1 = new Contact(NODE1);
        Contact c2 = new Contact(NODE2);
        /*I expect this CONTACT1 to be add to the replacement cache*/
        Contact c3 = new Contact(NODE3);
        //bucket.insert(c1);
        bucket.insert(c2);
        bucket.insert(c3);
        assertEquals(bucket.getReplacementCacheSize(), 1);
    }

    @Test
    public void addContacts_FullBucket_noStaledContacts() {
        //bucket.insert(CONTACT1);
        bucket.insert(CONTACT2);
        bucket.insert(CONTACT3);
        assertFalse(bucket.containsContact(CONTACT3));
    }

    @Test
    public void addContacts_FullBucket_OneStaledContact() {
        //bucket.insert(CONTACT1);
        for (int i = 0; i < 3; i++) CONTACT4.incrementStaleCount();
        bucket.insert(CONTACT4);
        bucket.insert(CONTACT3);
        assertFalse(bucket.containsContact(CONTACT4));
        assertTrue(bucket.containsContact(CONTACT3));
    }

    @Test
    public void addContacts_FullBucket_TwoStaledContacts() {
        //bucket.insert(CONTACT1);
        for (int i = 0; i < 2; i++) CONTACT1.incrementStaleCount();
        for (int i = 0; i < 3; i++) CONTACT4.incrementStaleCount();
        bucket.insert(CONTACT4);
        bucket.insert(CONTACT3);
        assertFalse(bucket.containsContact(CONTACT4));
        assertTrue(bucket.containsContact(CONTACT3));
    }
    @Test
    public void getReplacementCacheSizeTest() {
        assertEquals(bucket.getReplacementCacheSize(), 0);
    }

    @Test
    public void insertIntoReplacementCacheTest_emptyCache() throws InvocationTargetException, IllegalAccessException {
        insertIntoReplacementCache.invoke(bucket, CONTACT1);
        Contact contact1 = (Contact) removeFromReplacementCache.invoke(bucket, NODE1);
        assertEquals(contact1, CONTACT1);
    }

    @Test
    public void insertIntoReplacementCacheTest_contactInsertedTwice() throws InvocationTargetException, IllegalAccessException {
        insertIntoReplacementCache.invoke(bucket, CONTACT1);
        insertIntoReplacementCache.invoke(bucket, CONTACT1);
        Contact contact1 = (Contact) removeFromReplacementCache.invoke(bucket, NODE1);
        assertEquals(contact1, CONTACT1);
    }

    @Test
    public void removeFromReplacementCache_contactInsertOnce() throws InvocationTargetException, IllegalAccessException {
        insertIntoReplacementCache.invoke(bucket, CONTACT1);
        Contact contact1 = (Contact) removeFromReplacementCache.invoke(bucket, NODE1);
        assertEquals(contact1, CONTACT1);
    }

    @Test(expected = InvocationTargetException.class)
    public void removeFromReplacementCache_emptyReplacementCache() throws InvocationTargetException, IllegalAccessException {
        removeFromReplacementCache.invoke(bucket, NODE1);
    }

    @Test(expected = InvocationTargetException.class)
    public void insertIntoReplacementCacheTest_fullCache_extractFirstInserted() throws InvocationTargetException, IllegalAccessException {
        insertIntoReplacementCache.invoke(bucket, CONTACT1);
        insertIntoReplacementCache.invoke(bucket, CONTACT2);
        insertIntoReplacementCache.invoke(bucket, CONTACT3);
        removeFromReplacementCache.invoke(bucket, NODE1);
    }

    @Test
    public void insertIntoReplacementCacheTest_fullCache_extractLastInserted() throws InvocationTargetException, IllegalAccessException {
        insertIntoReplacementCache.invoke(bucket, CONTACT1);
        insertIntoReplacementCache.invoke(bucket, CONTACT2);
        insertIntoReplacementCache.invoke(bucket, CONTACT3);
        Contact contact3 = (Contact) removeFromReplacementCache.invoke(bucket, NODE3);
        assertEquals(contact3, CONTACT3);
    }

    @Test(expected = NoSuchElementException.class)
    public void getFromContact_NotInBucket() {
        bucket.getFromContacts(NODE2);
    }

    @Test
    public void insertAlreadyPresent_getsUpdatedStaleCount() {
        bucket.insert(CONTACT1);
        CONTACT1.incrementStaleCount();
        bucket.insert(CONTACT1);
        assertEquals(CONTACT1.staleCount(), 0);
    }

    @Test
    public void insertAlreadyPresent_isStillPresent() {
        bucket.insert(CONTACT1);
        bucket.insert(CONTACT1);
        assertTrue(bucket.containsContact(CONTACT1));
    }

    @Test
    public void checkNumContacts_asExpected() {
        bucket.insert(CONTACT1);
        bucket.insert(CONTACT2);
        assertEquals(bucket.numContacts(), 2);
    }

    @Test
    public void getContactList() {
        ArrayList<Contact> contList = new ArrayList<>();
        contList.add(CONTACT1);
        //contList.add(CONTACT2);
        bucket.insert(CONTACT1);
        //bucket.insert(CONTACT2);
        assertArrayEquals(contList.toArray(), bucket.getContacts().toArray());
    }

    @Test
    public void getContactList_withCache() {
        ArrayList<Contact> contList = new ArrayList<>();
        contList.add(CONTACT2);
        contList.add(CONTACT1);
        bucket.insert(CONTACT1);
        bucket.insert(CONTACT2);
        bucket.insert(CONTACT3);
        assertArrayEquals(contList.toArray(), bucket.getContacts().toArray());
        assertEquals(bucket.getReplacementCacheSize(), 1);
    }

    @Test
    public void toStringTest() {
        String expected = "Bucket at depth: 5\n" +
                " Nodes: \n" +
                "Node: 8202021E5EF98F7A2DFCDA894E49B8 (stale: 0)";
        assertEquals(bucket.toString(), expected);
    }
}