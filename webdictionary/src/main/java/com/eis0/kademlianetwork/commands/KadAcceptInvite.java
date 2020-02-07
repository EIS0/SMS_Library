package com.eis0.kademlianetwork.commands;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis0.kademlianetwork.KademliaInvitation;
import com.eis0.kademlianetwork.KademliaNetwork;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.netinterfaces.commands.AcceptInvite;
import com.eis0.netinterfaces.commands.CommandExecutor;

/**
 * Command to accept an invite when it's received
 *
 * @author Marco Cognolato
 */
public class KadAcceptInvite extends AcceptInvite<KademliaInvitation> {

    private KademliaNetwork kademliaNetwork;

    /**
     * Constructor for AcceptInvite command, requires data to work
     *
     * @param invitation The Invitation to a network
     */
    public KadAcceptInvite(KademliaInvitation invitation, KademliaNetwork kademliaNetwork) {
        super(invitation);
        this.kademliaNetwork = kademliaNetwork;
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
        CommandExecutor.execute(new KadAddPeer(invitation.getInviterPeer(), kademliaNetwork));
    }
}
