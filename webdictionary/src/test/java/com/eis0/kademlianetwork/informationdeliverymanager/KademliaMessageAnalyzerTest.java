package com.eis0.kademlianetwork.informationdeliverymanager;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;

import org.junit.Test;

import static org.junit.Assert.*;

public class KademliaMessageAnalyzerTest {

    private final SMSPeer PEER1 = new SMSPeer("+393423541601");
    private final RequestTypes REQUEST1 = RequestTypes.RemoveFromDict;
    private final KademliaId ID1 = new KademliaId(PEER1);
    private final String KEY1 = "Key1";
    private final String RESOURCE1 = "risorsa/risorsa";

    SMSMessage constructed = new KademliaMessage()
            .setPeer(PEER1)
            .setRequestType(REQUEST1)
            .setIdToFind(ID1)
            .setSearcher(PEER1)
            .setKey(KEY1)
            .setResource(RESOURCE1)
            .buildMessage();


    private final KademliaMessageAnalyzer messageAnalyzer = new KademliaMessageAnalyzer(constructed);

    @Test
    public void getPeer() {
        assertEquals(messageAnalyzer.getPeer(), PEER1);
    }

    @Test
    public void getCommand() {
        assertEquals(messageAnalyzer.getCommand(), REQUEST1);
    }

    @Test
    public void getIdToFind() {
        assertEquals(messageAnalyzer.getIdToFind(), ID1);
    }

    @Test
    public void getSearcher() {
        assertEquals(messageAnalyzer.getSearcher(), PEER1);
    }

    @Test
    public void getKey() {
        assertEquals(messageAnalyzer.getKey(), KEY1);
    }

    @Test
    public void getResource() {
        assertEquals(messageAnalyzer.getResource(), RESOURCE1);
    }
}