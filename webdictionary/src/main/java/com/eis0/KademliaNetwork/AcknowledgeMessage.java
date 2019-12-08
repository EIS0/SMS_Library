package com.eis0.KademliaNetwork;

import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.Message;
import com.eis0.smslibrary.SMSPeer;

/**
 * General respond message
 */

public class AcknowledgeMessage implements Message {

    private SMSKademliaNode localNode;
    private static final byte CODE = 0x02;

    public AcknowledgeMessage(SMSKademliaNode origin) {
        this.localNode = origin;
    }

    /**
     * @return Node's peer
     */
    public SMSPeer getPeer() {
        return localNode.getNodePeer();
    }

    /**
     * Return the Node
     * @return SMSKademliaNode
     */
    public SMSKademliaNode getNode(){
        return this.localNode;
    }


    /**
     * Return String representing the node
     *
     * @return String
     */
    public String getData() {
        return this.localNode.toString();
    }

    /**
     * Return Code message
     * @return byte
     */
    public byte code()
    {
        return CODE;
    }

     /**
      * Return a String representing the Node
      * @return String
      */
     public String toString() {
         return "ConnectMessage[origin NodeId=" + localNode.getNodeId() + "]";
     }
}

