package com.eis0.KademliaNetwork;


import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.Message;
import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SerializableObject;

/**
 * Message used to see/get a content in another node
 * The Node associated to the key, has to respond with a SendContentMessage.
 *
 * @author Edoardo Raimondi
 */

public class ViewContentMessage implements Message {

    private SMSKademliaNode from;
    private SerializableObject key;
    private static final byte CODE = 0x05;

    /**
     * @param from Node that want to see a value
     * @param key key of the value
     */
    public ViewContentMessage(SMSKademliaNode from, SerializableObject key){
        this.from = from;
        this.key = key;
    }

    /**
     * @return Peer of the node message sender
     */
    public SMSPeer getPeer() {
        return this.from.getNodePeer();
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
        return "ContentMessage[origin=" + from + ",key=" + key + "]";
    }

}
