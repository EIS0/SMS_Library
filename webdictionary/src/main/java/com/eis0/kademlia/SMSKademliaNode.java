package com.eis0.kademlia;

import androidx.annotation.NonNull;

import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SMSNetVocabulary;

import java.io.Serializable;

/**
 * A Node in the Kademlia network - Contains basic node network information.
 *
 * @see <a href="https://pdos.csail.mit.edu/~petar/papers/maymounkov-kademlia-lncs.pdf">Kademlia's
 *      paper</a> for more details.
 */
public class SMSKademliaNode implements Serializable {

    private KademliaId nodeId;
    private SMSPeer phoneNumber;
    private SMSKademliaRoutingTable RoutingTable;
    private static SMSNetVocabulary dictionary;

    /**
     * Stores information of a Kademlia node.
     *
     * @param nodeId        The node's ID.
     * @param phoneNumber   The phone associated to the node, replaces its IP address in our
     *                      implementation.
     */
    public SMSKademliaNode(KademliaId nodeId, SMSPeer phoneNumber, SMSNetVocabulary dictionary) {
        this.nodeId = nodeId;
        this.phoneNumber = phoneNumber;
        RoutingTable = new SMSKademliaRoutingTable(this, new DefaultConfiguration());
        this.dictionary = dictionary;
    }

    /**
     * @return This node's ID.
     */
    public KademliaId getNodeId() { return this.nodeId; }

    /**
     * @return this node's dictionary
     */
    public SMSNetVocabulary getDictionary(){
        return this.dictionary;
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