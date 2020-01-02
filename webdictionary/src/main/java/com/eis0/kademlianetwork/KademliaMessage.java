package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;

/**
 * This class is used to create custom messages to send or receive inside the Kademlia Network, the
 * fields are in common for all the type of requests
 */
public class KademliaMessage {
    private static final String REQUEST_TYPE_NULL = "The requestType parameter is null";

    public SMSPeer peer;
    public String text;
    public RequestTypes requestType;
    public KademliaId idToFind;
    public SMSPeer searcher;
    public String key;
    public String resource;

    /**
     * This is the constructor of the class, it allows to create an instance of the KademliaMessage
     * with the content of the received {@link SMSMessage}; it automatically extracts the values in
     * the fields of the message.
     * The message must satisfy the division into fields, every single word inside of it has a
     * specific task, except for the 'resource' field, which has an arbitrary length
     * [0] => The {@link RequestTypes} of the message
     * [1] => The {@link KademliaId}, can be the target ID to find inside the network, or the ID found
     * [2] => The {@link SMSPeer} which originated the request
     * [3] => The key of the interested resource
     * [4 --> end of the message] => The resource brought by the message
     *
     * @param message The message received from the network
     * @throws IndexOutOfBoundsException                                     If the message doesn't have the minimum number of fields
     * @throws IllegalArgumentException                                      If the {@link KademliaId} has not the right fit
     * @throws com.eis.smslibrary.exceptions.InvalidTelephoneNumberException If If telephoneNumber
     *                                                                       check is not {@link SMSPeer.TelephoneNumberState#TELEPHONE_NUMBER_VALID}.
     */
    public KademliaMessage(SMSMessage message) {
        text = message.getData();
        peer = message.getPeer();
        //Array of strings containing the message fields
        String[] splitted = text.split(" ");

        //Extracts the value contained inside the message
        requestType = RequestTypes.values()[Integer.parseInt(splitted[0])];
        idToFind = new KademliaId(splitted[1]);
        searcher = new SMSPeer(splitted[2]);
        key = splitted[3];
        for (int i = 4; i < splitted.length; i++) resource += splitted[i];
    }

    /**
     * This constructor allows to create a KademliaMessage with the desired values
     *
     * @param requestType The request type of the message
     * @param idToFind    Can be the target ID to find inside the network, or the ID found
     * @param searcher    The SMSPeer which originated the request
     * @param key         The key of the interested resource
     * @param resource    The resource that the message will contain
     */
    public KademliaMessage(
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

    /**
     * This method returns the String value of the Kademlia Message, with a format readable by the
     * receiver of the message; every information owns his position inside of it, so that is easier
     * to decode it
     *
     * @return The String value of the message itself
     */
    public String toString() {
        if (requestType == null) throw new IllegalArgumentException(REQUEST_TYPE_NULL);
        //The RequestType is the only field which needs to be not Null
        String stringIdToFind;
        if (idToFind == null) stringIdToFind = "/";
        else stringIdToFind = idToFind.toString();

        String stringSearcher;
        if (idToFind == null) stringSearcher = "/";
        else stringSearcher = searcher.toString();

        if (key == null) key = "/";
        if (resource == null) resource = "/";

        String messageToCreate =
                RequestTypes.AddToDict.ordinal() + " " +
                        stringIdToFind + " " +
                        stringSearcher + " " +
                        key + " " +
                        resource + " ";
        return messageToCreate;
    }
}
