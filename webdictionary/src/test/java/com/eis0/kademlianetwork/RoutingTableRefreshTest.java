package com.eis0.kademlianetwork;

import com.eis0.UtilityMocks;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaBucket;
import com.eis0.kademlia.SMSKademliaNode;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class RoutingTableRefreshTest {

    private KademliaId ID1 = new KademliaId("0");
    private KademliaId ID2 = new KademliaId("1");
    private KademliaId ID3 = new KademliaId("2");
    private KademliaId ID4 = new KademliaId("1");

    private SMSKademliaNode NODE1 = new SMSKademliaNode(ID1);
    private SMSKademliaNode NODE2 = new SMSKademliaNode(ID2);
    private SMSKademliaNode NODE3 = new SMSKademliaNode(ID3);
    private SMSKademliaNode NODE4 = new SMSKademliaNode(ID4);

    private RoutingTableRefresh refresh = new RoutingTableRefresh(NODE1);

    private final KademliaNetwork net1 = new KademliaNetwork();
    private final KademliaNetwork net2 = new KademliaNetwork();
    private final KademliaNetwork net3 = new KademliaNetwork();
    private final KademliaNetwork net4 = new KademliaNetwork();

    private final KademliaListener mockListener1 = mock(KademliaListener.class);
    private final KademliaListener mockListener2 = mock(KademliaListener.class);
    private final KademliaListener mockListener3 = mock(KademliaListener.class);
    private final KademliaListener mockListener4 = mock(KademliaListener.class);

    @Before
    public void setUp(){

        net1.init(NODE1, mockListener1, UtilityMocks.setupMocks());
        //net1 routing table population
        net1.addNodeToTable(NODE2);
        net1.addNodeToTable(NODE3);

        net2.init(NODE2, mockListener2, UtilityMocks.setupMocks());
        net3.init(NODE3, mockListener3, UtilityMocks.setupMocks());
        net4.init(NODE4, mockListener4, UtilityMocks.setupMocks());

        //set node2 as unresponsive removing it
        int bucketNode2Id = net1.getLocalRoutingTable().getBucketId(NODE2.getId());
        net1.getLocalRoutingTable().getBuckets()[bucketNode2Id].removeNode(NODE2);

        net1.refresh.start();
    }

    @Test
    public void

}