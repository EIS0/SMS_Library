package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlianetwork.InformationDeliveryManager.KademliaMessage;
import com.eis0.kademlianetwork.InformationDeliveryManager.RequestTypes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KademliaMessageText {

    public final SMSPeer VALID_PEER = new SMSPeer("+393423541601");

    @Test
    public void buildMessage(){
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER,
                RequestTypes.AcknowledgeMessage.ordinal() + " " +
                        "ABCDEF0123456789 / /"
        );
        SMSMessage actualMessage = new KademliaMessage()
                                    .setPeer(VALID_PEER)
                                    .setCommand(RequestTypes.AcknowledgeMessage)
                                    .addArguments("ABCDEF0123456789", "/", "/")
                                    .buildMessage();
        assertEquals(actualMessage, expectedMessage);
    }

}
