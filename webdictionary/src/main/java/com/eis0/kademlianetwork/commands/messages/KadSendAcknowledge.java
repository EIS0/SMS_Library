package com.eis0.kademlianetwork.commands.messages;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.netinterfaces.commands.Command;

/**
 * Command to send an acknowledge message
 */
public class KadSendAcknowledge extends Command {

    private SMSPeer peer;

    /**
     * Constructor for the KadSendAcknowledge command
     *
     * @param peer The {@link SMSPeer} of the node that contacted me
     */
    public KadSendAcknowledge(SMSPeer peer) {
        this.peer = peer;
    }

    /**
     * Sends an acknowledge message
     */
    protected void execute() {
        SMSMessage acknowledgeMessage = new KademliaMessage()
                .setPeer(peer)
                .setRequestType(RequestTypes.AcknowledgeMessage)
                .buildMessage();
        SMSManager.getInstance().sendMessage(acknowledgeMessage);
    }
}
