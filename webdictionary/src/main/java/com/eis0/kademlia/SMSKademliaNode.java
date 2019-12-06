package com.eis0.kademlia;

import androidx.annotation.NonNull;

import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SMSNetVocabulary;

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
    private SMSPeer nodePeer;
    private SMSNetVocabulary dictionary;

    /**
     * Stores information of a Kademlia node.
     *
     * @param nodeId      The node's ID.
     * @param phoneNumber The phone associated to the node, replaces its IP address in our
     *                    implementation.
     * @param dic         Node's portion of dictionary
     */
    public SMSKademliaNode(KademliaId nodeId, SMSPeer phoneNumber, SMSNetVocabulary dic) {
        this.nodeId = nodeId;
        this.nodePeer = phoneNumber;
        this.dictionary = dic;
    }

    /**
     * @return This node's ID.
     */
    public KademliaId getNodeId() {
        return this.nodeId;
    }

    /**
     * @return This node's portion of dictionary
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