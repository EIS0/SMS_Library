package com.eis0.kademlianetwork;

import android.telephony.SmsManager;

import com.eis.smslibrary.SMSPeer;
import com.eis0.UtilityMocks;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.informationdeliverymanager.DeleteResourceRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;
import com.eis0.kademlianetwork.listener.SMSKademliaListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SmsManager.class)
public class RequestsHandlerTest {
    private RequestsHandler requestsHandler;
    private final String KEY1 = "Key1";
    private final String KEY2 = "Key1";
    private final String RESOURCE1 = "First resource";
    private final String RESOURCE2 = "Second resource";
    private final KademliaId KAD_ID1 = new KademliaId(KEY1);
    private final KademliaId KAD_ID2 = new KademliaId(KEY2);

    private final SMSPeer VALID_PEER = new SMSPeer("+393423541601");
    private final SMSKademliaNode MAIN_NODE = new SMSKademliaNode(VALID_PEER);

    private final SMSKademliaListener mockListener = mock(SMSKademliaListener.class);
    private KademliaNetwork kadNet;
    private DeleteResourceRequest resourceRequest1;

    @Before
    public void setUp() {
        requestsHandler = new RequestsHandler();

        /*
        resourceRequest1 = mock(RequestsHandler.DeleteResourceRequest.class);
        when(resourceRequest1.getKey()).thenReturn(KEY1);
        when(resourceRequest1.getKeyId()).thenReturn(KAD_ID1);
        String resource1 = "Resource1";
        when(resourceRequest1.getResource()).thenReturn(resource1);

         */

        DeleteResourceRequest resourceRequest2 = mock(DeleteResourceRequest.class);
        when(resourceRequest2.getKey()).thenReturn(KEY2);
        when(resourceRequest2.getKeyId()).thenReturn(KAD_ID2);

        SmsManager smsManagerMock = mock(SmsManager.class);

        PowerMockito.mockStatic(SmsManager.class);
        PowerMockito.when(SmsManager.getDefault()).thenReturn(smsManagerMock);
        //when(smsManagerMock.sendTextMessage(any(String.class), any(String.class), any(String.class), any(PendingIntent.class), any(PendingIntent.class)));


        kadNet = KademliaJoinableNetwork.getInstance();
        kadNet.init(MAIN_NODE, UtilityMocks.setupMocks());
        when(kadNet.isAlive(MAIN_NODE.getPeer())).thenReturn(true);
        //requestsHandler.createAddRequest(KEY2, resource2);
    }

    @Test
    public void createAddRequest_equals() {
        /*
        requestsHandler.createRequest(KEY1, RESOURCE1);
        Map<KademliaId, RequestsHandler.DeleteResourceRequest> addRequests = requestsHandler.getPendingAddRequests();
        resourceRequest1 = addRequests.get(KAD_ID1);
        assertEquals(resourceRequest1.getKeyId(), KAD_ID1);

         */
    }
}