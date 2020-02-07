package com.eis0.kademlianetwork.commands;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.KademliaInvitation;
import com.eis0.kademlianetwork.KademliaJoinableNetwork;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.netinterfaces.commands.AcceptInvite;

/**
 * Command to accept an invite when it's received
 *
 * @author Marco Cognolato
 */
public class KadAcceptInvite extends AcceptInvite<KademliaInvitation> {

    /**
     * Constructor for AcceptInvite command, requires data to work
     *
     * @param invitation The Invitation to a network
     */
    public KadAcceptInvite(KademliaInvitation invitation) {
        super(invitation);
    }

    /**
     * Invites a peer to join a network
     */
    protected void execute(){
        /*
        Send an "invite accepted" notification to the other used
         */
        SMSMessage message = new KademliaMessage()
                .setPeer(invitation.getInviterPeer())
                .setRequestType(RequestTypes.AcceptJoin)
                .buildMessage();
        SMSManager.getInstance().sendMessage(message);

        //AddPeer procedure
        SMSKademliaNode node = new SMSKademliaNode(invitation.getInviterPeer());
        KademliaJoinableNetwork.getInstance().addNodeToTable(node);
        KademliaJoinableNetwork.getInstance().updateTable();
    }
}
