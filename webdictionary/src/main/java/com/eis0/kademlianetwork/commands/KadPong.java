package com.eis0.kademlianetwork.commands;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.netinterfaces.commands.Command;

public class KadPong extends Command {

    private SMSPeer peer;

    /**
     * Constructor for the Ping command message
     *
     * @param peer The SMSPeer to send the ping to
     */
    public KadPong(SMSPeer peer) {
        this.peer = peer;
    }

    /**
     * Sends a Ping message
     */
    protected void execute() {
        SMSMessage pongMessage = new KademliaMessage()
                .setPeer(peer)
                .setRequestType(RequestTypes.Pong)
                .buildMessage();
        SMSManager.getInstance().sendMessage(pongMessage);
    }
}
