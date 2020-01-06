package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessageBuilder;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KademliaMessageBuilderText {

    private final SMSPeer VALID_PEER = new SMSPeer("+393423541601");

    @Test
    public void buildMessage_everythingCorrect() {
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER,
                RequestTypes.AcknowledgeMessage.ordinal() + " " +
                        "ABCDEF0123456789 / /"
        );
        SMSMessage actualMessage = new KademliaMessageBuilder()
                .setPeer(VALID_PEER)
                .setCommand(RequestTypes.AcknowledgeMessage)
                .addArguments("ABCDEF0123456789", "/", "/")
                .buildMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void buildMessage_nullValues() {
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER,
                RequestTypes.AcknowledgeMessage.ordinal() + " " +
                        "ABCDEF0123456789 / /"
        );
        SMSMessage actualMessage = new KademliaMessageBuilder()
                .setPeer(VALID_PEER)
                .setCommand(RequestTypes.AcknowledgeMessage)
                .addArguments("ABCDEF0123456789", null, "/")
                .buildMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void buildMessage_emptyValues() {
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER,
                RequestTypes.AcknowledgeMessage.ordinal() + " " +
                        "ABCDEF0123456789 / /"
        );
        SMSMessage actualMessage = new KademliaMessageBuilder()
                .setPeer(VALID_PEER)
                .setCommand(RequestTypes.AcknowledgeMessage)
                .addArguments("ABCDEF0123456789", "", "/")
                .buildMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void buildMessage_multiplePasses() {
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER,
                RequestTypes.AcknowledgeMessage.ordinal() + " " +
                        "ABCDEF0123456789 / /"
        );
        SMSMessage actualMessage = new KademliaMessageBuilder()
                .setPeer(VALID_PEER)
                .setCommand(RequestTypes.AcknowledgeMessage)
                .addArguments("ABCDEF0123456789")
                .addArguments("", "/")
                .buildMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test(expected = IllegalStateException.class)
    public void buildMessage_noPeerError() {
        SMSMessage actualMessage = new KademliaMessageBuilder()
                .buildMessage();
    }

    @Test(expected = IllegalStateException.class)
    public void buildMessage_noMessageError() {
        SMSMessage actualMessage = new KademliaMessageBuilder()
                .setPeer(VALID_PEER)
                .buildMessage();
    }
}
