package com.eis0.kademlianetwork;


import com.eis0.kademlia.Contact;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.SMSPeer;
import static org.mockito.Mockito.mock;

/**
 * Test by Marco Cognolato
 * Mock by Edoardo Raimondi
 */

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ConnectionHandlerTest {
    private static final SMSPeer VALID_PEER1 = new SMSPeer("+395554");
    private static final SMSPeer VALID_PEER2 = new SMSPeer("+395556");
    private static final SMSKademliaNode VALID_NODE = new SMSKademliaNode(VALID_PEER2);
    private static final Contact VALID_CONTACT = new Contact(VALID_NODE);

    private KademliaNetwork instance;
    private KademliaListener mockListener = mock(KademliaListener.class);

    @Before
    public void setup(){
        instance = KademliaNetwork.getInstance();
        instance.init(VALID_NODE, mockListener);
    }

    @Test
    public void correctRequest(){
        ConnectionHandler.acceptRequest(VALID_PEER2);
        assertTrue(KademliaNetwork.getInstance().isNodeInNetwork(VALID_NODE));
    }
}
