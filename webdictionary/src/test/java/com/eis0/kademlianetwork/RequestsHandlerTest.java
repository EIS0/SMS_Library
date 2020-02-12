package com.eis0.kademlianetwork;

import android.telephony.SmsManager;

import com.eis.smslibrary.SMSPeer;
import com.eis0.UtilityMocks;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.informationdeliverymanager.AddResourceRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.DeleteResourceRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.FindIdRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.FindResourceRequest;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SmsManager.class)
public class RequestsHandlerTest {

    private RequestsHandler requestsHandler = new RequestsHandler();
    private final String KEY1 = "Key1";
    private final String RESOURCE1 = "First resource";
    private final KademliaId KAD_ID1 = new KademliaId(KEY1);


    @Test
    public void startFindIdRequest(){
        FindIdRequest request = new FindIdRequest(KAD_ID1);
        assertTrue(requestsHandler.startFindIdRequest(KAD_ID1).equals(request));
    }

    @Test
    public void startFindResourceRequest(){
        FindResourceRequest request = new FindResourceRequest(RESOURCE1);
        assertTrue(requestsHandler.startFindResourceRequest(RESOURCE1).equals(request));
    }

    @Test
    public void startAddResourceRequest(){
        AddResourceRequest resourceRequest = new AddResourceRequest(KEY1, RESOURCE1);
        assertTrue(requestsHandler.startAddResourceRequest(KEY1, RESOURCE1).equals(resourceRequest));
    }

    @Test
    public void startDeleteResourceRequest(){
        DeleteResourceRequest resourceRequest = new DeleteResourceRequest(KEY1);
        assertTrue(requestsHandler.startDeleteResourceRequest(KEY1).equals(resourceRequest));
    }

}