package com.eis0.smslibrary;

import android.util.Log;


/**
 * Class that implements the Peer interface. It represent the telephone Peer.
 *
 * @author Marco Cognolato
 */
public class SMSPeer implements Peer {

    private String address;
    private final String LOG_KEY = "SMS_PEER";

    /**
     * Creates and returns an SMSPeer given a valid destination.
     *
     * @param destination String containing the destination address.
     * @author Marco Cognolato
     */
    public SMSPeer(String destination) {
        this.address = destination;
        if(!isValid()) throw new IllegalArgumentException();
    }

    /**
     * Returns the SMSPeer address if valid.
     *
     * @return String containing the phone address.
     * @author Marco Cognolato
     */
    public String getAddress() {
        return address;
    }

    /**
     * Helper function to write the SMSPeer as a String.
     *
     * @return String containing the representation of a peer.
     * @author Marco Cognolato
     */
    public String toString() {
        return address;
    }

    /**
     * Returns true if the address is empty.
     *
     * @return A boolean representing the empty state.
     * @author Marco Cognolato
     */
    public boolean isEmpty() {
        return address.equals("");
    }

    /**
     * Returns true if SMSPeer has prefix.
     *
     * @return A boolean representing whenever or not the peer has a prefix.
     * @author Matteo Carnelos
     */
    public boolean hasPrefix() {
        return address.charAt(0) == '+';
    }

    /**
     * Returns true if the SMSPeer is valid
     * @author Edoardo Raimondi
     */
    public boolean isValid() {
        try {
            int maxLength = 15;
            if (!isEmpty() && address.length() < maxLength) {
                if (hasPrefix()) {
                    Long.parseLong(address.substring(1)); //to verify exception
                } else Long.parseLong(address);
                return true;
            }
        }
        catch(Exception e){
            Log.i(LOG_KEY, " invalid destination");
        }
        return false;
    }
    @Override
    public int hashCode() {
        return address.hashCode();
    }

}
