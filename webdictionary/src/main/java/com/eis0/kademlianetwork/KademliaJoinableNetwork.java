package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSPeer;
import com.eis0.netinterfaces.JoinableNetworkManager;
import com.eis0.netinterfaces.listeners.JoinInvitationListener;

public class KademliaJoinableNetwork extends KademliaNetwork
        implements JoinableNetworkManager<String, String, SMSPeer, KademliaFailReason, KademliaInvitation> {

    /**
     * Method used to join the network after an invitation in received.
     *
     * @param invitation The invitation previously received.
     */
    public void acceptJoinInvitation(KademliaInvitation invitation){

    }

    /**
     * Sets the listener used to wait for invitations to join the network.
     *
     * @param joinInvitationListener Listener called upon invitation received.
     */
    public void setJoinInvitationListener(JoinInvitationListener<KademliaInvitation> joinInvitationListener){

    }
}
