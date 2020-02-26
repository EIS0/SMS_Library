package com.eis0.kademlianetwork.commands.messages;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.netinterfaces.commands.Command;

/**
 * Command to send a pong message
 */
public class KadPong extends Command {

    private SMSPeer peer;

    /**
     * Constructor for the Pong command message
     *
     * @param peer The SMSPeer to send the pong to
     */
    public KadPong(SMSPeer peer) {
        this.peer = peer;
    }

    /**
     * Sends a Pong message
     */
    protected void execute() {
        SMSMessage pongMessage = new KademliaMessage()
                .setPeer(peer)
                .setRequestType(RequestTypes.Pong)
                .buildMessage();
        SMSManager.getInstance().sendMessage(pongMessage);
    }
}
