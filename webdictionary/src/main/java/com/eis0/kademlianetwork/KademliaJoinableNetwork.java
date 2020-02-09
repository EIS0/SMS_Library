package com.eis0.kademlianetwork;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlianetwork.commands.KadAcceptInvite;
import com.eis0.netinterfaces.JoinableNetworkManager;
import com.eis0.netinterfaces.NetDictionary;
import com.eis0.netinterfaces.commands.CommandExecutor;
import com.eis0.netinterfaces.listeners.JoinInvitationListener;

/**
 * Implementatino of a Kademlia Joinable Network
 *
 * @author Marco Cognolato
 */
public class KademliaJoinableNetwork extends KademliaNetwork
        implements JoinableNetworkManager<String, String, SMSPeer, KademliaFailReason, KademliaInvitation> {

    private static KademliaJoinableNetwork instance;

    /**
     * Private constructor as per the Singleton Design Pattern
     */
    private KademliaJoinableNetwork() {

    }

    private JoinInvitationListener<KademliaInvitation> invitationListener;

    /**
     * Public static getInstance() as per the Singleton Design Pattern
     *
     * @return A single instance which can exist of KademliaJoinableNetwork
     */
    public static KademliaJoinableNetwork getInstance() {
        if (instance == null) instance = new KademliaJoinableNetwork();
        return instance;
    }

    /**
     * Method used to join the network after an invitation is received.
     *
     * @param invitation The invitation previously received.
     */
    public void acceptJoinInvitation(KademliaInvitation invitation) {
        CommandExecutor.execute(new KadAcceptInvite(invitation, KademliaJoinableNetwork.getInstance()));
    }

    /**
     * Sets the listener used to wait for invitations to join the network.
     *
     * @param joinInvitationListener Listener called upon invitation received.
     */
    public void setJoinInvitationListener(JoinInvitationListener<KademliaInvitation> joinInvitationListener) {
        this.invitationListener = joinInvitationListener;
    }

    /**
     * Checks if a listener is set, if it's not automatically accepts the invitation, else
     * informs the user of the invitation through the listener
     *
     * @param invitation The invitation for a network
     */
    public void checkInvitation(KademliaInvitation invitation) {
        if (invitationListener == null) acceptJoinInvitation(invitation);
        else invitationListener.onJoinInvitationReceived(invitation);
    }

    /**
     * Sets a given dictionary of resources, to provide the network
     * with your own implementation
     *
     * @param dictionary A NetDictionary of type <String,String> to provide
     */
    public void setNetDictionary(@NonNull NetDictionary<String, String> dictionary) {
        this.localKademliaDictionary = dictionary;
    }
}
