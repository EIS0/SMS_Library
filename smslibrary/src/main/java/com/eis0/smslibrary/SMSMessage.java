package com.eis0.smslibrary;

import androidx.annotation.NonNull;

/**
 * Class implementing the Message interface, it represent a message of type SMS.
 *
 * @author Marco Cognolato
 */
public class SMSMessage implements Message {

    private SMSPeer destination;
    private String message;

    /**
     * Builds and returns an SMS message given a valid SMSPeer and a valid message to send.
     *
     * @param destination The destination Peer object.
     * @param message The String message.
     * @author Marco Cognolato
     */
    public SMSMessage(SMSPeer destination, String message) {
        this.destination = destination;
        this.message = message;
    }

    /**
     * Returns the peer of the message if valid.
     *
     * @return An SMSPeer object containing all the peer info.
     * @author Marco Cognolato
     */
    @Override
    public SMSPeer getPeer() {
        return destination;
    }

    /**
     * Returns the data of the message if valid.
     *
     * @return A String containing the data.
     * @author Marco Cognolato
     */
    @Override
    public String getData() {
        return message;
    }

    /**
     * Helper function to write the message as a string.
     *
     * @return The String containing the object representation.
     * @author Marco Cognolato
     */
    @NonNull
    @Override
    public String toString() {
        return "SMSPeer: " + destination + ", SMSMessage: " + message;
    }
}
