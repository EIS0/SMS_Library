package com.eis0.kademlia;

import androidx.annotation.NonNull;

import com.eis0.smslibrary.SMSPeer;

import java.io.Serializable;

/**
 * A Node in the Kademlia network. It contains basic node network information.
 *
 * @see <a href="https://pdos.csail.mit.edu/~petar/papers/maymounkov-kademlia-lncs.pdf">Kademlia's
 * paper</a> for more details.
 * @author Edoardo Raimondi
 * @author Marco Cognolato
 * @author Matteo Carnelos
 * @author Enrico Cestaro
 */
public class SMSKademliaNode implements Serializable {

    private KademliaId id;
    private SMSPeer peer;

    /**
     * Stores information of a Kademlia node.
     *
     * @param phoneNumber The phone associated to the node, replaces its IP address in out
     *                    implementation.
     * @author Marco Cognolato
     * @author Matteo Carnelos
     */
    public SMSKademliaNode(SMSPeer phoneNumber) {
        this.id = new KademliaId(phoneNumber);
        this.peer = phoneNumber;
    }


    /**
     * Get the local node id.
     *
     * @return This node's Id.
     * @author Marco Cognloato
     */
    public KademliaId getId() {
        return this.id;
    }


    /**
     * Get the local peer.
     *
     * @return The node's Peer.
     * @author Edoardo Raimondi
     * @author Matteo Carnelos
     */
    public SMSPeer getPeer() {
        return peer;
    }


    /**
     * Method to compare two Nodes.
     * Two KademliaNodes are said to be equal if they have the same Id.
     *
     * @param obj Object to compare.
     * @return True if they are equal, false otherwise.
     * @author Marco Cognolato
     * @author Matteo Carnelos
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SMSKademliaNode)) return false;
        SMSKademliaNode toCompare = (SMSKademliaNode)obj;
        return this.getId().equals(toCompare.getId());
    }


    /**
     * @return NodeId's hashCode
     */
    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }


    /**
     * Convert the NodeId to a string.
     *
     * @return A String containing the node id.
     * @author Edoardo Raimondi
     * @author Matteo Carnelos
     */
    @Override
    @NonNull
    public String toString() {
        return this.getId().toString();
    }
}
