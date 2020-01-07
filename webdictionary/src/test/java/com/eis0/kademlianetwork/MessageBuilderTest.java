package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlianetwork.informationdeliverymanager.MessageBuilder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageBuilderTest {

    private final SMSPeer VALID_PEER = new SMSPeer("+393423541601");

    @Test
    public void buildMessage_everythingCorrect() {
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER,
                        "ABCDEF0123456789 / /"
        );
        SMSMessage actualMessage = new MessageBuilder()
                .setPeer(VALID_PEER)
                .addArguments("ABCDEF0123456789", "/", "/")
                .buildMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void buildMessage_nullValues() {
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER,
                        "ABCDEF0123456789 / /"
        );
        SMSMessage actualMessage = new MessageBuilder()
                .setPeer(VALID_PEER)
                .addArguments("ABCDEF0123456789", null, "/")
                .buildMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void buildMessage_emptyValues() {
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER,
                        "ABCDEF0123456789 / /"
        );
        SMSMessage actualMessage = new MessageBuilder()
                .setPeer(VALID_PEER)
                .addArguments("ABCDEF0123456789", "", "/")
                .buildMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void buildMessage_multiplePasses() {
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER,
                        "ABCDEF0123456789 / /"
        );
        SMSMessage actualMessage = new MessageBuilder()
                .setPeer(VALID_PEER)
                .addArguments("ABCDEF0123456789")
                .addArguments("", "/")
                .buildMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test(expected = IllegalStateException.class)
    public void buildMessage_noPeerError() {
        SMSMessage actualMessage = new MessageBuilder()
                .buildMessage();
    }

    @Test(expected = IllegalStateException.class)
    public void buildMessage_noMessageError() {
        SMSMessage actualMessage = new MessageBuilder()
                .setPeer(VALID_PEER)
                .buildMessage();
    }
}
