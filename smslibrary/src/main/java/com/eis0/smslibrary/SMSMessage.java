package com.eis0.smslibrary;

public class SMSMessage implements Message {
    private SMSPeer destination;
    private String message;

    /**
     * Builds and returns an SMS message given a valid SMSPeer and a valid message to send
     */
    public SMSMessage(SMSPeer destination, String message) {
        this.destination = destination;
        this.message = message;
    }

    /**
     * Returns the peer of the message if valid
     */
    public SMSPeer getPeer() {
        return destination;
    }

    /**
     * Returns the data of the message if valid
     */
    public String getData() {
        return message;
    }

    /**
     * Helper function to write the message as a string
     */
    public String toString() {
        return "SMSPeer: " + destination + ",SMSMessage: " + message;
    }
}
