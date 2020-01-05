package com.eis0.kademlianetwork.informationdeliverymanager;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;

/**
 * This class is used to create custom messages to send or receive inside the Kademlia Network, the
 * fields are in common for all the type of requests; the message is not automatically sent, is just
 * written, this class must be seen as a code/decode handler, not as the main messages manager of
 * the network.
 * <p>
 * The syntax is defined in the constructor
 *
 * @author Enrico Cestaro
 * @author edits by Giovanni Velludo
 */
public class KademliaMessage {
    private static final String REQUEST_TYPE_NULL = "The requestType parameter is null";
    private static final String DEFAULT = "/";
    private static final String BLANK = " ";

    public final RequestTypes requestType;
    public final KademliaId idToFind;
    public final SMSPeer searcher;
    public String key;
    public String resource;

    /**
     * This is the constructor of the class, it allows to create an instance of the KademliaMessage
     * with the content of the received {@link SMSMessage}; it automatically extracts the values in
     * the fields of the message.
     * The message must satisfy the division into fields, every single word inside of it has a
     * specific task, except for the 'resource' field, which has an arbitrary length; fields are
     * separated by the BLANK value (which is a blank space); this constructor is used to create a
     * KademliaMessage using a text which, theoretically, already contains the values in the right
     * positions, which are:
     * [0] => The {@link RequestTypes} of the message
     * [1] => The {@link KademliaId}, can be the target ID to find inside the network, or the ID found
     * [2] => The {@link SMSPeer} which originated the request
     * [3] => The key of the interested resource
     * [4 --> end of the message] => The resource brought by the message
     * To create a new message starting from the single parameters the second constructor is needed
     *
     * @param message The message received from the network
     * @throws IndexOutOfBoundsException                                     If the message doesn't have the minimum number of fields
     * @throws IllegalArgumentException                                      If the {@link KademliaId} has not the right fit
     * @throws com.eis.smslibrary.exceptions.InvalidTelephoneNumberException If If telephoneNumber
     *                                                                       check is not {@link SMSPeer.TelephoneNumberState#TELEPHONE_NUMBER_VALID}.
     */
    public KademliaMessage(String message) {
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

    /**
     * This constructor allows to create a KademliaMessage that contains the desired values
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
     * This method returns the String value of the KademliaMessage, with a format readable by the
     * receiver of the message; every information owns a specific position inside of the output
     * message, so that is easier to decode.
     * The values occupy the positions:
     * [0] => The {@link RequestTypes} of the message
     * [1] => The {@link KademliaId}, can be the target ID to find inside the network, or the ID found
     * [2] => The {@link SMSPeer} which originated the request
     * [3] => The key of the interested resource
     * [4 --> end of the message] => The resource brought by the message
     * Each field is separated from the others by the BLANK value (which is the blank space " ");
     * except for the {@link RequestTypes} of the message, each field can be inserted as null, it
     * will be transcribed as the DEFAULT value (which is the '/' character)
     *
     * @return The String value of the message itself
     */
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
}
