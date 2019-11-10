package com.eis0.library_demo;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

/**
 * Creates and modifies TernaryPoll objects based on inputs from Activities and SMS Messages.
 * @author Giovanni Velludo
 */

class PollManager implements ReceivedMessageListener<SMSMessage> {

    /**
     * Receives an SMSMessage and updates poll data accordingly.
     * If the message was sent by the pollAuthor: if the Poll already exists, updates the content of
     * the local copy of the Poll, otherwise creates a new Poll.
     * If the message was sent by a pollUser who is not the pollAuthor, sets his answer accordingly
     * and updates all other users.
     * @author Giovanni Velludo
     */
    public void onMessageReceived(SMSMessage message) {
        SMSPeer peer = message.getPeer();
        String data = message.getData();
        if (data.charAt(0) == '0') {

        }
    }
}
