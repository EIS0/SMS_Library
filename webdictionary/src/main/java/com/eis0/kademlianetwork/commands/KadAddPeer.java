package com.eis0.kademlianetwork.commands;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.KademliaNetwork;
import com.eis0.netinterfaces.commands.AddPeer;

public class KadAddPeer extends AddPeer<SMSPeer> {

    /**
     * AddPeer command constructor, receives the data it needs to operate on.
     *
     * @param peer           The Peer to add to the network
     * @param kademliaNetwork The kademlia network used by the command to add the peer to
     */
    public KadAddPeer(@NonNull SMSPeer peer, @NonNull KademliaNetwork kademliaNetwork) {
        super(peer, kademliaNetwork);
    }

    /**
     * Adds the peer to the subscribers list and broadcasts it to the net
     */
    protected void execute(){
        SMSKademliaNode node = new SMSKademliaNode(peer);
        kademliaNetwork.addNodeToTable(node);
        kademliaNetwork.updateTable();
    }
}
