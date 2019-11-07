package com.eis0.library_demo;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

// TODO: add checks for lost packages during the transmission of messages
// TODO: check what happens if characters outside of the GSM character set are used
// TODO: handle messages too large to be split in the maximum amount of fragments allowed in an SMS
// TODO: skip check of TOTAL_FRAGMENTS in messages other than the first
class PacketHandler implements ReceivedMessageListener<SMSMessage> {

    /*
     * Messages longer than (140 chars - packet headers) are split into fragments.
     * Packet structure is the following:
     * APP_ID | MESSAGE_NUMBER | FRAGMENT_NUMBER | [TOTAL FRAGMENTS] | PAYLOAD
     * Fields are separated by the CR character, except for the first two because they have fixed
     * lengths.
     *
     * APP_ID is a single char = 0x02, this lets SMSHandler identify the message and send it
     * to our app;
     * MESSAGE_NUMBER identifies to which message this fragment belongs, together with the sender's
     * phone number. It's a single char, it allows us to distinguish a message from others with
     * reasonable confidence;
     * FRAGMENT_NUMBER identifies fragments in an orderly manner;
     * TOTAL_FRAGMENTS is the number of fragments in which the original message was split, it is
     * only sent in the first fragment of the message;
     * PAYLOAD is the message fragment.
     */

    private static final int messageNumberIndex = 1;
    private static final int fragmentNumberIndex = 2;
    private static int totalFragmentsIndex;
    private static int payloadIndex;
    private int messageNumber;
    private int fragmentNumber;
    private int totalFragments;
    private String payload;

    /**
     * Called by SMSHandler whenever an SMS message with our APP_ID is received.
     * @param message The message received
     */
    public void onMessageReceived(SMSMessage message) {
        SMSPeer sender = message.getPeer();
        String data = message.getData();

        // parse fields from SMS text
        int messageNumber = Character.getNumericValue(data.charAt(messageNumberIndex));
        String fragmentNumberString = "";
        for (int i = fragmentNumberIndex; i < data.length(); i++) {
            char c = data.charAt(i);
            if (data.charAt(i) != '\r') fragmentNumberString += c;
            else {
                fragmentNumber = Integer.parseInt(fragmentNumberString);
                if (fragmentNumber == 1) {
                    totalFragmentsIndex = i + 1;
                    break;
                } else {
                    payloadIndex = i + 1;
                    break;
                }
            }
        }
        if (fragmentNumber == 1) {
            // parse totalFragments
        }
        // parse payload

        // TODO: complete this method
    }

    void sendMessage(String message, SMSPeer destination) {
        if (message.length() > 160) {
            // TODO: fragment the message
        }
    }
}
