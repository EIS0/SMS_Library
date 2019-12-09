package com.eis0.kademlianetwork;

import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SerializableObject;

/**
 * A message used by a node to send a content to another one
 * When a node receive this type of message, it's supposed to add the Key-Content
 * to his own portion of dictionary
 *
 * @author Edoardo Raimondi
 */

public class SendContentMessage extends SMSMessage {

    private SMSKademliaNode from;
    private SerializableObject content;
    private SerializableObject key;
    private static final byte CODE = 0x03;

    /**
     * @param content serializableObject to send
     * @param key     serializableObject key
     */
    public SendContentMessage(SMSKademliaNode from, SerializableObject content, SerializableObject key) {
        super(from.getNodePeer(), "SendContentMessage x");
        this.from = from;
        this.content = content;
        this.key = key;

    }


    /**
     * @return key of the content
     */
    public SerializableObject getKey() {
        return this.key;
    }


    /**
     * @return content that's is sent
     */
    public SerializableObject getContent() {
        return this.content;
    }


    /**
     * @return Peer of the node
     */
    @Override
    public SMSPeer getPeer() {
        return this.from.getNodePeer();
    }


    /**
     * @return byte Code
     */
    public byte getCode() {
        return this.CODE;
    }


    /**
     * @return String representing the message
     */
    @Override
    public String getData() {
        return "ContentMessage[origin=" + from + ",content=" + content + "]";
    }

}
