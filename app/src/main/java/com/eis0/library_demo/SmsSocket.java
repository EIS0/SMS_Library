package com.eis0.library_demo;

import java.util.ArrayList;

/**
 * Provides sockets for exchanging messages with an SMSPeer.
 * @author Giovanni Velludo
 */

class SmsSocket {
    ArrayList<String>[] outgoingMessages;
    ArrayList<String>[] incomingMessages;
    private static final int maxMessageNumber = 10;

    SmsSocket() {
        // initializes two arrays of messages, where each message is the ArrayList of packets
        // in which the original message was split
        for (int i = 0; i < maxMessageNumber; i++) {
            outgoingMessages[i] = new ArrayList<String>();
            incomingMessages[i] = new ArrayList<String>();
        }
    }
}
