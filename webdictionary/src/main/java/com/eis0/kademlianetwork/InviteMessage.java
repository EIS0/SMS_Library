package com.eis0.kademlianetwork;

import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.Message;
import com.eis0.smslibrary.SMSPeer;

/**
 *
 * Message to invite someone to my network
 * It's supposed that the invited node to send an acknowledge message as respond
 */
public class InviteMessage implements Message {

    private static final byte CODE = 0x01;
    private SMSKademliaNode from;

    /**
     *
     * @param from node that is inviting someone
     */
    public InviteMessage(SMSKademliaNode from){
        this.from = from;
    }

    /**
     * @return node from peer
     */
    public SMSPeer getPeer(){ return from.getNodePeer(); }

    /**
     * @return byte code
     */
    public byte getCode(){ return this.CODE; }

    /**
     * @return String representing the message
     */
    public String getData(){ return "Node to connect"; }

}
