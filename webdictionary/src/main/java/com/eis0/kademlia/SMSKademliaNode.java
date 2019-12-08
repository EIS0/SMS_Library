package com.eis0.kademlia;

import androidx.annotation.NonNull;

import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SMSNetVocabulary;

import org.w3c.dom.Node;

import java.io.Serializable;

/**
 * A Node in the Kademlia network - Contains basic node network information.
 *
 * @see <a href="https://pdos.csail.mit.edu/~petar/papers/maymounkov-kademlia-lncs.pdf">Kademlia's
 * paper</a> for more details.
 * @author Edoardo Raimondi, edits by Giovanni Velludo and Marco Cognolato
 */
public class SMSKademliaNode implements Serializable {

    private KademliaId nodeId;
    private SMSKademliaRoutingTable RoutingTable;
    private static SMSNetVocabulary dictionary;
    private SMSPeer nodePeer;


    /**
     * Stores information of a Kademlia node.
     *
     * @param nodeId        The node's ID.
     * @param phoneNumber   The phone associated to the node, replaces its IP address in our
     *                      implementation.
     */
    public SMSKademliaNode(KademliaId nodeId, SMSPeer phoneNumber, SMSNetVocabulary dictionary) {
        this.nodeId = nodeId;
        this.nodePeer = phoneNumber;
        RoutingTable = new SMSKademliaRoutingTable(this, new DefaultConfiguration());
        this.dictionary = dictionary;
    }

    /**
     * @return This node's ID.
     */
    public KademliaId getNodeId() {
        return this.nodeId;
    }

    /**
     * @return this node's dictionary
     */

    public SMSNetVocabulary getDictionary() {
        return this.dictionary;
    }

    /**
     * @return The node's Peer
     */
    public SMSPeer getNodePeer() {
        return nodePeer;
    }

    /**
     * @return Node's routing table
     */
    public SMSKademliaRoutingTable getRoutingTable(){ return this.RoutingTable; }

    /**
     * Method to compare two Nodes
     * Compares if two objects are equal or not.
     * Two Kademlia Nodes are said to be equal if they have the same Id
     *
     * @param toCompare Object to compare
     * @return True if they are equal, false otherwise
     */
    @Override
    public boolean equals(Object toCompare) {
        if (!(toCompare instanceof SMSKademliaNode)) {
            return false;
        }
        SMSKademliaNode n = (SMSKademliaNode) toCompare;
        return this.getNodeId().equals(n.getNodeId());
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