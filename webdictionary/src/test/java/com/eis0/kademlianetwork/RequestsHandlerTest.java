package com.eis0.kademlianetwork;


import com.eis.smslibrary.SMSPeer;
import static org.junit.Assert.*;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.AddResourceRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.DeleteResourceRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.FindIdRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;

import org.junit.Before;
import org.junit.Test;


public class RequestsHandlerTest {

    private RequestsHandler requestsHandler;

    private final String KEY1 = "Key1";
    private final String KEY2 = "key2";
    private final String RESOURCE1 = "First resource";
    private final KademliaId KAD_ID1 = new KademliaId(KEY1);
    private final KademliaId KAD_ID2 = new KademliaId(KEY2);
    private final SMSPeer PEER = new SMSPeer("12345");

    private FindIdRequest findIdRequest;
    private AddResourceRequest addResourceRequest;
    private DeleteResourceRequest deleteResourceRequest;

    @Before
    public void setUp() {

        requestsHandler = new RequestsHandler();

        deleteResourceRequest = new DeleteResourceRequest(KEY1);
        findIdRequest = new FindIdRequest(KAD_ID1);
        addResourceRequest = new AddResourceRequest(KEY1, RESOURCE1);


    }

    @Test
     public void startFindIdRequest() {
        assert(requestsHandler.startFindIdRequest(KAD_ID1).equals(findIdRequest));
    }

    @Test
     public void startAddResourceRequest() {
        assert(requestsHandler.startAddResourceRequest(KEY1, RESOURCE1).equals(addResourceRequest));
    }

    @Test
     public void startDeleteResourceRequest() {
        assert(requestsHandler.startDeleteResourceRequest(KEY1).equals(deleteResourceRequest));
    }

    @Test
    public void completeFindIdRequest() {
        FindIdRequest toClose = requestsHandler.startFindIdRequest(KAD_ID1);
        requestsHandler.completeFindIdRequest(KAD_ID1, PEER);
        assertTrue(toClose.isCompleted());
    }

    @Test
    public void completeFindIdrequest_twoRequest_sameID() {
        FindIdRequest toClose1 = requestsHandler.startFindIdRequest(KAD_ID1);
        FindIdRequest toClose2 = requestsHandler.startFindIdRequest(KAD_ID1);
        requestsHandler.completeFindIdRequest(KAD_ID1, PEER);
        //assertTrue(toClose1.isCompleted());
        assertTrue(toClose2.isCompleted());
    }


}