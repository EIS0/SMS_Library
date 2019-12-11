package com.eis0.kademlianetwork;

import com.eis0.kademlia.KadConfiguration;
import com.eis0.kademlia.SMSKademliaNode;

import java.util.List;

/**
 * Receives a NodeLookupMessage
 * It's supposed to be sent a NodeReplyMessage as reply with the K-Closest nodes to the ID sent.
 * N.B. All "receivers" has to be manage in a listener
 *
 * TO CHECK THE USEFULNESS
 *
 * @author Edoardo Raimondi
 */
public class NodeLookupReceiver {

    private final KademliaNetwork network = KademliaNetwork.getInstance();
    private final KadConfiguration config;

    public NodeLookupReceiver(KadConfiguration config) {
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
        network.getLocalRoutingTable().insert(origin);

        /* Find nodes closest to the LookupId */
        List<SMSKademliaNode> nodes = network.getLocalRoutingTable().findClosest(incoming.getLookupId(), this.config.k());
    }
}
