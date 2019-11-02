package com.eis0.sms_library;

public class Peer {
    private String destination;

    Peer(String destination){
        this.destination = destination;
    }

    public String getDestination(){
        return destination;
    }

    public String toString(){
        return destination;
    }

    public boolean isEmpty(){
        return destination.equals("");
    }

    public boolean isValid(){
        return !isEmpty() && destination.length() < 15;
    }
}
