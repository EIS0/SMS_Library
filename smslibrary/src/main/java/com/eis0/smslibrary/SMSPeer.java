package com.eis0.smslibrary;

import androidx.annotation.Nullable;

/**
 * Class that implements the Peer interface. It represent the telephone Peer.
 *
 * @author Marco Cognolato
 */
public class SMSPeer implements Peer, java.io.Serializable {

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
        String[] prefixSigns = {"+", "00"};
        for(String prefixSign : prefixSigns)
            if(getAddress().startsWith(prefixSign)) return true;
        return false;
    }

    /**
     * Returns true if the SMSPeer is valid.
     *
     * @return A boolean representing the valid state of the peer.
     * @author Marco Cognolato
     */
    public boolean isValid() {
        return address != null && !address.isEmpty() && address.matches(MATCH_EXPRESSION);
    }

    /**
     * @author Giovanni Velludo
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof SMSPeer)) return false;
        SMSPeer peer = (SMSPeer) obj;
        return peer.getAddress().equals(getAddress());
    }

    /**
     * @author Giovanni Velludo
     */
    @Override
    public int hashCode() {
        return getAddress().hashCode();
    }
}
