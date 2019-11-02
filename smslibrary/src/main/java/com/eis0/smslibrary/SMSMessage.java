package com.eis0.smslibrary;

public class SMSMessage implements Message{
    private SMSPeer destination;
    private String message;

    public SMSMessage(SMSPeer destination, String message){
        this.destination = destination;
        this.message = message;
    }

    public SMSPeer getPeer(){
        return destination;
    }

    public String getData(){
        return message;
    }

    public String toString(){
        return "SMSPeer: " + destination + ",SMSMessage: " + message;
    }
}
