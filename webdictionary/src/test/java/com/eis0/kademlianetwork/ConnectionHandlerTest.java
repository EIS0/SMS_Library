package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSPeer;
import com.eis0.UtilityMocks;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.listener.SMSKademliaListener;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Test by Marco Cognolato
 * Mock by Edoardo Raimondi
 */
public class ConnectionHandlerTest {
    private static final SMSPeer VALID_PEER = new SMSPeer("+395556");
    private static final SMSKademliaNode VALID_NODE = new SMSKademliaNode(VALID_PEER);

    private KademliaNetwork instance;
    private final SMSKademliaListener mockListener = mock(SMSKademliaListener.class);


    @Before
    public void setup(){
        instance = KademliaNetwork.getInstance();
        instance.init(VALID_NODE, UtilityMocks.setupMocks());
    }

    @Test()
    public void correctRequest(){
        try{
            //this throws an error because of the send message mock missing.
            ConnectionHandler.acceptRequest(VALID_PEER);
            fail();
        }
        catch(RuntimeException e){
            assertTrue(instance.isNodeInNetwork(VALID_NODE));
        }
    }
}
