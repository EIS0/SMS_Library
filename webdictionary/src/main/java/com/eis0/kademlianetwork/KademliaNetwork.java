package com.eis0.kademlianetwork;

import com.eis0.kademlia.DefaultConfiguration;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;

/**
 * Central class fo the KademliaNetwork. Instead of handling everything itself,
 * calls the proper handler. This is basically the
 * <a href="https://refactoring.guru/design-patterns/chain-of-responsibility">Chain Of Responsibility</a>
 * Design Pattern, where this class instead of handling everything
 * itself (which would make it a really big class), redirects the work on the proper handler.
 *
 * @author Matteo Carnelos
 */
public class KademliaNetwork {

    private SMSKademliaNode localNode;
    private SMSKademliaRoutingTable localRoutingTable;

    // Request types for the Kademlia Network
    public enum RequestType {
        JoinPermission,
        AddPeers,
        RemovePeers,
        UpdatePeers,
        LeavePermission,
        AcceptLeave,
        AcknowledgeMessage,
        AddToDict,
        RemoveFromDict,
        UpdateDict,
        NodeLookup
    }

    // Singleton instance
    private static KademliaNetwork instance;
    // Constructor following the Singleton Design Pattern
    private KademliaNetwork() { }

    /**
     * Return an instance of KademliaNetwork.
     *
     * @return Returns a single KademliaNetwork instance as per the
     * <a href="https://refactoring.guru/design-patterns/singleton">Singleton Design Pattern</a>.
     *
     * @author Matteo Carnelos
     */
    public static KademliaNetwork getInstance() {
        if(instance == null) instance = new KademliaNetwork();
        return instance;
    }

    /**
     * Initialize the network by setting the current node.
     *
     * @param localNode The SMSKademliaNode to set.
     * @author Matteo Carnelos
     */
    public void init(SMSKademliaNode localNode) {
        this.localNode = localNode;
        localRoutingTable = new SMSKademliaRoutingTable(localNode, new DefaultConfiguration());
    }

    /**
     * Get the local node.
     *
     * @return The SMSKademliaNode representing the local node.
     * @author Matteo Carnelos
     */
    public SMSKademliaNode getLocalNode() {
        return localNode;
    }

    /**
     * Get the local routing table.
     *
     * @return The SMSKademliaRoutingTable representing the local routing table.
     * @author Matteo Carnelos
     */
    public SMSKademliaRoutingTable getLocalRoutingTable() {
        return localRoutingTable;
    }
}
