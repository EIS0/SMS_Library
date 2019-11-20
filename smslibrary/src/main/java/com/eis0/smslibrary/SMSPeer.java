package com.eis0.smslibrary;

public class SMSPeer implements Peer {
    private String address;
    private final String MATCH_EXPRESSION = "^\\+?\\d{4,15}$";

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
        return address.charAt(0) == '+';
    }

    /**
     * Returns true if the SMSPeer is valid
     */
    public boolean isValid() {
        return address != null && address.matches(MATCH_EXPRESSION);
    }
}
