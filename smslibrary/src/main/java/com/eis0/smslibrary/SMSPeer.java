package com.eis0.smslibrary;

import androidx.annotation.Nullable;

public class SMSPeer implements Peer, java.io.Serializable {
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
     * Checks if the address has prefix
     * @return Returns true if the address has the prefix sign
     */

    public boolean hasPrefix() {
        char prefixSign = '+';
        return address.charAt(0) == prefixSign;
    }

    public SMSPeer withoutPrefix() {
        if(!hasPrefix()) return this;
        return new SMSPeer(this.address.substring(1));
    }

    /**
     * Returns true if the SMSPeer is valid
     * @author Edoardo Raimondi
     */
    public boolean isValid() {
        try {
            int maxLength = 15;
            if (!isEmpty() && address.length() < maxLength) {
                if (hasPrefix()) Long.parseLong(withoutPrefix().address);
                else Long.parseLong(address);
                return true;
            }
        }
        catch(Exception e) { }
        return false;
    }

    /**
     * @author Giovanni Velludo
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof SMSPeer)) return false;

        SMSPeer peer = (SMSPeer) obj;
        return peer.address.equals(this.address);
    }

    /**
     * @author Giovanni Velludo
     */
    @Override
    public int hashCode() {
        return address.hashCode();
    }
}
