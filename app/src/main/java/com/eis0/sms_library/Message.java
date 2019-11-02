package com.eis0.sms_library;

public class Message {
    private Peer destination;
    private String message;

    public Message(Peer destination, String message){
        this.destination = destination;
        this.message = message;
    }

    public Peer getPeer(){
        return destination;
    }

    public String getMessage(){
        return message;
    }

    public String toString(){
        return "Peer: " + destination + ",Message: " + message;
    }
}
