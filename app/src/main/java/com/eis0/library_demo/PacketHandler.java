package com.eis0.library_demo;

import android.content.Context;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;

/**
 * Sends and receives messages longer than a single SMS.
 *
 * Messages longer than (140 chars - packet headers) are split into fragments.
 * Packet structure is the following:
 * APP_ID | MESSAGE_NUMBER | FRAGMENT_NUMBER | [TOTAL FRAGMENTS] | PAYLOAD
 *
 * APP_ID is a single char == 0x02, this lets SMSHandler identify the message and send it
 * to our app;
 * MESSAGE_NUMBER identifies to which message this fragment belongs, together with the sender's
 * phone number. It's a single char, it allows us to distinguish a message from others with
 * reasonable confidence. When a peer has finished receiving a message with a certain number,
 * that number can be reused for future messages;
 * FRAGMENT_NUMBER are 2 chars which identify the current fragment;
 * TOTAL_FRAGMENTS are 2 chars indicating the number of fragments in which the original message
 * was split, it is only sent in the first fragment of the message and its maximum value is 99;
 * PAYLOAD is the message fragment.
 *
 *                          Header size:    Payload max size:
 * First fragment:          6 chars         154 chars
 * Subsequent fragments:    4 chars         156 chars
 *
 * @author Giovanni Velludo
 */

// TODO: add checks for lost packages during the transmission of messages (using built-in SMS delivery reports?)
// TODO: check what happens if characters outside of the GSM character set are used
// TODO: handle messages too large to be split in the maximum amount of fragments allowed in an SMS
// TODO: skip check of TOTAL_FRAGMENTS in messages other than the first
// TODO: use groups of bits instead of characters for headers
// TODO: implement a timeout for MESSAGE_NUMBER, to discard messages that were not completely received and free up a slot
class PacketHandler implements ReceivedMessageListener<SMSMessage> {

    private Context context;
    private static final int messageNumberIndex = 1;
    private static final int fragmentNumberIndex = 2;
    private static final int totalFragmentsIndex = 2;
    private static int payloadIndex;
    private int messageNumber;
    private int fragmentNumber;
    private int totalFragments;
    private String payload;

    /**
     * Creates a PacketHandler object, to send and receive messages longer than a single SMS.
     * @param context Context of the application using this object.
     */
    PacketHandler(Context context) {
        this.context = context;
    }

    /**
     * Called by SMSHandler whenever a packet with our APP_ID is received.
     * @param packet The packet received
     */
    public void onMessageReceived(SMSMessage packet) {
        SMSPeer sender = packet.getPeer();
        String data = packet.getData();

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
        // assuming that the last part of the message will fit perfectly into the last fragment,
        // and the message is at least 154 chars long
        int processedChars = 0;
        ArrayList<String> fragments = new ArrayList<String>();
        fragments.add(message.substring(processedChars, 154));
        processedChars += 154;
        if (processedChars < message.length()){
            while (processedChars < message.length()) {
                fragments.add(message.substring(processedChars, 156));
                processedChars += 154;



            }
        }

    }
}
