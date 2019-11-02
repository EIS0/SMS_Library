package com.eis0.sms_library;

public class Peer {
    private String address;

    Peer(String destination){
        this.address = destination;
    }

    public String getDestination(){
        return address;
    }

    public String toString(){
        return address;
    }

    public boolean isEmpty(){
        return address.equals("");
    }

    public boolean isValid(){
        return !isEmpty() && address.length() < 15;
    }
}
