package com.eis0.smslibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * Class that implements the Peer interface. It represent the telephone Peer.
 *
 * @author Marco Cognolato
 */
public class SMSPeer implements Peer, java.io.Serializable {

    private PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private Phonenumber.PhoneNumber phoneNumber;

    /**
     * Creates and returns an SMSPeer given a valid destination.
     *
     * @param destination String containing the destination address.
     * @throws IllegalArgumentException If the destination given is not valid or is not a phone number.
     * @author Marco Cognolato
     * @author Matteo Carnelos
     */
    public SMSPeer(String destination) {
        IllegalArgumentException exception = new IllegalArgumentException("Cannot create SMSPeer: invalid destination.");
        try {
            phoneNumber = phoneNumberUtil.parse(destination, "IT");
        } catch(NumberParseException e) {
            throw exception;
        }
        // If the destination is a simulator phone number (i.e. 555*) adds the extension: 555521
        // The following two lines are intended for simulator use only
        if(getAddress().matches("^555\\d$"))
            phoneNumber.setNationalNumber(Long.parseLong("555521" + getAddress()));
        if(!isValid())
            throw exception;
    }

    /**
     * Returns the SMSPeer address if valid.
     *
     * @return String containing the phone address.
     * @author Marco Cognolato
     * @author Matteo Carnelos
     */
    @Override
    public String getAddress() {
        return String.valueOf(phoneNumber.getNationalNumber());
    }

    /**
     * Helper function to write the SMSPeer as a String.
     *
     * @return String containing the representation of a peer.
     * @author Marco Cognolato
     * @author Matteo Carnelos
     */
    @NonNull
    @Override
    public String toString() {
        return getAddress();
    }

    /**
     * Returns true if the SMSPeer is valid.
     *
     * @return A boolean representing the valid state of the peer.
     * @author Marco Cognolato
     * @author Matteo Carnelos
     */
    public boolean isValid() {
        return phoneNumberUtil.isPossibleNumber(phoneNumber);
    }

    /**
     * Tell if two SMSPeer are equals.
     *
     * @return True if the compared objects are equals, false otherwise.
     * @author Giovanni Velludo
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof SMSPeer)) return false;
        SMSPeer peer = (SMSPeer)obj;
        return peer.getAddress().equals(getAddress());
    }

    /**
     * Generate a unique hash code for the object.
     *
     * @return The integer representing the hash code.
     * @author Giovanni Velludo
     */
    @Override
    public int hashCode() {
        return getAddress().hashCode();
    }
}
