package com.eis0.KademliaNetwork;


import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.Message;
import com.eis0.smslibrary.SMSPeer;

/**
 * A message sent to another node requesting to connect to them.
 *
 */
public class ConnectMessage implements Message
{

    private SMSKademliaNode NodeToConnect;
    public static final byte CODE = 0x02;

    public ConnectMessage(SMSKademliaNode origin)
    {
        this.NodeToConnect = origin;
    }

    /**
     * Return the Node
     * @return SMSKademliaNode
     */
    public SMSKademliaNode getNode(){
        return this.NodeToConnect;
    }

    /**
     * Return the message peer
     * @return SMSpeer
     */
    public SMSPeer getPeer(){
        return NodeToConnect.getPhoneNumber();
    }


    /**
     * Return the data message
     * @return String of the node
     */
    public String getData(){
        return this.NodeToConnect.toString();
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
    @Override
    public String toString()
    {
        return "ConnectMessage[origin NodeId=" + NodeToConnect.getNodeId() + "]";
    }
}