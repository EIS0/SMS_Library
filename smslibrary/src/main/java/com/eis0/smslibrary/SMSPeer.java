package com.eis0.smslibrary;

public class SMSPeer implements Peer{
    private String address;

    /**
     * creates and returns an SMSPeer given a valid destination
     */
    public SMSPeer(String destination){
        this.address = destination;
    }

    /**
     * returns the SMSPeer address if valid
     */
    public String getAddress(){
        return address;
    }

    /**
     * helper function to write the SMSPeer as a string
     */
    public String toString(){
        return address;
    }

    /**
     * returns true if the address is empty
     */
    public boolean isEmpty(){
        return address.equals("");
    }

    /**
     * returns true if the SMSPeer is valid
     */
    public boolean isValid(){
        return !isEmpty() && address.length() < 15;
    }
}
