package com.eis0.kademlianetwork.informationdeliverymanager;

import com.eis0.kademlia.KademliaId;

import org.junit.Test;

import static org.junit.Assert.*;

public class DeleteResourceRequestTest {

    DeleteResourceRequest deleteResourceRequest = new DeleteResourceRequest("ciao");

    @Test
    public void getKey() {
        assertEquals(deleteResourceRequest.getKey(), "ciao");
    }

    @Test
    public void getKeyId() {
        KademliaId keyId = new KademliaId("00000000000000000000006369616F"); //calculated outside
        assertEquals(deleteResourceRequest.getKeyId(), keyId);
    }


    @Test
    public void isCompleted_true() {
        deleteResourceRequest.setCompleted(); //2 test in 1. Not so elegant but necessary
        assertTrue(deleteResourceRequest.isCompleted());
    }

    @Test
    public void isCompleted_false(){
        assertFalse(deleteResourceRequest.isCompleted());
    }

    @Test
    public void equals1() {
        assertFalse(deleteResourceRequest.equals(new DeleteResourceRequest("1")));
    }

    @Test
    public void equals2(){
        assertTrue(deleteResourceRequest.equals(new DeleteResourceRequest("ciao")));
    }
}