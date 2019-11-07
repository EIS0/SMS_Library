package com.eis0.library_demo;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

// TODO: add checks for lost packages during the transmission of messages
// TODO: check what happens if characters outside of the GSM character set are used
class PacketHandler implements ReceivedMessageListener<SMSMessage> {

    /**
     * Packet structure is the following:
     * APP_ID | MESSAGE_NUMBER | TOTAL FRAGMENTS | FRAGMENT_NUMBER | PAYLOAD
     * Fields are separated by the CR character, except for the first two because they have fixed
     * lengths.
     *
     * APP_ID is a single char = 0x02, this lets SMSHandler identify the message and send it
     * to our app;
     * MESSAGE_NUMBER identifies to which message this fragment belongs, together with the sender's
     * phone number. It's a single char, it allows us to distinguish a message from others with
     * reasonable certainty;
     * TOTAL_FRAGMENTS is the number of fragments in which the original message was split;
     * FRAGMENT_NUMBER identifies fragments in an orderly manner.
     */

    /**
     * Called by SMSHandler whenever an SMS message with our APP_ID is received.
     * @param message The message received
     */
    public void onMessageReceived(SMSMessage message) {
        SMSPeer sender = message.getPeer();
        String data = message.getData();
        int messageNumber = Character.getNumericValue(data.charAt(1));
        // TODO: complete this method
    }

    void sendMessage(String message, SMSPeer destination) {
        if (message.length() > 160) {
            // TODO: fragment the message
        }
    }
}
