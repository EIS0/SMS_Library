package com.eis0.kademlianetwork.commands;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis0.kademlianetwork.KademliaInvitation;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.netinterfaces.commands.SendInvitation;

public class KadSendInvitation extends SendInvitation<KademliaInvitation> {

    /**
     * Constructor for the SendInvitation command, requires data to work
     *
     * @param invitation The Invitation containing the info of the network
     */
    public KadSendInvitation(@NonNull KademliaInvitation invitation) {
        super(invitation);
    }

    /**
     * Execute the SendInvitation logic: sends a request to join a network
     */
    protected void execute(){
        SMSMessage message = new KademliaMessage()
                .setPeer(invitation.getInviterPeer())
                .setRequestType(RequestTypes.JoinPermission)
                .buildMessage();
        SMSManager.getInstance().sendMessage(message);
    }
}
