package com.eis0.kademlianetwork;


import com.eis.smslibrary.SMSPeer;
import static org.junit.Assert.*;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.AddResourceRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.DeleteResourceRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.FindIdRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.FindResourceRequest;
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
    public void completeFindIdRequest_twoRequests_sameID() {
        /*I expect only the more recent request (so the second one) to be completed*/
        FindIdRequest notToClose = requestsHandler.startFindIdRequest(KAD_ID1);
        FindIdRequest toClose = requestsHandler.startFindIdRequest(KAD_ID1);
        requestsHandler.completeFindIdRequest(KAD_ID1, PEER);
        assertFalse(notToClose.isCompleted());
        assertTrue(toClose.isCompleted());
    }

    @Test
    public void completeFindIdRequest_twoDifferentRequests() {
        /*I expect both to be closed*/
        FindIdRequest toClose1 = requestsHandler.startFindIdRequest(KAD_ID1);
        FindIdRequest toClose2 = requestsHandler.startFindIdRequest(KAD_ID2);
        requestsHandler.completeFindIdRequest(KAD_ID1, PEER);
        requestsHandler.completeFindIdRequest(KAD_ID2, PEER);
        assertTrue(toClose1.isCompleted());
        assertTrue(toClose2.isCompleted());
    }

    @Test
    public void completeFindIdRequest_calledTwice(){
        /*I don't expect any problem*/
        FindIdRequest toClose = requestsHandler.startFindIdRequest(KAD_ID1);
        requestsHandler.completeFindIdRequest(KAD_ID1, PEER);
        requestsHandler.completeFindIdRequest(KAD_ID1, PEER);
        assertTrue(toClose.isCompleted());
    }

    @Test
    public void completeFindIdRequest_emptyCall(){
        /*I don't expect any problem*/
        requestsHandler.completeFindIdRequest(KAD_ID1, PEER);
    }

    @Test
    public void completeFindResourceRequest_twoRequests_sameID() {
        /*I expect only the more recent request (so the second one) to be completed*/
        FindResourceRequest notToClose = requestsHandler.startFindResourceRequest(KEY1);
        FindResourceRequest toClose = requestsHandler.startFindResourceRequest(KEY1);
        requestsHandler.completeFindResourceRequest(KEY1, RESOURCE1);
        assertFalse(notToClose.isCompleted());
        assertTrue(toClose.isCompleted());
    }

    @Test
    public void completeFindResourceRequest_twoDifferentRequests() {
        /*I expect both to be closed*/
        FindResourceRequest toClose1 = requestsHandler.startFindResourceRequest(KEY1);
        FindResourceRequest toClose2 = requestsHandler.startFindResourceRequest(KEY2);
        requestsHandler.completeFindResourceRequest(KEY1, RESOURCE1);
        requestsHandler.completeFindResourceRequest(KEY2, RESOURCE1);
        assertTrue(toClose1.isCompleted());
        assertTrue(toClose2.isCompleted());
    }

    @Test
    public void completeFindResourceRequest_calledTwice(){
        /*I don't expect any problem*/
        FindResourceRequest toClose = requestsHandler.startFindResourceRequest(KEY1);
        requestsHandler.completeFindResourceRequest(KEY1, RESOURCE1);
        requestsHandler.completeFindResourceRequest(KEY1, RESOURCE1);
        assertTrue(toClose.isCompleted());
    }

    @Test
    public void completeFindResourceRequest_emptyCall(){
        /*I don't expect any problem*/
        requestsHandler.completeFindResourceRequest(KEY1, RESOURCE1);
    }

    @Test
    public void completeAddResourceRequest_twoRequests_sameID() {
        /*I expect only the more recent request (so the second one) to be completed*/
        AddResourceRequest notToClose = requestsHandler.startAddResourceRequest(KEY1, RESOURCE1);
        AddResourceRequest toClose = requestsHandler.startAddResourceRequest(KEY1, RESOURCE1);
        requestsHandler.completeAddResourceRequest(KEY1);
        assertFalse(notToClose.isCompleted());
        assertTrue(toClose.isCompleted());
    }

    @Test
    public void completeAddResourceRequest_twoDifferentRequests() {
        /*I expect both to be closed*/
        AddResourceRequest toClose1 = requestsHandler.startAddResourceRequest(KEY1, RESOURCE1);
        AddResourceRequest toClose2 = requestsHandler.startAddResourceRequest(KEY2, RESOURCE1);
        requestsHandler.completeAddResourceRequest(KEY1);
        requestsHandler.completeAddResourceRequest(KEY2);
        assertTrue(toClose1.isCompleted());
        assertTrue(toClose2.isCompleted());
    }

    @Test
    public void completeAddResourceRequest_calledTwice(){
        /*I don't expect any problem*/
        AddResourceRequest toClose = requestsHandler.startAddResourceRequest(KEY1, RESOURCE1);
        requestsHandler.completeAddResourceRequest(KEY1);
        requestsHandler.completeAddResourceRequest(KEY1);
        assertTrue(toClose.isCompleted());
    }

    @Test
    public void completeAddResourceRequest_emptyCall(){
        /*I don't expect any problem*/
        requestsHandler.completeAddResourceRequest(KEY1);
    }

    @Test
    public void completeDeleteResourceRequest_twoRequests_sameID() {
        /*I expect only the more recent request (so the second one) to be completed*/
        DeleteResourceRequest notToClose = requestsHandler.startDeleteResourceRequest(KEY1);
        DeleteResourceRequest toClose = requestsHandler.startDeleteResourceRequest(KEY1);
        requestsHandler.completeDeleteResourceRequest(KEY1);
        assertFalse(notToClose.isCompleted());
        assertTrue(toClose.isCompleted());
    }

    @Test
    public void completeDeleteResourceRequest_twoDifferentRequests() {
        /*I expect both to be closed*/
        DeleteResourceRequest toClose1 = requestsHandler.startDeleteResourceRequest(KEY1);
        DeleteResourceRequest toClose2 = requestsHandler.startDeleteResourceRequest(KEY2);
        requestsHandler.completeDeleteResourceRequest(KEY1);
        requestsHandler.completeDeleteResourceRequest(KEY2);
        assertTrue(toClose1.isCompleted());
        assertTrue(toClose2.isCompleted());
    }

    @Test
    public void completeDeleteResourceRequest_calledTwice(){
        /*I don't expect any problem*/
        DeleteResourceRequest toClose = requestsHandler.startDeleteResourceRequest(KEY1);
        requestsHandler.completeDeleteResourceRequest(KEY1);
        requestsHandler.completeDeleteResourceRequest(KEY1);
        assertTrue(toClose.isCompleted());
    }

    @Test
    public void completeDeleteResourceRequest_emptyCall(){
        /*I don't expect any problem*/
        requestsHandler.completeDeleteResourceRequest(KEY1);
    }

    @Test
    public void completeDeleteResourceRequest_mixCall(){
        /*I expect notToClose not to be closed*/
        DeleteResourceRequest notToClose = requestsHandler.startDeleteResourceRequest(KEY1);
        requestsHandler.completeAddResourceRequest(KEY1);
        assertFalse(notToClose.isCompleted());
    }

}