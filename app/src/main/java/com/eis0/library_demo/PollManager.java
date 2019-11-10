package com.eis0.library_demo;

import android.util.Pair;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates and modifies TernaryPoll objects based on inputs from Activities and SMS Messages.
 * @author Giovanni Velludo
 */

class PollManager implements ReceivedMessageListener<SMSMessage> {

    private static PollManager instance = null;
    private static final char fieldSeparator = '\r';
    private static final int authorIndex = 1;
    private static Map<Pair<String, String>, TernaryPoll> polls =
            new HashMap<>();

    // Singleton Design Pattern
    private PollManager() {}

    /**
     * Returns a new instance of PollManager if none exist, otherwise the one already created as per
     * the Singleton Design Patter.
     * @return Single instance of this class
     */
    public static PollManager getInstance() {
        if(instance == null) instance = new PollManager();
        return instance;
    }

    /**
     * Receives an SMSMessage and updates poll data accordingly.
     * If the message was sent by the pollAuthor: if the Poll already exists, updates the content of
     * the local copy of the Poll, otherwise creates a new Poll.
     * If the message was sent by a pollUser who is not the pollAuthor, sets his answer accordingly
     * and updates all other users.
     *
     * SMSMessage fields:
     * messageCode + pollAuthor + pollId + pollQuestion + pollUsers
     * Fields are separated by the character CR, except for messageCode
     * and pollAuthor because the first is always only the first character.
     * Different pollUsers are separated by the character CR.
     *
     * messageCode assumes the following values:
     * 0 when the message contains a new poll
     * 1 when the message is sent from a user to the author and contains an answer
     * 2 when the message is sent from the author to users and contains updated poll data
     *
     * @author Giovanni Velludo
     */
    public void onMessageReceived(SMSMessage message) {
        SMSPeer peer = message.getPeer();
        String data = message.getData();
        char messageCode = data.charAt(0);
        if (messageCode == '0') {
            // received new TernaryPoll
            String pollAuthor;
            String pollId;
            String pollQuestion;
            ArrayList<String> pollUsers = new ArrayList<>();

            /* TODO: use a single for cycle for parsing, unless it becomes too difficult to read,
             *  we could use a single ArrayList for all fields
             */

            int authorEndIndex = authorIndex;
            while (data.charAt(authorEndIndex) != fieldSeparator) {
                authorEndIndex++;
            }
            pollAuthor = data.substring(authorIndex, authorEndIndex - authorIndex);

            int idIndex = authorEndIndex + 1;
            int idEndIndex = idIndex;
            while (data.charAt(idEndIndex) != fieldSeparator) {
                idEndIndex++;
            }
            pollId = data.substring(idIndex, idEndIndex - idIndex);

            int questionIndex = idEndIndex + 1;
            int questionEndIndex = questionIndex;
            while (data.charAt(questionEndIndex) != fieldSeparator) {
                questionEndIndex++;
            }
            pollQuestion = data.substring(questionIndex, questionEndIndex - questionIndex);

            // TODO: test these two cycles
            int userEndIndex = questionEndIndex + 1;
            for (int userIndex = userEndIndex; userIndex < data.length(); userIndex += userEndIndex + 1) {
                while (data.charAt(userEndIndex) != fieldSeparator) {
                    userEndIndex++;
                }
                pollUsers.add(data.substring(userIndex, userEndIndex - userIndex));
            }
            // finished parsing message fields

            // creates a new Poll and adds it to a list of Polls
            TernaryPoll poll = new TernaryPoll();
        }
    }
}
