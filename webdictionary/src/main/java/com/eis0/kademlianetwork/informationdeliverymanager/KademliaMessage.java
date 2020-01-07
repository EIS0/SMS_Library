package com.eis0.kademlianetwork.informationdeliverymanager;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;

/**
 * KademliaMessage constructed using the {@link MessageBuilder}
 *
 * This class is similar to the Director part of the
 * <a href="https://refactoring.guru/design-patterns/builder">Builder Design Pattern</a>,
 * which also works as a Builder for the same pattern.
 *
 * @author Marco Cognolato
 */
public class KademliaMessage {
    //Builder used to construct a KademliaMessage
    private MessageBuilder builder = new MessageBuilder();

    private SMSPeer peer = null;
    private RequestTypes requestType = null;
    private KademliaId idToFind = null;
    private SMSPeer searcher = null;
    private String key = null;
    private String resource = null;

    private final String BLANK = "/";

    /**
     * Sets an SMSPeer as the destination Peer of the SMSMessage
     * @param peer The SMSPeer to send the message to
     * @return Returns an instance of this KademliaMessage to chain calls together
     */
    public KademliaMessage setPeer(SMSPeer peer){
        this.peer = peer;
        return this;
    }

    /**
     * Sets the RequestType parameter of the message
     * @param requestType The RequestType to set
     * @return Returns an instance of this KademliaMessage to chain calls together
     */
    public KademliaMessage setRequestType(RequestTypes requestType){
        this.requestType = requestType;
        return this;
    }

    /**
     * Sets the idToFind parameter of the message
     * @param idToFind The KademliaId to find
     * @return Returns an instance of this KademliaMessage to chain calls together
     */
    public KademliaMessage setIdToFind(KademliaId idToFind){
        this.idToFind = idToFind;
        return this;
    }

    /**
     * Sets the searcher parameter of the message
     * @param searcher The SMSPeer to set as a parameter
     * @return Returns an instance of this KademliaMessage to chain calls together
     */
    public KademliaMessage setSearcher(SMSPeer searcher){
        this.searcher = searcher;
        return this;
    }

    /**
     * Sets a resource-key parameter of the message
     * @param key The String key to set as a parameter
     * @return Returns an instance of this KademliaMessage to chain calls together
     * @throws IllegalArgumentException If the Key is invalid.
     * A key is undefined invalid if it's more than one word
     */
    public KademliaMessage setKey(String key){
        if(!key.matches("^\\w*$")) throw new IllegalArgumentException("Key is invalid!" +
                " Be sure to only have one word as a key!");
        this.key = key;
        return this;
    }

    /**
     * Sets the Resource parameter of the message
     * @param resource The String parameter of the message
     * @return Returns an instance of this KademliaMessage to chain calls together
     */
    public KademliaMessage setResource(String resource){
        this.resource = resource;
        return this;
    }

    /**
     * Resets to empty the KademliaMessage builder
     * @return Returns an instance of this KademliaMessage to chain calls together
     */
    public KademliaMessage reset(){
        return new KademliaMessage();
    }

    /**
     * Constructs the final message. If any of the parameters has not been set up, it
     * will instead default to a "/"
     * @return Returns the SMSMessage used by Kademlia Operations
     * @throws IllegalArgumentException If the peer or the RequestType parameters have
     * not been setup correctly.
     */
    public SMSMessage buildMessage(){
        if(peer == null) throw new IllegalArgumentException("Peer has not been setup!");
        if(requestType == null) throw new IllegalArgumentException("RequestType has not been setup!");
        String builtMessage = requestType.ordinal() + " "
                + (idToFind == null ? BLANK : idToFind) + " "
                + (searcher == null ? BLANK : searcher) + " "
                + (key      == null ? BLANK : key     ) + " "
                + (resource == null ? BLANK : resource);
        return new SMSMessage(peer, builtMessage);
    }

    /*
    private static final String REQUEST_TYPE_NULL = "The requestType parameter is null";
    private static final String DEFAULT = "/";
    private static final String BLANK = " ";


    public KademliaOldMessage(
            RequestTypes requestType,
            KademliaId idToFind,
            SMSPeer searcher,
            String key,
            String resource) {
        this.requestType = requestType;
        this.idToFind = idToFind;
        this.searcher = searcher;
        this.key = key;
        this.resource = resource;
    }

    public KademliaOldMessage(String message) {
        //Array of strings containing the message fields
        String[] splitted = message.split(BLANK);
        //Extracts the value contained inside the message
        requestType = RequestTypes.values()[Integer.parseInt(splitted[0])];
        idToFind = new KademliaId(splitted[1]);
        searcher = new SMSPeer(splitted[2]);
        key = splitted[3];
        StringBuilder resourceBuilder = new StringBuilder();
        for (int i = 4; i < splitted.length; i++) {
            resourceBuilder.append(splitted[i]).append(BLANK);
        }
        resource = resourceBuilder.toString();
    }

    public String toString() {
        //The RequestType is the only field which needs to be not Null
        if (requestType == null) throw new IllegalArgumentException(REQUEST_TYPE_NULL);

        String stringIdToFind = DEFAULT;
        String stringSearcher = DEFAULT;
        if (idToFind != null) stringIdToFind = idToFind.toString();
        if (searcher != null) stringSearcher = searcher.toString();
        if (key == null) key = DEFAULT;
        if (resource == null) resource = DEFAULT;

        return requestType.ordinal() + BLANK +
                stringIdToFind + BLANK +
                stringSearcher + BLANK +
                key + BLANK +
                resource;
    }

    * */
}
