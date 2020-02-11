package com.eis0.kademlianetwork.informationdeliverymanager;

import com.eis0.kademlia.KademliaId;

import org.junit.Test;

import static org.junit.Assert.*;

public class AddResourceRequestTest {

    AddResourceRequest addResourceRequest = new AddResourceRequest("ciao", "");

    @Test
    public void getKey() {
        assertEquals(addResourceRequest.getKey(), "ciao");
    }

    @Test
    public void getKeyId() {
        KademliaId keyId = new KademliaId("00000000000000000000006369616F"); //calculated outside
        assertEquals(addResourceRequest.getKeyId(), keyId);
    }

    @Test
    public void getResource() {
        assertEquals(addResourceRequest.getResource(), "");
    }

    @Test
    public void isCompleted_true() {
        addResourceRequest.setCompleted(); //2 test in 1. Not so elegant but necessary
        assertTrue(addResourceRequest.isCompleted());
    }

    @Test
    public void isCompleted_false(){
        assertFalse(addResourceRequest.isCompleted());
    }

    @Test
    public void equals1() {
        assertFalse(addResourceRequest.equals(new AddResourceRequest("1", "")));
    }

    @Test
    public void equals2(){
        assertTrue(addResourceRequest.equals(new AddResourceRequest("ciao", "")));
    }
}