package com.eis0.kademlianetwork.informationdeliverymanager;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.FindResourceRequest;

import org.junit.Test;

import static org.junit.Assert.*;

public class FindResourceRequestTest {

    FindResourceRequest findResourceRequest = new FindResourceRequest("ciao");

    @Test
    public void getKey() {
        assertEquals(findResourceRequest.getKey(), "ciao");
    }

    @Test
    public void getKeyId() {
        KademliaId keyId = new KademliaId("00000000000000000000006369616F"); //calculated outside
        assertEquals(findResourceRequest.getKeyId(), keyId);
    }

    @Test
    public void getResource_notAlreadyFound() {
        assertNull(findResourceRequest.getResource());
    }

    @Test
    public void isCompleted_true() {
        findResourceRequest.setCompleted(""); //2 test in 1. Not so elegant but necessary
        assertTrue(findResourceRequest.isCompleted());
    }

    @Test
    public void getResource_found(){
        findResourceRequest.setCompleted("");
        assertEquals(findResourceRequest.getResource(), "");
    }

    @Test
    public void isCompleted_false(){
        assertFalse(findResourceRequest.isCompleted());
    }

    @Test
    public void equals1() {
        assertFalse(findResourceRequest.equals(new FindResourceRequest("1")));
    }

    @Test
    public void equals2(){
        assertTrue(findResourceRequest.equals(new FindResourceRequest("ciao")));
    }
}