package com.eis0.kademlianetwork;

import androidx.annotation.NonNull;

import com.eis0.netinterfaces.FailReason;

public class KademliaFailReason extends FailReason {

    /**
     * Generic failure.
     */
    public static final KademliaFailReason GENERIC_FAIL = new KademliaFailReason("GenericFail");

    /**
     * Failure when a Peer is already in the network and you're trying to invite him
     */
    public static final KademliaFailReason PEER_IN_NET = new KademliaFailReason("PeerAlreadyInNetwork");

    /**
     * Private constructor as suggested in the TypeSafe enum pattern.
     *
     * @param name The name of the enumeration.
     */
    protected KademliaFailReason(final @NonNull String name) {
        super(name);
    }
}
