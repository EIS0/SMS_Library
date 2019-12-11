package com.eis0.kademlianetwork;


import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SerializableObject;

/**
 * Message used to see a content in another node
 * The Node associated to the key, has to respond with a ContentMessage.
 *
 * @author Edoardo Raimondi
 */

public class ViewContentMessage extends SMSMessage {

    private SMSKademliaNode from;
    private SerializableObject key;
    private static final byte CODE = 0x04;

    /**
     * @param from Node that want to see a value
     * @param key  key of the value
     */
    public ViewContentMessage(SMSKademliaNode from, SerializableObject key) {
        super(from.getPeer(), "ViewContentMessage x");
        this.from = from;
        this.key = key;
    }


    /**
     * @return Peer of the node message sender
     */
    @Override
    public SMSPeer getPeer() {
        return this.from.getPeer();
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
        return super.getData() + " with the following key: " + key.toString();
    }

}
