package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlianetwork.informationdeliverymanager.MessageBuilder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageBuilderTest {

    private final SMSPeer VALID_PEER = new SMSPeer("+393423541601");
    private final String BLANK = "/";
    private final String FAKE_ID = "ABCDEF0123456789";
    private final String EXPECTED_STRING = FAKE_ID + " " + BLANK + " " + BLANK;

    @Test
    public void buildMessage_everythingCorrect() {
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER, EXPECTED_STRING);
        SMSMessage actualMessage = new MessageBuilder()
                .setPeer(VALID_PEER)
                .addArguments(FAKE_ID, BLANK, BLANK)
                .buildMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void buildMessage_nullValues() {
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER, EXPECTED_STRING);
        SMSMessage actualMessage = new MessageBuilder()
                .setPeer(VALID_PEER)
                .addArguments(FAKE_ID, null, BLANK)
                .buildMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void buildMessage_emptyValues() {
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER, EXPECTED_STRING);
        SMSMessage actualMessage = new MessageBuilder()
                .setPeer(VALID_PEER)
                .addArguments(FAKE_ID, "", BLANK)
                .buildMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void buildMessage_multiplePasses() {
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER, EXPECTED_STRING);
        SMSMessage actualMessage = new MessageBuilder()
                .setPeer(VALID_PEER)
                .addArguments(FAKE_ID)
                .addArguments("", BLANK)
                .buildMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test(expected = IllegalStateException.class)
    public void buildMessage_noPeerError() {
        new MessageBuilder().buildMessage();
    }

    @Test(expected = IllegalStateException.class)
    public void buildMessage_noMessageError() {
        new MessageBuilder().setPeer(VALID_PEER).buildMessage();
    }

    @Test(expected = IllegalStateException.class)
    public void resetBuilding_noPeerError(){
        new MessageBuilder().setPeer(VALID_PEER).addArguments(FAKE_ID)
                .reset().buildMessage();
    }

    @Test(expected = IllegalStateException.class)
    public void resetBuilding_noMessageError(){
        new MessageBuilder().setPeer(VALID_PEER).addArguments(FAKE_ID)
                .reset().setPeer(VALID_PEER).buildMessage();
    }

    @Test()
    public void resetBuilding_fullMessage(){
        new MessageBuilder().setPeer(VALID_PEER).addArguments(FAKE_ID)
                .reset().setPeer(VALID_PEER)
                .addArguments(FAKE_ID, BLANK, BLANK).buildMessage();
    }
}
