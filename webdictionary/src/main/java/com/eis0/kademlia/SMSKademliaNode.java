package com.eis0.kademlia;

import androidx.annotation.NonNull;

import com.eis0.smslibrary.SMSPeer;

import java.io.Serializable;

/**
 * A Node in the Kademlia network - Contains basic node network information.
 *
 * @see <a href="https://pdos.csail.mit.edu/~petar/papers/maymounkov-kademlia-lncs.pdf">Kademlia's
 *      paper</a> for more details.
 */
public class SMSKademliaNode implements Serializable {

    private KademliaId nodeId;
    private int port;
    private SMSPeer phoneNumber;
    private SMSKademliaRoutingTable RoutingTable;
    //TODO it's supposed a node should have a portion of a SMSNetDictionary

    /**
     * Stores information of a Kademlia node.
     *
     * @param nodeId        The node's ID.
     * @param port          The port used by the node for communication.
     * @param phoneNumber   The phone associated to the node, replaces its IP address in our
     *                      implementation.
     */
    public SMSKademliaNode(KademliaId nodeId, int port, SMSPeer phoneNumber) {
        this.nodeId = nodeId;
        this.port = port;
        this.phoneNumber = phoneNumber;
        RoutingTable = new SMSKademliaRoutingTable(this, new DefaultConfiguration());
    }

    /**
     * @return This node's ID.
     */
    public KademliaId getNodeId() { return this.nodeId; }

    /**
     * @return The receiving port of the node.
     */
    public int getPort() {
        return port;
    }

    /**
     * @return The node's phone number.
     */
    public SMSPeer getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return Node's routing table
     */
    public SMSKademliaRoutingTable getRoutingTable(){ return this.RoutingTable; }

    /**
     * Method to compare two Nodes
     *
     * @param toCompare Object to compare
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object toCompare) {
        if (toCompare instanceof SMSKademliaNode) {
            SMSKademliaNode n = (SMSKademliaNode) toCompare;
            if (n == this) {
                return true;
            }
            return this.getNodeId().equals(n.getNodeId());
        }
        return false;
    }

    /**
     * @return NodeId's hashCode
     */
    @Override
    public int hashCode() {
        return this.getNodeId().hashCode();
    }

    /**
     * Convert the NodeId to a string
     *
     * @return String
     */
    @Override
    @NonNull
    public String toString() {
        return this.getNodeId().toString();
    }
}