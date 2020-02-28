package com.eis0.kademlianetwork.informationdeliverymanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;

/**
 * KademliaMessage constructed using the {@link MessageBuilder}
 * <p>
 * This class is similar to the Director part of the
 * <a href="https://refactoring.guru/design-patterns/builder">Builder Design Pattern</a>,
 * meaning this class uses the Builder to construct a specific type of message.
 * <p>
 * All messages of the Kademlia network have a standard pattern, which is then used to
 * deconstruct the message in {@link KademliaMessageAnalyzer}
 * <p>
 * Every message MUST have a peer to send the message to.
 * The text part of the message has 5 space-separated fields:
 * A number, which is the RequestType, a {@link KademliaId}, used for searchId requests,
 * a {@link SMSPeer}, the Peer searching for the Id,
 * a String "key" identifier of a resource and finally a String resource.
 * <p>
 * The TextMessage is constructed as "RequesType IdToFind Searcher Key Resource"
 * <p>
 * N.B. the key can only be one word, else we couldn't split between the end of the key
 * and the start of a resource
 * <p>
 * The RequestType is a required argument, which can't be null, while the others
 * can both be omitted or be null.
 * <p>
 * Methods to construct the message can also be called one after the other and in ANY order,
 * but must end with {@link KademliaMessage#buildMessage()} to construct the final message
 *
 * @author Marco Cognolato
 */
public class KademliaMessage {
    private SMSPeer peer = null;
    private RequestTypes requestType = null;
    private KademliaId idToFind = null;
    private SMSPeer searcher = null;
    private String key = null;
    private String resource = null;

    private final String BLANK = "/";

    /**
     * Sets an SMSPeer as the destination Peer of the SMSMessage
     *
     * @param peer The SMSPeer to send the message to
     * @return Returns an instance of this KademliaMessage to chain calls together
     */
    public KademliaMessage setPeer(@NonNull SMSPeer peer) {
        this.peer = peer;
        return this;
    }

    /**
     * Sets the RequestType parameter of the message
     *
     * @param requestType The RequestType to set
     * @return Returns an instance of this KademliaMessage to chain calls together
     */
    public KademliaMessage setRequestType(@NonNull RequestTypes requestType) {
        this.requestType = requestType;
        return this;
    }

    /**
     * Sets the idToFind parameter of the message
     *
     * @param idToFind The KademliaId to find
     * @return Returns an instance of this KademliaMessage to chain calls together
     */
    public KademliaMessage setIdToFind(@Nullable KademliaId idToFind) {
        this.idToFind = idToFind;
        return this;
    }

    /**
     * Sets the searcher parameter of the message
     *
     * @param searcher The SMSPeer to set as a parameter
     * @return Returns an instance of this KademliaMessage to chain calls together
     */
    public KademliaMessage setSearcher(@Nullable SMSPeer searcher) {
        this.searcher = searcher;
        return this;
    }

    /**
     * Sets a resource-key parameter of the message
     *
     * @param key The String key to set as a parameter
     * @return Returns an instance of this KademliaMessage to chain calls together
     * @throws IllegalArgumentException If the Key is invalid.
     *                                  A key is undefined invalid if it's more than one word
     */
    public KademliaMessage setKey(@Nullable String key) {
        if (key != null && !key.matches("^\\w*$"))
            throw new IllegalArgumentException("Key is invalid!" +
                    " Be sure to only have one word as a key!");
        this.key = key;
        return this;
    }

    /**
     * Sets the Resource parameter of the message
     *
     * @param resource The String parameter of the message
     * @return Returns an instance of this KademliaMessage to chain calls together
     */
    public KademliaMessage setResource(@Nullable String resource) {
        this.resource = resource;
        return this;
    }

    /**
     * Resets to empty the KademliaMessage builder
     *
     * @return Returns an instance of this KademliaMessage to chain calls together
     */
    public KademliaMessage reset() {
        return new KademliaMessage();
    }

    /**
     * Constructs the final message. If any of the parameters has not been set up, it
     * will instead default to a "/"
     *
     * @return Returns the SMSMessage used by Kademlia Operations
     * @throws IllegalArgumentException If the peer or the RequestType parameters have
     *                                  not been setup correctly.
     */
    public SMSMessage buildMessage() {
        if (peer == null) throw new IllegalArgumentException("Peer has not been setup!");
        if (requestType == null)
            throw new IllegalArgumentException("RequestType has not been setup!");
        return new MessageBuilder()
                .setPeer(peer)
                .addArguments(requestType.ordinal() + "")
                .addArguments(idToFind == null ? BLANK : idToFind.toString())
                .addArguments(searcher == null ? BLANK : searcher.toString())
                .addArguments(key, resource)
                .buildMessage();
    }
}