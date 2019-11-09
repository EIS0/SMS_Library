package com.eis0.library_demo;

import java.util.ArrayList;

class MessageSlots {
    ArrayList<String>[] outgoingMessages;
    ArrayList<String>[] incomingMessages;
    private static final int maxMessageNumber = 10;

    MessageSlots() {
        // initializes two arrays of messages, where each message is an ArrayList of packets
        // in which it was split
        for (int i = 0; i < maxMessageNumber; i++) {
            outgoingMessages[i] = new ArrayList<String>();
            incomingMessages[i] = new ArrayList<String>();
        }
    }
}
