package com.eis0.KademliaNetwork;

import com.eis0.kademlia.KadConfiguration;
import com.eis0.kademlia.SMSKademliaNode;

import java.util.List;

/**
 * Receives a NodeLookupMessage
 * It's supposed to be sent a NodeReplyMessage as reply with the K-Closest nodes to the ID sent.
 * N.B. All "receivers" has to be manage in a listener
 */
public class NodeLookupReceiver {


    private final SMSKademliaNode localNode;
    private final KadConfiguration config;

    public NodeLookupReceiver(SMSKademliaNode local, KadConfiguration config) {
        this.localNode = local;
        this.config = config;
    }

    /**
     * Handle receiving a NodeLookupMessage
     * Find the set of K nodes closest to the lookup ID and return them
     *
     * @param incoming the message asking for the set
     */
    public void receive(NodeLookupMessage incoming) {

        SMSKademliaNode origin = incoming.getNode();

        /* Update the local space by inserting the origin node. */
        this.localNode.getRoutingTable().insert(origin);

        /* Find nodes closest to the LookupId */
        List<SMSKademliaNode> nodes = this.localNode.getRoutingTable().findClosest(incoming.getLookupId(), this.config.k());

    }
}
