package com.eis0.kademlianetwork.informationdeliverymanager;

import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;

/**
 * SMS Message Builder using the
 * <a href="https://refactoring.guru/design-patterns/builder">Builder Design Pattern</a>.
 * Thanks to this class it's possible to create space separated messages by building a
 * message argument by argument.
 *
 * @author Marco Cognolato
 */
public class MessageBuilder {

    private SMSPeer peer = null;
    private String messageText = null;

    /**
     * Constructor for a Kademlia Message to build
     */
    public MessageBuilder(){ }

    /**
     * Sets an SMSPeer for this message
     * @param peer The peer to set
     * @return Returns an instance of this MessageBuilder so you
     * can concatenate construction calls
     */
    public MessageBuilder setPeer(SMSPeer peer){
        this.peer = peer;
        return this;
    }

    /**
     * Adds space separated arguments to the string message
     * @param arguments The arguments to add to the message, if null or "", a "/" is added instead
     * @return Returns an instance of this MessageBuilder so you
     * can concatenate construction calls
     */
    public MessageBuilder addArguments(String... arguments){
        StringBuilder builder = new StringBuilder(messageText == null? "" : messageText);
        for(String arg : arguments){
            builder.append(" ");
            if(arg == null || arg .equals(""))
                builder.append("/");
            else
                builder.append(arg);
        }
        //if I don't have an older message, I have to remove the empty space at the start
        if(messageText == null) builder.deleteCharAt(0);
        messageText = builder.toString();
        return this;
    }

    /**
     * Resets the Message builder to his default state
     * @return Returns an instance of this MessageBuilder so you
     * can concatenate construction calls
     */
    public MessageBuilder reset(){
        return new MessageBuilder();
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
