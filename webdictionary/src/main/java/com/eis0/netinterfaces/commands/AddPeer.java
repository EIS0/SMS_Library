package com.eis0.netinterfaces.commands;

import androidx.annotation.NonNull;

import com.eis.communication.Peer;
import com.eis0.kademlianetwork.KademliaNetwork;

/**
 * Command to add a peer to the Subscribers list
 *
 * @author Edoardo Raimondi
 * @author Marco Cognolato (edited comments)
 * @author Giovanni Velludo (added NonNull)
 */
public abstract class AddPeer<T extends Peer> extends Command {

    protected final T peer;
    protected final KademliaNetwork kademliaNetwork;

    /**
     * AddPeer command constructor, receives the data it needs to operate on.
     *
     * @param peer           The Peer to add to the network
     * @param kademliaNetwork The kademlia network used by the command to add the peer to
     */
    public AddPeer(@NonNull T peer, @NonNull KademliaNetwork kademliaNetwork) {
        this.peer = peer;
        this.kademliaNetwork = kademliaNetwork;
    }

    /**
     * Adds the peer to the subscribers list and broadcasts it to the net
     */
    protected abstract void execute();
}
