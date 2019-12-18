package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ResourceExchangeHandlerTest {
    private ResourceExchangeHandler resourceExchangeHandler;
    private final String KEY1 = "Key1";
    private final String KEY2 = "Key1";
    private final String RESOURCE1 = "Resource1";
    private final String RESOURCE2 = "Resource2";
    private final KademliaId KAD_ID1 = new KademliaId(KEY1);
    private final KademliaId KAD_ID2 = new KademliaId(KEY2);
    private final SMSPeer VALID_PEER = new SMSPeer("+393423541601");
    private final SMSKademliaNode MAIN_NODE = new SMSKademliaNode(VALID_PEER);

    //private final KademliaListener mockListener = mock(KademliaListener.class);

    @Before
    public void setUp() throws Exception {
        resourceExchangeHandler = new ResourceExchangeHandler();
        IRequest REQUEST1 = mock(IRequest.class);
        when(REQUEST1.getKey()).thenReturn(KEY1);
        when(REQUEST1.getKeyId()).thenReturn(KAD_ID1);
        when(REQUEST1.getResource()).thenReturn(RESOURCE1);

        IRequest REQUEST2 = mock(IRequest.class);
        when(REQUEST2.getKey()).thenReturn(KEY2);
        when(REQUEST2.getKeyId()).thenReturn(KAD_ID2);
        when(REQUEST2.getResource()).thenReturn(RESOURCE2);

        KademliaNetwork kadNet = KademliaNetwork.getInstance();
        //kadNet.init(MAIN_NODE, mockListener, UtilityMocks.setupMocks());
        SMSKademliaListener listener = new SMSKademliaListener(kadNet);
        //resourceExchangeHandler.createAddRequest(KEY1, RESOURCE1);
        //resourceExchangeHandler.createAddRequest(KEY2, RESOURCE2);
    }

    @Test
    public void createAddRequest_equals() {
        Map<KademliaId, IRequest> addRequests = resourceExchangeHandler.getPendingAddRequests();
        IRequest request1 = addRequests.get(KAD_ID1);
        assertEquals(request1.getKeyId(), KAD_ID1);
    }

    public void createAddRequest_notEquals() {
        Map<KademliaId, IRequest> addRequests = resourceExchangeHandler.getPendingAddRequests();
        IRequest request1 = addRequests.get(KAD_ID1);
        IRequest request2 = addRequests.get(KAD_ID2);
        assertFalse(request1.getKeyId().equals(request2.getKeyId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAddRequest_nullParameters() {
        resourceExchangeHandler.createAddRequest(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void processRequest_nullParameters() {
        resourceExchangeHandler.processRequest(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void completeAddRequest_nullParameters() {
        resourceExchangeHandler.processRequest(null, null, null);
    }

    @Test
    public void createGetRequest() {
    }

}