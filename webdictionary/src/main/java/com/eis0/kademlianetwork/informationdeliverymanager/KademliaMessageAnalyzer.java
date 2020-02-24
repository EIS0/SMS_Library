package com.eis0.kademlianetwork.informationdeliverymanager;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;

public class KademliaMessageAnalyzer {
    private final SMSPeer peer;
    private final RequestTypes command;
    private final KademliaId idToFind;
    private final SMSPeer searcher;
    private final String key;
    private String resource = "";

    /**
     * Constructor for the SMSMessage analyzer:
     * unpacks the message in 6 pieces of data:
     * 1. the peer who sent the message
     * 2. the request type the message is asking
     * 3. the idToFind as a KademliaId
     * 4. the SMSPeer who is searching
     * 5. the key of a resource as a String
     * 6. the resource as a String
     * @param message The message to analyze
     */
    public KademliaMessageAnalyzer(SMSMessage message){
        this.peer = message.getPeer();
        String[] messageTextParts = message.getData().split(" ");
        this.command = RequestTypes.values()[Integer.parseInt(messageTextParts[0])];
        idToFind = new KademliaId(messageTextParts[1]);
        searcher = new SMSPeer(messageTextParts[2]);
        key = messageTextParts[3];
        for(int i = 4; i < messageTextParts.length-1; i++)
            resource += messageTextParts[i] + " ";
        resource += messageTextParts[messageTextParts.length-1];
    }

    /**
     * @return Returns the peer of the SMSMessage
     */
    public SMSPeer getPeer(){
        return peer;
    }

    /**
     * @return Returns the command as a RequestTypes
     */
    public RequestTypes getCommand(){
        return command;
    }

    /**
     * @return Returns the idToFind as a KademliaId
     */
    public KademliaId getIdToFind(){
        return idToFind;
    }

    /**
     * @return Returns the searches as an SMSPeer
     */
    public SMSPeer getSearcher(){
        return searcher;
    }

    /**
     * @return Returns the key of a resource
     */
    public String getKey(){
        return key;
    }

    /**
     * @return Returns the resource
     */
    public String getResource(){
        return resource;
    }
}
