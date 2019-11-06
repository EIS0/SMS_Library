package com.eis0.smslibrary;

import android.util.Log;

public class SMSPeer implements Peer {
    private String address;

    /**
     * Creates and returns an SMSPeer given a valid destination
     */
    public SMSPeer(String destination) {
        this.address = destination;
    }

    /**
     * Returns the SMSPeer address if valid
     */
    public String getAddress() {
        return address;
    }

    /**
     * Helper function to write the SMSPeer as a string
     */
    public String toString() {
        return address;
    }

    /**
     * Returns true if the address is empty
     */
    public boolean isEmpty() {
        return address.equals("");
    }


    /**
     * Returns true if SMSPeer has prefix
     */

    public boolean hasPrefix() {
        char prefixSign = '+';
        return address.charAt(0) == prefixSign;
    }

    /**
     * Returns true if the SMSPeer is valid
     */
    public boolean isValid() {
        try {
            int maxLength = 15;
            if (!isEmpty() && address.length() < maxLength) {
                if (hasPrefix()) Integer.parseInt(address.substring(1)); // To verify exceptions
                else Integer.parseInt(address);
                return true;
            }
        }
        catch(Exception e) { }
        return false;
    }
}