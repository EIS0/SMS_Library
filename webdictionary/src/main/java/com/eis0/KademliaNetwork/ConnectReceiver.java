package com.eis0.KademliaNetwork;

import com.eis0.kademlia.SMSKademliaNode;

/**
 * Receives a ConnectMessage and sends an AcknowledgeMessage as reply.
 * N.B. All "receivers" has to be manage in a listener
 *
 * @author Edoardo Raimondi
 */
public class ConnectReceiver {

    private final SMSKademliaNode localNode;

    public ConnectReceiver(SMSKademliaNode local) {
        this.localNode = local;
    }

    /**
     * Handle receiving a ConnectMessage
     */
    public void receive(ConnectMessage incoming) {

        /* Update the local space by inserting the origin node. */
        this.localNode.getRoutingTable().insert(incoming.getNode());

        /* Respond to the connect request */
        AcknowledgeMessage msg = new AcknowledgeMessage(this.localNode);

    }
}