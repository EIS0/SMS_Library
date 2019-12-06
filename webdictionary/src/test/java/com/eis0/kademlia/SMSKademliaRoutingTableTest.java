package com.eis0.kademlia;


import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SMSNetVocabulary;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SMSKademliaRoutingTableTest {

    KadConfiguration config = new DefaultConfiguration();
    SMSNetVocabulary dic = new SMSNetVocabulary();
    KademliaId ID = new KademliaId("00000000000000000001");
    KademliaId ID2 = new KademliaId("00000000000000000011");
    SMSKademliaNode test2 = new SMSKademliaNode(ID2, new SMSPeer("3497364511"), dic);
    SMSKademliaNode test = new SMSKademliaNode(ID, new SMSPeer("3497364511"), dic);

    /* Testing initialization */
    SMSKademliaRoutingTable toTest = new SMSKademliaRoutingTable(test, config);

    @Test
    public void initializationTest(){
        toTest.initialize();
    }

    @Test
    public void insertContactTest(){
        Contact c = new Contact(test);
        toTest.insert(c);
        assertEquals(toTest.getAllNodes().get(0), test);
    }

    @Test
    public void insertNodeTest(){
        toTest.insert(test);
        assertEquals(toTest.getAllNodes().get(0), test);
    }

    @Test
    public void getBucketIdTest(){
        assertEquals(toTest.getBucketId(ID), 0);
    }

    @Test
    public void findClosestTest(){
        KademliaId target = new KademliaId("00000000000000000010");
        SMSNetVocabulary dic = new SMSNetVocabulary();
        SMSKademliaNode toCompare = new SMSKademliaNode(target,
                new SMSPeer("3497364511"), dic);
        toTest.insert(toCompare);
        int required = 1; //so I should have target as result
        List<SMSKademliaNode> result;
        result = toTest.findClosest(target, required);
        assertEquals(result.get(0), toCompare);
    }

    @Test
    public void getAllNodesTest(){
        toTest.insert(test);
        toTest.insert(test2);
        List<SMSKademliaNode> result;
        result = toTest.getAllNodes();
        assertEquals(result.get(0), test);
        assertEquals(result.get(1), test2);
    }

    @Test
    public void getBucketTest(){
        SMSKademliaBucket[] result = toTest.getBuckets();
        assertEquals(result[0], toTest.getBuckets()[0]);
    }
}