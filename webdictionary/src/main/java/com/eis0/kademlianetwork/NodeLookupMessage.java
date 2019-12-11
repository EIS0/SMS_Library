package com.eis0.kademlianetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;


/**
 * Class to create Lookup messages
 * A lookup message is sent to other nodes to request which are the K nodes closest to the key sent
 * within the message itself.
 *
 * @author Edoardo Raimondi
 */
public class NodeLookupMessage extends SMSMessage {

    private SMSKademliaNode origin;
    private KademliaId lookupId;

    public static final byte CODE = 0x05;


    /**
     * The NodeLookupMessage constructor
     *
     * @param origin The Node from which the message is coming from
     * @param lookup The key upon which the lookup is carried out
     */
    public NodeLookupMessage(SMSKademliaNode origin, KademliaId lookup) {
        super(origin.getPeer(), "NodeLookupMessage x");
        this.origin = origin;
        this.lookupId = lookup;
    }


    /**
     * Returns the peer of the node which originated the lookup
     *
     * @return The SMSPeer object of the origin node, that is the SMSKademliaNode object sent
     * inside the NodeLookupMessage, representing the node which originated the lookup
     */
    @Override
    public SMSPeer getPeer() {
        return origin.getPeer();
    }


    /**
     * Return the node's data
     *
     * @return String
     */
    @Override
    public String getData() {
        return origin.toString();
    }


    /**
     * Return the node
     *
     * @return SMSKademliaNode
     */
    public SMSKademliaNode getNode() {
        return this.origin;
    }


    /**
     * Return the key
     *
     * @return KademliaId
     */

    public KademliaId getLookupId() {
        return this.lookupId;
    }


    /**
     * Return the code
     *
     * @return code
     */
    public byte code() {
        return this.CODE;
    }


    /**
     * Return a String representing the node
     *
     * @return String
     */
    @Override
    public String toString() {
        return "NodeLookupMessage[origin=" + origin + ",lookup=" + lookupId + "]";
    }
}
