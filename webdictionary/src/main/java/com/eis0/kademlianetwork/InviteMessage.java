package com.eis0.kademlianetwork;

import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

/**
 *
 * Message to invite someone to my network
 * It's supposed that the invited node to send an acknowledge message as respond
 */
public class InviteMessage extends SMSMessage {

    private static final byte CODE = 0x01;
    private SMSKademliaNode from;

    /**
     *
     * @param from node that is inviting someone
     */
    public InviteMessage(SMSKademliaNode from){
        super(from.getNodePeer(), "InviteMessage x");
        this.from = from;
    }

    /**
     * @return node from peer
     */
    @Override
    public SMSPeer getPeer(){ return from.getNodePeer(); }

    /**
     * @return node sending this message
     */
    public SMSKademliaNode getNode(){ return from; }


    /**
     * @return byte code
     */
    public byte getCode(){ return this.CODE; }

    /**
     * @return String representing the message
     */
    @Override
    public String getData(){ return "Node to connect"; }

}
