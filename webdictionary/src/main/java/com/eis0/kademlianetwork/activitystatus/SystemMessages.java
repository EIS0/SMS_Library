package com.eis0.kademlianetwork.activitystatus;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessageBuilder;

/**
 * This class contains all the methods that send system messages
 *
 * @author Edoardo Raimondi
 * @author edits by Giovanni Velludo
 */

public class SystemMessages {

    /**
     * Sends an acknowledge message
     *
     * @param peer The {@link SMSPeer} of the node that contacted me
     */
    public static void sendAcknowledge(SMSPeer peer) {
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
    public static void sendPong(SMSPeer peer) {
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
    public static void sendPing(SMSKademliaNode receiver) {
        SMSMessage pingMessage = new KademliaMessageBuilder()
                .setPeer(receiver.getPeer())
                .setCommand(RequestTypes.Ping)
                .addArguments(null, null, null, null)
                .buildMessage();
        SMSManager.getInstance().sendMessage(pingMessage);
    }

}
