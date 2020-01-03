package com.eis0.kademlianetwork;

import android.telephony.SmsManager;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSPeer;
import com.eis0.UtilityMocks;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaBucket;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.KademliaListener;
import com.eis0.kademlianetwork.KademliaNetwork;
import com.eis0.kademlianetwork.RoutingTableRefresh;

import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.assertTrue;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RoutingTableRefreshTest {

    private SMSPeer peer1 = new SMSPeer("+5554");
    private SMSPeer peer2 = new SMSPeer("+5555");
    private SMSPeer peer3 = new SMSPeer("+5556");
    private SMSPeer peer4 = new SMSPeer("+5554");

    private SMSKademliaNode NODE1 = new SMSKademliaNode(peer1); //local node
    private SMSKademliaNode NODE2 = new SMSKademliaNode(peer2);
    private SMSKademliaNode NODE3 = new SMSKademliaNode(peer3);
    private SMSKademliaNode NODE4 = new SMSKademliaNode(peer4);

    private final KademliaNetwork net1 = new KademliaNetwork();
    private final KademliaNetwork net2 = new KademliaNetwork();
    private final KademliaNetwork net3 = new KademliaNetwork();
    private final KademliaNetwork net4 = new KademliaNetwork();

    private RoutingTableRefresh refresh = new RoutingTableRefresh(NODE1, net1);

    private final KademliaListener mockListener1 = mock(KademliaListener.class);
    //private final KademliaListener mockListener2 = mock(KademliaListener.class);
    private final KademliaListener mockListener3 = mock(KademliaListener.class);
    private final KademliaListener mockListener4 = mock(KademliaListener.class);

    @Before
    public void setUp(){

        SmsManager smsManagerMock = mock(SmsManager.class);

        PowerMockito.mockStatic(SmsManager.class);
        PowerMockito.when(SmsManager.getDefault()).thenReturn(smsManagerMock);


        net1.init(NODE1, mockListener1, UtilityMocks.setupMocks());
        //I don't set Node2 listener in order to set it unresponsive
        //net2.init(NODE2, mockListener2, UtilityMocks.setupMocks());
        net3.init(NODE3, mockListener3, UtilityMocks.setupMocks());
        net4.init(NODE4, mockListener4, UtilityMocks.setupMocks());

        //net1 routing table population
        net1.addNodeToTable(NODE2);
        net1.addNodeToTable(NODE3);
        //net3 routing table population
        net3.addNodeToTable(NODE4);


        //I expected node4 to be added in my routing table
        net1.refresh.start();
    }

    @Test()
    public void testIfSwitched(){
        int bucketId = net1.getLocalRoutingTable().getBucketId(NODE4.getId());
        assertTrue(net1.getLocalRoutingTable().getBuckets()[bucketId].containsNode(NODE4));
    }

}
