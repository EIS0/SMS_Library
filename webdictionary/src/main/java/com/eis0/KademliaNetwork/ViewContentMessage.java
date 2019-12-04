package com.eis0.KademliaNetwork;


import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.Message;
import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SerializableObject;

/**
 * Message used to see/get a content in another node
 */
public class ViewContentMessage implements Message {

    private SMSKademliaNode From;
    private SerializableObject Key;
    private static final byte CODE = 0x05;

    /**
     * @param from Node that want to see a value
     * @param key key of the value
     */
    public ViewContentMessage(SMSKademliaNode from, SerializableObject key){
        this.From = from;
        this.Key = key;
    }

    /**
     * @param to The node containing my value
     * @return the value associated to the given key
     */
    public SerializableObject getContent(SMSKademliaNode to){
        return to.getDictionary().getResource(Key);
    }

    /**
     * @return Peer of the node
     */
    public SMSPeer getPeer() {
        return this.From.getPhoneNumber();
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
        return "ContentMessage[origin=" + From + ",key=" + Key + "]";
    }

}
