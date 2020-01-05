package com.eis0.kademlianetwork;

import android.telephony.SmsManager;

import com.eis.smslibrary.SMSPeer;
import com.eis0.UtilityMocks;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.informationdeliverymanager.IRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.ResourceExchangeHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SmsManager.class)
public class ResourceExchangeHandlerTest {
    private ResourceExchangeHandler resourceExchangeHandler;
    private final String KEY1 = "Key1";
    private final String KEY2 = "Key1";
    private final KademliaId KAD_ID1 = new KademliaId(KEY1);
    private final KademliaId KAD_ID2 = new KademliaId(KEY2);
    private final SMSPeer VALID_PEER = new SMSPeer("+393423541601");
    private final SMSKademliaNode MAIN_NODE = new SMSKademliaNode(VALID_PEER);
    private final SMSKademliaListener mockListener = mock(SMSKademliaListener.class);

    @Before
    public void setUp() {
        resourceExchangeHandler = new ResourceExchangeHandler();
        ResourceExchangeHandler.Request request1 = mock(ResourceExchangeHandler.Request.class);
        when(request1.getKey()).thenReturn(KEY1);
        when(request1.getKeyId()).thenReturn(KAD_ID1);
        String resource1 = "Resource1";
        when(request1.getResource()).thenReturn(resource1);

        ResourceExchangeHandler.Request request2 = mock(ResourceExchangeHandler.Request.class);
        when(request2.getKey()).thenReturn(KEY2);
        when(request2.getKeyId()).thenReturn(KAD_ID2);
        String resource2 = "Resource2";
        when(request2.getResource()).thenReturn(resource2);


        SmsManager smsManagerMock = mock(SmsManager.class);

        PowerMockito.mockStatic(SmsManager.class);
        PowerMockito.when(SmsManager.getDefault()).thenReturn(smsManagerMock);
        //when(smsManagerMock.sendTextMessage(any(String.class), any(String.class), any(String.class), any(PendingIntent.class), any(PendingIntent.class)));


        KademliaNetwork kadNet = KademliaNetwork.getInstance();
        kadNet.init(MAIN_NODE, UtilityMocks.setupMocks());
        //resourceExchangeHandler.createAddRequest(KEY2, resource2);
    }

    @Test
    public void createAddRequest_equals() {
        //resourceExchangeHandler.createAddRequest(KEY1, RESOURCE1);
        Map<KademliaId, ResourceExchangeHandler.Request> addRequests = resourceExchangeHandler.getPendingAddRequests();
        IRequest request1 = addRequests.get(KAD_ID1);
        assertEquals(request1.getKeyId(), KAD_ID1);
    }

    public void createAddRequest_notEquals() {
        Map<KademliaId, ResourceExchangeHandler.Request> addRequests = resourceExchangeHandler.getPendingAddRequests();
        IRequest request1 = addRequests.get(KAD_ID1);
        IRequest request2 = addRequests.get(KAD_ID2);
        assertNotEquals(request1.getKeyId(), request2.getKeyId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void processRequest_nullParameters() {
        resourceExchangeHandler.processRequest(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void completeAddRequest_nullParameters() {
        resourceExchangeHandler.processRequest(null, null, null);
    }
}