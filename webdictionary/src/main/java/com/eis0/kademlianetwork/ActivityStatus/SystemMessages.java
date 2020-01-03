package com.eis0.kademlianetwork.ActivityStatus;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.InformationDeliveryManager.KademliaMessage;
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
        KademliaMessage acknowledge = new KademliaMessage(RequestTypes.AcknowledgeMessage, null, null, null, null);
        SMSMessage acknowledgeMessage = new SMSMessage(peer, acknowledge.toString());
        SMSManager.getInstance().sendMessage(acknowledgeMessage);
    }

    /**
     * Sends a pong message
     *
     * @param peer The {@link SMSPeer} of the node that contacted me
     */
    public static final void sendPong(SMSPeer peer) {
        KademliaMessage pong = new KademliaMessage(RequestTypes.Pong, null, null, null, null);
        SMSMessage pongMessage = new SMSMessage(peer, pong.toString());
        SMSManager.getInstance().sendMessage(pongMessage);
    }

    /**
     * Sends a ping message
     *
     * @param receiver The {@link SMSKademliaNode} represented by peer that I'm looking for
     */
    public static final void sendPing(SMSKademliaNode receiver) {
        KademliaMessage ping = new KademliaMessage(RequestTypes.Ping, null, null, null, null);
        SMSMessage pingMessage = new SMSMessage(receiver.getPeer(), ping.toString());
        SMSManager.getInstance().sendMessage(pingMessage);
    }

}
