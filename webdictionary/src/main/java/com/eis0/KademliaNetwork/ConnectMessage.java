package com.eis0.KademliaNetwork;


import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.Message;
import com.eis0.smslibrary.SMSPeer;

/**
 * Class to create connection request messages
 * A connection request message is sent from a node asking to connect to another node.
 *
 * @author Edoardo Raimondi
 */
public class ConnectMessage implements Message {

    private SMSKademliaNode nodeToConnect;
    public static final byte CODE = 0x02;

    public ConnectMessage(SMSKademliaNode origin) {
        this.nodeToConnect = origin;
    }

    /**
     * Returns the Node asking for a connection
     *
     * @return The SMSKademliaNode object contained in the ConnectMessage
     */
    public SMSKademliaNode getNode() {
        return this.nodeToConnect;
    }


    /**
     * Returns the Peer of the Node asking for a connection
     *
     * @return The SMSPeer object of the nodeToConnect, that is the SMSKademliaNode object sent
     * inside the ConnectMessage
     */
    public SMSPeer getPeer() {
        return nodeToConnect.getNodePeer();
    }


    /**
     * Returns the message Data, a String value representing the Node asking for a connection
     *
     * @return The String value representing the nodeToConnect object inside the ConnectMessage
     */
    public String getData() {
        return this.nodeToConnect.toString();
    }


    /**
     * Returns the Code of the ConnectMessage
     *
     * @return The byte containing the code of the ConnectMessage
     */
    public byte code() {
        return CODE;
    }


    /**
     * Returns a String representing the Node
     *
     * @return The String value containing the ID of the node that originated the ConnectMessage,
     * written in a format understandable to the user
     */
    @Override
    public String toString() {
        return "ConnectMessage[origin NodeId=" + nodeToConnect.getNodeId() + "]";
    }
}