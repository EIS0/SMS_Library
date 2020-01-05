package com.eis0.kademlianetwork.InformationDeliveryManager;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;

/**
 * Kademlia Message using the
 * <a href="https://refactoring.guru/design-patterns/builder">Builder Design Pattern</a>
 */
public class KademliaMessage{

    private final String SEPARATOR = " ";
    private SMSPeer peer;
    private String messageText = "";

    /**
     * Constructor for a Kademlia Message totally built
     */
    public KademliaMessage(){ }

    public KademliaMessage setPeer(SMSPeer peer){
        this.peer = peer;
        return this;
    }

    public KademliaMessage setCommand(RequestTypes request){
        messageText = request.ordinal()+"";
        return this;
    }

    public KademliaMessage addArguments(String... arguments){
        StringBuilder builder = new StringBuilder(messageText);
        for(String arg : arguments){
            builder.append(SEPARATOR);
            builder.append(arg);
        }
        messageText = builder.toString();
        return this;
    }

    public SMSMessage buildMessage(){
        return new SMSMessage(this.peer, this.messageText);
    }
}
