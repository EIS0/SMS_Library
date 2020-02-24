package com.eis0.kademlianetwork.informationdeliverymanager;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;

import org.junit.Test;

import static org.junit.Assert.*;

public class KademliaMessageAnalyzerTest {

    private final String BLANCK = "/";
    private final SMSPeer NULL_PEER = new SMSPeer("/");
    private final SMSPeer PEER1 = new SMSPeer("+393423541601");
    private final RequestTypes REQUEST1 = RequestTypes.RemoveFromDict;
    private final RequestTypes REQUEST2 = RequestTypes.AcknowledgeMessage;
    private final KademliaId ID1 = new KademliaId(PEER1);
    private final String KEY1 = "Key1";
    private final String RESOURCE1 = "risorsa/risorsa";
    private final String RESOURCE2 = "risorsa";
    private final String RESOURCE3 = "risorsa con più parole";

    SMSMessage constructed = new KademliaMessage()
            .setPeer(PEER1)
            .setRequestType(REQUEST1)
            .setIdToFind(ID1)
            .setSearcher(PEER1)
            .setKey(KEY1)
            .setResource(RESOURCE1)
            .buildMessage();

    SMSMessage problematic = new KademliaMessage()
            .setPeer(PEER1)
            .setRequestType(REQUEST2)
            .buildMessage();


    private final KademliaMessageAnalyzer messageAnalyzer1 = new KademliaMessageAnalyzer(constructed);
    private final KademliaMessageAnalyzer messageAnalyzer2 = new KademliaMessageAnalyzer(problematic);

    @Test
    public void getPeer() {
        assertEquals(messageAnalyzer1.getPeer(), PEER1);
    }

    @Test
    public void getCommand() {
        assertEquals(messageAnalyzer1.getCommand(), REQUEST1);
    }

    @Test
    public void getIdToFind() {
        assertEquals(messageAnalyzer1.getIdToFind(), ID1);
    }

    @Test
    public void getSearcher() {
        assertEquals(messageAnalyzer1.getSearcher(), PEER1);
    }

    @Test
    public void getKey() {
        assertEquals(messageAnalyzer1.getKey(), KEY1);
    }

    @Test
    public void getResource() {
        assertEquals(messageAnalyzer1.getResource(), RESOURCE1);
    }

    @Test
    public void getKey_null(){ assertEquals(messageAnalyzer2.getKey(), BLANCK); }

    @Test
    public void getResource_null() { assertEquals(messageAnalyzer2.getResource(), BLANCK);}

    @Test
    public void getSearcher_null() { assertEquals(messageAnalyzer2.getSearcher(), NULL_PEER);}

    @Test
    public void getResource_oneWord() {
        SMSMessage message = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setIdToFind(ID1)
                .setSearcher(PEER1)
                .setKey(KEY1)
                .setResource(RESOURCE2)
                .buildMessage();
        KademliaMessageAnalyzer messageAnalyzer = new KademliaMessageAnalyzer(message);
        assertEquals(messageAnalyzer.getResource(), RESOURCE2);
    }

    @Test
    public void getResource_multipleWords() {
        SMSMessage message = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setIdToFind(ID1)
                .setSearcher(PEER1)
                .setKey(KEY1)
                .setResource(RESOURCE3)
                .buildMessage();
        KademliaMessageAnalyzer messageAnalyzer = new KademliaMessageAnalyzer(message);
        assertEquals(messageAnalyzer.getResource(), RESOURCE3);
    }
}