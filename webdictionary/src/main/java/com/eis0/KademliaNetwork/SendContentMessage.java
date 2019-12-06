package com.eis0.KademliaNetwork;

import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.Message;
import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SerializableObject;

/**
 * A message used by a node to send a content to another one
 */

public class SendContentMessage implements Message {

    private SMSKademliaNode from;
    private SerializableObject content;
    private SerializableObject key;
    private static final byte CODE = 0x01;

    /**
     * @param from Node where the content is from
     * @param content serializableObject to send
     * @param key serializableObject key
     */
    public SendContentMessage(SMSKademliaNode from, SerializableObject content, SerializableObject key) {
        this.from = from;
        this.content = content;
        this.key = key;
    }

    /**
     * @param ofNode SMSKademliaNode having the dictionary
     */
    public void setContent(SMSKademliaNode ofNode){
        ofNode.getDictionary().add(this.key, this.content);
    }

    /**
     * @return content that's is sent
     */
    public SerializableObject getContent(){
        return this.content;
    }

    /**
     * @return Peer of the node
     */
    public SMSPeer getPeer() {
        return this.from.getPhoneNumber();
    }

    /**
     * @return byte Code
     */
    public byte getCode(){
        return this.CODE;
    }

    /**
     * @return String representing the message
     */
    public String getData()
    {
        return "ContentMessage[origin=" + from + ",content=" + content + "]";
    }

}
