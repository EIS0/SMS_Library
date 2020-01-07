package com.eis0.kademlianetwork.activitystatus;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.MessageBuilder;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;

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
        SMSMessage acknowledgeMessage = new KademliaMessage()
                .setPeer(peer)
                .setRequestType(RequestTypes.AcknowledgeMessage)
                .buildMessage();
        SMSManager.getInstance().sendMessage(acknowledgeMessage);
    }

    /**
     * Sends a pong message
     *
     * @param peer The {@link SMSPeer} of the node that contacted me
     */
    public static void sendPong(SMSPeer peer) {
        SMSMessage pongMessage = new KademliaMessage()
                .setPeer(peer)
                .setRequestType(RequestTypes.Pong)
                .buildMessage();
        SMSManager.getInstance().sendMessage(pongMessage);
    }

    /**
     * Sends a ping message
     *
     * @param receiver The {@link SMSKademliaNode} represented by peer that I'm looking for
     */
    public static void sendPing(SMSKademliaNode receiver) {
        SMSMessage pingMessage = new KademliaMessage()
                .setPeer(receiver.getPeer())
                .setRequestType(RequestTypes.Ping)
                .buildMessage();
        SMSManager.getInstance().sendMessage(pingMessage);
    }

}
