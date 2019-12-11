package com.eis0.kademlianetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.SMSPeer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ConnectionHandlerTest {
    private static final SMSPeer VALID_PEER1 = new SMSPeer("+395554");
    private static final SMSPeer VALID_PEER2 = new SMSPeer("+395556");
    private static final KademliaId VALID_ID = new KademliaId(VALID_PEER2);
    private static final SMSKademliaNode VALID_NODE = new SMSKademliaNode(VALID_ID, VALID_PEER2, null);

    private KademliaNetwork instance;

    @Before
    public void setup(){
        instance = KademliaNetwork.getInstance();
        instance.init(VALID_PEER1);
    }

    @Test
    public void correctRequest(){
        ConnectionHandler.acceptRequest(VALID_PEER2);
        assertTrue(KademliaNetwork.getInstance().isNodeInNetwork(VALID_NODE));
    }
}
