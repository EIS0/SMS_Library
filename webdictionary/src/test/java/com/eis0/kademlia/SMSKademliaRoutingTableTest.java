package com.eis0.kademlia;


import com.eis.smslibrary.SMSPeer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SMSKademliaRoutingTableTest {

    private final KadConfiguration CONFIG = new DefaultConfiguration();

    private final KademliaId ID1 = new KademliaId("0000000000000001");
    private final KademliaId ID2 = new KademliaId("0000000000000011");

    private final SMSPeer PEER1 = new SMSPeer("+393423541604");
    private final SMSPeer PEER2 = new SMSPeer("+393423541606");
    private final SMSPeer PEER3 = new SMSPeer("+393423541601");

    private final SMSKademliaNode NODE1 = new SMSKademliaNode(PEER1);
    private final SMSKademliaNode NODE2 = new SMSKademliaNode(PEER2);
    private final SMSKademliaNode MAIN_NODE = new SMSKademliaNode(PEER3);

    /* Testing initialization */
    private SMSKademliaRoutingTable routingTable;

    private ArrayList<SMSKademliaNode> partialList = new ArrayList<>();
    private ArrayList<SMSKademliaNode> fullList = new ArrayList<>();

    @Before
    public void setup(){
        partialList.add(MAIN_NODE);
        partialList.add(NODE1);
        fullList.add(MAIN_NODE);
        fullList.add(NODE1);
        fullList.add(NODE2);
        routingTable = new SMSKademliaRoutingTable(MAIN_NODE, CONFIG);
    }

    @Test
    public void constructionTest(){
        new SMSKademliaRoutingTable(MAIN_NODE, CONFIG);
    }

    @Test
    public void initializationTest(){
        //this might be redundant since the routing table is initialized when it is constructed
        new SMSKademliaRoutingTable(MAIN_NODE, CONFIG).initialize();
    }

    @Test
    public void insertNoNode_mainIsPresent(){
        assertEquals(routingTable.getAllNodes().size(), 1);
        assertEquals(routingTable.getAllNodes().get(0), MAIN_NODE);
    }

    @Test
    public void insertOneContact_isAdded(){
        Contact contact = new Contact(NODE1);
        routingTable.insert(contact);
        assertArrayEquals(routingTable.getAllNodes().toArray(), partialList.toArray());
    }

    @Test
    public void insertOneNode_isAdded(){
        routingTable.insert(NODE1);
        assertArrayEquals(routingTable.getAllNodes().toArray(), partialList.toArray());
    }

    @Test
    public void insertTwoNodes_areAdded(){
        routingTable.insert(NODE1);
        routingTable.insert(NODE2);
        assertArrayEquals(routingTable.getAllNodes().toArray(), fullList.toArray());
    }

    @Test
    public void insertTwoContacts_areAdded(){
        routingTable.insert(new Contact(NODE1));
        routingTable.insert(new Contact(NODE2));
        assertArrayEquals(routingTable.getAllNodes().toArray(), fullList.toArray());
    }

    @Test
    public void getBucketIdTest(){
        assertEquals(routingTable.getBucketId(ID1), KademliaId.ID_LENGTH-2);
    }

    @Test
    public void findClosestTest(){
        routingTable.insert(NODE2);
        List<SMSKademliaNode> result = routingTable.findClosest(ID2, 1);
        assertEquals(result.get(0), NODE2);
    }

    @Test
    public void getBucketTest(){
        SMSKademliaBucket[] result = routingTable.getBuckets();
        assertEquals(result[0], routingTable.getBuckets()[0]);
    }
}