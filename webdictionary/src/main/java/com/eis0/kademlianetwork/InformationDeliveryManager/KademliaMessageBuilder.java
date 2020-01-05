package com.eis0.kademlianetwork.InformationDeliveryManager;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;

/**
 * Kademlia Message using the
 * <a href="https://refactoring.guru/design-patterns/builder">Builder Design Pattern</a>
 */
public class KademliaMessageBuilder {

    private final String SEPARATOR = " ";
    private final String BLANK = "/";
    private SMSPeer peer = null;
    private String messageText = null;

    /**
     * Constructor for a Kademlia Message to build
     */
    public KademliaMessageBuilder(){ }

    /**
     * Sets an SMSPeer for this message
     * @param peer The peer to set
     * @return Returns an instance of this KademliaMessageBuilder so you
     * can concatenate construction calls
     */
    public KademliaMessageBuilder setPeer(SMSPeer peer){
        this.peer = peer;
        return this;
    }

    /**
     * Sets a command for this message
     * @param request The request command for the message
     * @return Returns an instance of this KademliaMessageBuilder so you
     * can concatenate construction calls
     */
    public KademliaMessageBuilder setCommand(RequestTypes request){
        messageText = request.ordinal()+"";
        return this;
    }

    /**
     * Adds space separated arguments to the string message
     * @param arguments The arguments to add to the message, if null or "", a "/" is added instead
     * @return Returns an instance of this KademliaMessageBuilder so you
     * can concatenate construction calls
     */
    public KademliaMessageBuilder addArguments(String... arguments){
        StringBuilder builder = new StringBuilder(messageText);
        for(String arg : arguments){
            builder.append(SEPARATOR);
            if(arg == null || arg .equals(""))
                builder.append(BLANK);
            else
                builder.append(arg);
        }
        messageText = builder.toString();
        return this;
    }

    /**
     * @return Returns the SMSMessage built from calls
     * @throws IllegalStateException if the message has not been correctly built
     */
    public SMSMessage buildMessage(){
        if(peer == null) throw new IllegalStateException("you have to set an SMSPeer first!");
        if(messageText == null) throw new IllegalStateException("you have to build a message first!");
        return new SMSMessage(this.peer, this.messageText);
    }
}
