package com.eis0.kademlianetwork;

import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;

/**
 * Central class fo the KademliaNetwork. Instead of handling everything itself,
 * calls the proper handler. This is basically the
 * <a href="https://refactoring.guru/design-patterns/chain-of-responsibility">Chain Of Responsibility</a>
 * Design Pattern, where this class instead of handling everything
 * itself (which would make it a really big class), redirects the work on the proper handler.
 */
public class KademliaNetwork {

    //User node of the network
    private SMSKademliaNode node;
    //Routing table for this user of the network
    private SMSKademliaRoutingTable table;

    //Constructor following the Singleton Design Pattern
    private KademliaNetwork(){ }

    /**
     * @return Returns a single KademliaNetwork instance as per the
     * <a href="https://refactoring.guru/design-patterns/singleton">Singleton Design Pattern</a>
     */
    public static KademliaNetwork getInstance(){
        return null;
    }

    /**
     * Request Types for the Kademlia Network
     */
    public enum RequestType{
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
}
