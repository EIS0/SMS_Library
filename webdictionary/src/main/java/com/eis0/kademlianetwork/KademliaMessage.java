package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;

public class KademliaMessage {

    public SMSPeer peer;
    public String text;
    public RequestTypes requestType;
    public KademliaId idToFind;
    public SMSPeer searcher;
    public String key;
    public String resource;

    /**
     *
     * @param message
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
     *
     * @param requestType
     * @param idToFind
     * @param searcher
     * @param key
     * @param resource
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
     *
     * @return
     */
    public String toString() {
        if (requestType == null) throw new IllegalArgumentException();

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
