package com.eis0.kademlianetwork.activitystatus;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KademliaMessageTest {

    private final String BLANK = "/";

    private final SMSPeer PEER1 = new SMSPeer("+393423541601");
    private final SMSPeer PEER2 = new SMSPeer("+393423541602");

    private final RequestTypes REQUEST1 = RequestTypes.FindId;
    private final RequestTypes REQUEST2 = RequestTypes.AcceptJoin;

    private final KademliaId ID1 = new KademliaId(PEER1);
    private final KademliaId ID2 = new KademliaId(PEER2);

    private final String KEY1 = "Key1";
    private final String INVALID_KEY = "This Key is Not Valid";

    private final String RESOURCE1 = "This is the first resource to send";
    private final String RESOURCE2 = "ResToSend";

    @Test
    public void constructValidMessage(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setIdToFind(ID1)
                .setSearcher(PEER1)
                .setKey(KEY1)
                .setResource(RESOURCE1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + ID1
                + " " + PEER1 + " " + KEY1 + " " + RESOURCE1;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_differentOrder(){
        SMSMessage constructed = new KademliaMessage()
                .setIdToFind(ID1)
                .setResource(RESOURCE1)
                .setSearcher(PEER1)
                .setPeer(PEER1)
                .setKey(KEY1)
                .setRequestType(REQUEST1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + ID1
                + " " + PEER1 + " " + KEY1 + " " + RESOURCE1;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_differentResource(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setIdToFind(ID1)
                .setSearcher(PEER1)
                .setKey(KEY1)
                .setResource(RESOURCE2)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + ID1
                + " " + PEER1 + " " + KEY1 + " " + RESOURCE2;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_missingId(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setSearcher(PEER1)
                .setKey(KEY1)
                .setResource(RESOURCE1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + BLANK
                + " " + PEER1 + " " + KEY1 + " " + RESOURCE1;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_missingKey(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setIdToFind(ID1)
                .setSearcher(PEER1)
                .setResource(RESOURCE1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + ID1
                + " " + PEER1 + " " + BLANK + " " + RESOURCE1;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_missingResource(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setIdToFind(ID1)
                .setSearcher(PEER1)
                .setKey(KEY1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + ID1
                + " " + PEER1 + " " + KEY1 + " " + BLANK;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_missingSearcher(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setIdToFind(ID1)
                .setKey(KEY1)
                .setResource(RESOURCE1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + ID1
                + " " + BLANK + " " + KEY1 + " " + RESOURCE1;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_missingIdAndKey(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setSearcher(PEER1)
                .setResource(RESOURCE1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + BLANK
                + " " + PEER1 + " " + BLANK + " " + RESOURCE1;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_missingKeyAndResource(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setIdToFind(ID1)
                .setSearcher(PEER1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + ID1
                + " " + PEER1 + " " + BLANK + " " + BLANK;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_missingIdAndResource(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setSearcher(PEER1)
                .setKey(KEY1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + BLANK
                + " " + PEER1 + " " + KEY1 + " " + BLANK;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_missingSearcherAndId(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setKey(KEY1)
                .setResource(RESOURCE1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + BLANK
                + " " + BLANK + " " + KEY1 + " " + RESOURCE1;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_missingSearcherAndKey(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setIdToFind(ID1)
                .setResource(RESOURCE1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + ID1
                + " " + BLANK + " " + BLANK + " " + RESOURCE1;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_missingSearcherAndResource(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setIdToFind(ID1)
                .setKey(KEY1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + ID1
                + " " + BLANK + " " + KEY1 + " " + BLANK;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_onlySearcher(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setSearcher(PEER1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + BLANK
                + " " + PEER1 + " " + BLANK + " " + BLANK;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_onlyId(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setIdToFind(ID1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + ID1
                + " " + BLANK + " " + BLANK + " " + BLANK;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_onlyKey(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setKey(KEY1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + BLANK
                + " " + BLANK + " " + KEY1 + " " + BLANK;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_onlyResource(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setResource(RESOURCE1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + BLANK
                + " " + BLANK + " " + BLANK + " " + RESOURCE1;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test
    public void constructValidMessage_missingAll(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .buildMessage();
        String expectedMessage = REQUEST1.ordinal() + " " + BLANK
                + " " + BLANK + " " + BLANK + " " + BLANK;
        assertEquals(constructed.getPeer(), PEER1);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructInvalidMessage_noPeerSet(){
        new KademliaMessage()
                .setRequestType(REQUEST1)
                .setIdToFind(ID1)
                .setKey(KEY1)
                .setResource(RESOURCE1)
                .buildMessage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructInvalidMessage_noRequestSet(){
        new KademliaMessage()
                .setPeer(PEER1)
                .setIdToFind(ID1)
                .setKey(KEY1)
                .setResource(RESOURCE1)
                .buildMessage();
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructInvalidMessage_invalidKey(){
        new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setIdToFind(ID1)
                .setKey(INVALID_KEY)
                .setResource(RESOURCE1)
                .buildMessage();
    }

    @Test
    public void constructValidMessage_withReset(){
        SMSMessage constructed = new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setIdToFind(ID1)
                .reset()
                .setPeer(PEER2)
                .setRequestType(REQUEST2)
                .setIdToFind(ID2)
                .setSearcher(PEER1)
                .setKey(KEY1)
                .setResource(RESOURCE1)
                .buildMessage();
        String expectedMessage = REQUEST2.ordinal() + " " + ID2
                + " " + PEER1 + " " + KEY1 + " " + RESOURCE1;
        assertEquals(constructed.getPeer(), PEER2);
        assertEquals(constructed.getData(), expectedMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructInvalidMessage_withReset(){
        new KademliaMessage()
                .setPeer(PEER1)
                .setRequestType(REQUEST1)
                .setIdToFind(ID1)
                .setKey(KEY1)
                .setResource(RESOURCE1)
                .reset()
                .buildMessage();
    }
}
