package com.eis0.kademlianetwork.commands.messages;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.netinterfaces.commands.Command;

/**
 * Command to send a ping message
 */

public class KadPing extends Command {

    private SMSPeer peer;

    /**
     * Constructor for the Ping command message
     *
     * @param peer The SMSPeer to send the ping to
     */
    public KadPing(SMSPeer peer) {
        this.peer = peer;
    }

    /**
     * Sends a Ping message
     */
    protected void execute() {
        SMSMessage pingMessage = new KademliaMessage()
                .setPeer(peer)
                .setRequestType(RequestTypes.Ping)
                .buildMessage();
        SMSManager.getInstance().sendMessage(pingMessage);
    }
}
