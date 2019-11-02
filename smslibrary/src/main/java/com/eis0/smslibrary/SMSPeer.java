package com.eis0.smslibrary;

public class SMSPeer implements Peer{
    private String address;

    public SMSPeer(String destination){
        this.address = destination;
    }

    public String getAddress(){
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
