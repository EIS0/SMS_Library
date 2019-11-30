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
 * @author Matteo Carnelos
 */
public class SMSPeer implements Peer, java.io.Serializable {

    private String address;

    /**
     * Creates and returns an SMSPeer given a valid destination.
     *
     * @param destination String containing the destination address.
     * @throws IllegalArgumentException If the destination given is not valid.
     * @author Marco Cognolato
     * @author Matteo Carnelos
     */
    public SMSPeer(String destination) {
        Phonenumber.PhoneNumber phoneNumber = phoneNumberWithoutPrefix(destination);
        if (!adjustForEmulator(phoneNumber)) {
            if (!PhoneNumberUtil.getInstance().isValidNumber(phoneNumber))
                throw new IllegalArgumentException("Unable to create SMSPeer, invalid destination: \"" + destination + "\".");
        }
        this.address = extractNationalNumber(phoneNumber);
    }

    /**
     * Returns the string version of the national address contained in a PhoneNumber object.
     *
     * @param phoneNumber The PhoneNumber object to get the national number string.
     * @return A string containing the phone number.
     */
    private String extractNationalNumber(Phonenumber.PhoneNumber phoneNumber) {
        return String.valueOf(phoneNumber.getNationalNumber());
    }

    /**
     * This method adjust the address string in case it is the extended phone number of an emulator.
     * The emulator phone numbers could be (without prefix) either "555521555*" or "555*",
     * this method adjust emulator phone numbers of the first type by removing the
     * extension "555521".
     *
     * @param phoneNumber The PhoneNumber to analyze.
     * @return True if the number given is an emulator number, false otherwise.
     * @author Matteo Carnelos
     */
    private boolean adjustForEmulator(Phonenumber.PhoneNumber phoneNumber) {
        // NOTE: Before releasing the app this method should be removed or not called in the
        //       constructor. This because all the numbers of type "555521555*" in the real world
        //       wont be able to send SMSs through this library.
        String destWithoutPrefix = extractNationalNumber(phoneNumber);
        if(destWithoutPrefix.matches("^555\\d$")) return true;
        //    phoneNumber.setNationalNumber((Long.parseLong("555521" + destWithoutPrefix)));
        if(destWithoutPrefix.startsWith("555521")) {
            phoneNumber.setNationalNumber(Long.parseLong(destWithoutPrefix.substring(6)));
            return true;
        }
        return false;
    }

    /**
     * Returns a PhoneNumber object that contains the destination string without the prefix.
     *
     * @param destination The destination string, eventually with the prefix.
     * @return The PhoneNumber object containing the phone number given without the prefix.
     * @throws IllegalArgumentException If the given destination is not parsable.
     * @author Matteo Carnelos
     */
    private Phonenumber.PhoneNumber phoneNumberWithoutPrefix(String destination) {
        Phonenumber.PhoneNumber phoneNumber;
        try { phoneNumber = PhoneNumberUtil.getInstance().parse(destination, "IT"); }
        catch(NumberParseException e) {
            throw new IllegalArgumentException("Unable to remove prefix, not parsable destination: \"" + destination + "\".");
        }
        return phoneNumber;
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
        return address;
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
