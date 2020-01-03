package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.SMSKademliaNode;

/**
 * This class contains all the methods that send system messages
 *
 * @author Edoardo Raimondi
 */

public class SystemMessages {

    /**
     * Sends an acknowledge message
     *
     * @param  peer The {@link SMSPeer} of the node that contacted me
     */
    public static final void sendAcknowledge(SMSPeer peer) {
        String message = RequestTypes.AcknowledgeMessage.ordinal() + " ";
        SMSMessage acknowledgeMessage = new SMSMessage(peer, message);
        SMSManager.getInstance().sendMessage(acknowledgeMessage);
    }

    /**
     * Sends a pong message
     *
     * @param  peer The {@link SMSPeer} of the node that contacted me
     */
    public static final void sendPong(SMSPeer peer){
        String pong = RequestTypes.Pong.ordinal() + " ";
        SMSMessage pongMessage = new SMSMessage(peer, pong);
        SMSManager.getInstance().sendMessage(pongMessage);
    }

    /**
     * Sends a ping message
     *
     * @param receiver The {@link SMSKademliaNode} represented by peer that I'm looking for
     */
    public static final void sendPing(SMSKademliaNode receiver){
        String pingMessage = RequestTypes.Ping.ordinal() + " ";
        SMSMessage ping = new SMSMessage(receiver.getPeer(), pingMessage);
        SMSManager.getInstance().sendMessage(ping);
    }

}
