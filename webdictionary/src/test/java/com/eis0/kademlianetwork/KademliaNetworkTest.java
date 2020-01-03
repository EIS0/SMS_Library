package com.eis0.kademlianetwork;


import android.content.Context;

import com.eis.smslibrary.SMSPeer;
import com.eis0.UtilityMocks;
import com.eis0.kademlia.SMSKademliaNode;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class KademliaNetworkTest {

    Context mockContext = spy(Context.class);

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

    private final KademliaListener mockListener1 = mock(KademliaListener.class);

    @Before
    public void setUp(){
        net1.init(NODE1, mockListener1, UtilityMocks.setupMocks());
    }

    @Test
    public void addNodeToTable_OneNode(){
        net1.addNodeToTable(NODE1);
        net1.addNodeToTable(NODE2);
    }

    @Test
    public void addNodeToTable_TwoNodes(){
        net1.addNodeToTable(NODE3);
    }

    @Test
    public void testSingleton(){
        assertEquals(KademliaNetwork.getInstance(),
                     KademliaNetwork.getInstance());
    }
}
