package com.eis0.kademlianetwork.ActivityStatus;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.InformationDeliveryManager.KademliaMessageBuilder;
import com.eis0.kademlianetwork.InformationDeliveryManager.RequestTypes;

/**
 * This class contains all the methods that send system messages
 *
 * @author Edoardo Raimondi
 */

public class SystemMessages {

    /**
     * Sends an acknowledge message
     *
     * @param peer The {@link SMSPeer} of the node that contacted me
     */
    public static final void sendAcknowledge(SMSPeer peer) {
        SMSMessage acknowledgeMessage = new KademliaMessageBuilder()
                .setPeer(peer)
                .setCommand(RequestTypes.AcknowledgeMessage)
                .addArguments(null, null, null, null)
                .buildMessage();
        SMSManager.getInstance().sendMessage(acknowledgeMessage);
    }

    /**
     * Sends a pong message
     *
     * @param peer The {@link SMSPeer} of the node that contacted me
     */
    public static final void sendPong(SMSPeer peer) {
        SMSMessage pongMessage = new KademliaMessageBuilder()
                .setPeer(peer)
                .setCommand(RequestTypes.Pong)
                .addArguments(null, null, null, null)
                .buildMessage();
        SMSManager.getInstance().sendMessage(pongMessage);
    }

    /**
     * Sends a ping message
     *
     * @param receiver The {@link SMSKademliaNode} represented by peer that I'm looking for
     */
    public static final void sendPing(SMSKademliaNode receiver) {
        SMSMessage pingMessage = new KademliaMessageBuilder()
                .setPeer(receiver.getPeer())
                .setCommand(RequestTypes.Ping)
                .addArguments(null, null, null, null)
                .buildMessage();
        SMSManager.getInstance().sendMessage(pingMessage);
    }

}
