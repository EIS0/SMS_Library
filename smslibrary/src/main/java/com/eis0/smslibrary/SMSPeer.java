package com.eis0.smslibrary;

import android.util.Log;

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
     * returns true if SMSPeer has prefix
     */

    public boolean hasPrefix(){
        char prefix = '+';
        return address.charAt(0) == prefix;
    }

    /**
     * returns true if the SMSPeer is valid
     */
    public boolean isValid(){
        try {
            int maxLength = 15;
            if (!isEmpty() && address.length() < maxLength ) {
                if (hasPrefix()) {
                    int test = Integer.parseInt(address.substring(1)); //to verify exceptions
                } else {
                    int test = Integer.parseInt(address);
                }
                return true;
            }
        }
        catch(Exception e){
            Log.e("address", "letters on test");
        }
        return false;
    }
}
