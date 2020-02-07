package com.eis0.netinterfaces.commands;

import androidx.annotation.NonNull;

import com.eis.communication.Peer;
import com.eis0.netinterfaces.NetSubscriberList;

/**
 * Command to add a peer to the Subscribers list
 *
 * @author Edoardo Raimondi
 * @author Marco Cognolato
 * @author Giovanni Velludo
 */
public abstract class AddPeer<T extends Peer> extends Command {

    protected final T peer;
    protected final NetSubscriberList<T> netSubscribers;

    /**
     * AddPeer command constructor, receives the data it needs to operate on.
     *
     * @param peer           The Peer to add to the network
     * @param netSubscribers The subscribers to notify of the newest member
     */
    public AddPeer(@NonNull T peer, @NonNull NetSubscriberList<T> netSubscribers) {
        this.peer = peer;
        this.netSubscribers = netSubscribers;
    }

    /**
     * Adds the peer to the subscribers list and broadcasts it to the net
     */
    protected abstract void execute();
}
