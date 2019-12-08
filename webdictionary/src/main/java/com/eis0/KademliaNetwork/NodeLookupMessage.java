package com.eis0.KademliaNetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.Message;
import com.eis0.smslibrary.SMSPeer;


/**
 * A message sent to other nodes requesting the K-Closest nodes to a key sent in this message.
 *
 */
public class NodeLookupMessage implements Message
{

    private SMSKademliaNode origin;
    private KademliaId lookupId;

    public static final byte CODE = 0x05;

    /**
     * A new NodeLookupMessage to find nodes
     *
     * @param origin The Node from which the message is coming from
     * @param lookup The key for which to lookup nodes for
     */
    public NodeLookupMessage(SMSKademliaNode origin, KademliaId lookup)
    {
        this.origin = origin;
        this.lookupId = lookup;
    }

    /**
     * Return the node peer
     * @return SMSPeer
     */
    public SMSPeer getPeer(){
        return origin.getNodePeer();
    }

    /**
     * Return the node's data
     * @return String
     */
    public String getData(){
        return origin.toString();
    }

    /**
     * Return the node
     * @return SMSKademliaNode
     */
    public SMSKademliaNode getNode()
    {
        return this.origin;
    }

    /**
     * Return the key
     * @return KademliaId
     */

    public KademliaId getLookupId()
    {
        return this.lookupId;
    }

    /**
     * Return the code
     * @return code
     */
    public byte code()
    {
        return CODE;
    }

    /**
     * Return a String representing the node
     * @return String
     */
    @Override
    public String toString()
    {
        return "NodeLookupMessage[origin=" + origin + ",lookup=" + lookupId + "]";
    }
}
