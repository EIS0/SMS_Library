package com.eis0.kademlianetwork;

import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.Message;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

/**
 * Class to create acknowledge messages
 * The acknowledge message is a generic response message, whose purpose is to inform another node
 * about the occurrence of a specific event
 * Example: A content arrives to destination, the receiving node will send an acknowledge
 * message to inform the sending node about the delivery completed
 *
 * @author Edoardo Raimondi
 */

public class AcknowledgeMessage extends SMSMessage {

    private SMSKademliaNode localNode;
    private static final byte CODE = 0x02;


    /**
     * @param origin Node that's is sending the message
     */
    public AcknowledgeMessage(SMSKademliaNode origin) {
        super(origin.getNodePeer(), "Acknowledge x");
        this.localNode = origin;
    }

    /**
     * Returns the Peer of the localNode stored inside the AcknowledgeMessage
     *
     * @return The SMSPeer object belonging to the localNode, a SMSKademliaNode object stored inside
     * the AcknowledgeMessage object
     */
    @Override
    public SMSPeer getPeer() {
        return localNode.getNodePeer();
    }


    /**
     * Returns the localNode value of the AcknowledgeMessage object
     *
     * @return The SMSKademliaNode object carried by the AcknowledgeMessage
     */
    public SMSKademliaNode getNode() {
        return this.localNode;
    }


    /**
     * Returns a String value representing the localNode
     *
     * @return The String value representing the localNode, a SMSKademliaNode object inside carried
     * inside the AcknowledgeMessage
     */
    @Override
    public String getData() {
        return this.localNode.toString();
    }


    /**
     * Returns the Code of the AcknowledgeMessage
     *
     * @return The byte containing the Code of the AcknowledgeMessage
     */
    public byte code() {
        return CODE;
    }


    /**
     * Returns a String representing the ID of the node that originated the AcknowledgeMessage
     *
     * @return The String value containing the ID of the node that originated the AcknowledgeMessage,
     * written in a format understandable to the user
     */
    @Override
    public String toString() {
        return "ConnectMessage[origin NodeId=" + localNode.getNodeId() + "]";
    }

}

