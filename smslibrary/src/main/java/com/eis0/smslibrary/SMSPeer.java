package com.eis0.smslibrary;

import androidx.annotation.Nullable;

/**
 * Class that implements the Peer interface. It represent the telephone Peer.
 *
 * @author Marco Cognolato
 */
public class SMSPeer implements Peer {

    private String address;
    private final String MATCH_EXPRESSION = "^\\+?\\d{4,15}$";

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
     * Returns true if the SMSPeer is valid.
     *
     * @return A boolean representing the valid state of the peer.
     * @author Marco Cognolato
     */
    public boolean isValid() {
        return address != null && address.matches(MATCH_EXPRESSION);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }
        SMSPeer other = (SMSPeer)obj;
        return other.getAddress().equals(this.address);
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }
}
