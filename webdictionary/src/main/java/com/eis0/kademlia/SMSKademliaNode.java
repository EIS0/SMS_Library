package com.eis0.kademlia;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSPeer;

import java.io.Serializable;

/**
 * A Node in the Kademlia network. It contains basic node network information.
 *
 * @see <a href="https://pdos.csail.mit.edu/~petar/papers/maymounkov-kademlia-lncs.pdf">Kademlia's
 * paper</a> for more details.
 * @author Edoardo Raimondi
 * @author Marco Cognolato
 * @author Matteo Carnelos
 */
public class SMSKademliaNode implements Serializable {

    private final KademliaId id;
    private SMSPeer peer;

    /**
     * Create a Node in a traditional way
     * in order to always have a generic method to create a node
     *
     * @param id KademliaId that identify the node
     *
     * @author Edoardo Raimondi
     */
    public SMSKademliaNode(KademliaId id){ this.id = id; }

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
     * @return Returns this node id
     *
     * @author Marco Cognloato
     */
    public KademliaId getId() {
        return this.id;
    }

    /**
     * @return Returns this node's SMSPeer if it's present, else returns null.
     *
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
