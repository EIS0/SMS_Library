package com.eis0.kademlianetwork;

import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SerializableObject;

/**
 * Message containing a content to show
 * (It's supposed to be a respond to a specific ViewContentMessage)
 */

public class ContentMessage extends SMSMessage {

    private SMSKademliaNode from;
    private SerializableObject content;
    private static final byte CODE = 0x05;

    /**
     * @param from Node sending that content
     * @param content The content to be sent
     */
    public ContentMessage(SMSKademliaNode from, SerializableObject content){
        super(from.getNodePeer(), "ContentMessage x");
        this.from = from;
        this.content = content;
    }

    /**
     * @return Content to be visualized by another node
     */
    public SerializableObject getContent(){ return this.content; }

    /**
     * @return Peer of the node
     */
    @Override
    public SMSPeer getPeer() { return this.from.getNodePeer();
    }

    /**
     * @return byte Code
     */
    public byte getCode() { return this.CODE; }


    /**
     * @return String representing the message
     */
    @Override
    public String getData() {
        return super.getData() + " with the following content : " + content.toString();
    }
}
