package com.eis0.library_demo;

import android.content.Context;
import android.util.Pair;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates and modifies TernaryPoll objects based on inputs from Activities and SMS Messages.
 * To be notified of changes to polls, your class must implement PollListener, and you must pass it
 * to an instance of PollManager with addPollListener(PollListener).
 * @author Giovanni Velludo
 */

class PollManager implements ReceivedMessageListener<SMSMessage> {

    private static PollManager instance = null; // must always be static for getInstance to work
    private static final char fieldSeparator = '\r';
    private static final int authorIndex = 1;
    private static Map<Pair<SMSPeer, Integer>, TernaryPoll> polls = new HashMap<>();
    private static PollListener pollListener;
    private Context context;
    private SMSManager smsManager;

    // Singleton Design Pattern
    private PollManager() {
        smsManager = SMSManager.getInstance(context);
    }

    /**
     * Returns a new instance of PollManager if none exist, otherwise the one already created as per
     * the Singleton Design Patter.
     * @param context Context of the application requesting a PollManager.
     * @return Single instance of this class.
     */
    static PollManager getInstance(Context context) {
        if(instance == null) instance = new PollManager();
        instance.context = context;
        return instance;
    }

    /**
     * Adds the listener watching for incoming TernaryPolls
     * @param listener The listener to wake up when a message is received
     */
    void addPollListener(PollListener listener) {
        pollListener = listener;
    }

    /**
     * Receives an SMSMessage and updates poll data accordingly.
     * If the message was sent by the pollAuthor: if the Poll already exists, updates the content of
     * the local copy of the Poll, otherwise creates a new Poll.
     * If the message was sent by a pollUser who is not the pollAuthor, sets his answer accordingly
     * and updates all other users.
     *
     * messageCode assumes the following values:
     * 0 when the message contains a new poll
     * 1 when the message is sent from a user to the author and contains an answer
     * 2 when the message is sent from the author to users and contains updated poll data
     *
     * @author Giovanni Velludo
     */
    public void onMessageReceived(SMSMessage message) {
        // SMSPeer peer = message.getPeer();
        String data = message.getData();
        char messageCode = data.charAt(0);
        if (messageCode == '0') {
            /* Received new TernaryPoll.
             *
             * SMSMessage fields:
             * messageCode + pollAuthor + pollId + pollQuestion + pollUsers + CR
             * Fields are separated by the character CR, except for messageCode
             * and pollAuthor because the first is always only the first character.
             * Different pollUsers are separated by the character CR.
             */
            // TODO: check if pollAuthor is the same as peer and act accordingly
            SMSPeer pollAuthor;
            int pollId;
            String pollQuestion;
            ArrayList<SMSPeer> pollUsers = new ArrayList<>();

            /* TODO: use a single for cycle for parsing, unless it becomes too difficult to read,
             *  we could use a single ArrayList for all fields
             */

            int authorEndIndex = authorIndex;
            while (data.charAt(authorEndIndex) != fieldSeparator) {
                authorEndIndex++;
            }
            pollAuthor = new SMSPeer(data.substring(authorIndex, authorEndIndex - authorIndex));

            int idIndex = authorEndIndex + 1;
            int idEndIndex = idIndex;
            while (data.charAt(idEndIndex) != fieldSeparator) {
                idEndIndex++;
            }
            pollId = Integer.parseInt(data.substring(idIndex, idEndIndex - idIndex));

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
                pollUsers.add(new SMSPeer(data.substring(userIndex, userEndIndex - userIndex)));
            }
            // finished parsing message fields

            // creates a new Poll and adds it to a Map of Polls
            TernaryPoll poll = new TernaryPoll(pollQuestion, pollAuthor, pollUsers, pollId);
            polls.put(new Pair<>(pollAuthor, pollId), poll);
            // informs PollListener
            pollListener.onNewPollReceived(poll);
        } else if (messageCode == 1) {
            /* Received new answer from an user.
             *
             * SMSMessage fields:
             * messageCode + pollAuthor + pollId + pollUser + result + CR
             * Fields are separated by the character CR, except for messageCode
             * and pollAuthor because the first is always only the first character.
             * result is 1 if the answer is yes, 0 if the answer is no.
             */
            // TODO: check if pollUser is the same as peer and act accordingly
            SMSPeer pollAuthor;
            int pollId;
            SMSPeer pollUser;
            boolean pollResult;

            /* TODO: use a single for cycle for parsing, unless it becomes too difficult to read,
             *  we could use a single ArrayList for all fields
             */

            int authorEndIndex = authorIndex;
            while (data.charAt(authorEndIndex) != fieldSeparator) {
                authorEndIndex++;
            }
            pollAuthor = new SMSPeer(data.substring(authorIndex, authorEndIndex - authorIndex));

            int idIndex = authorEndIndex + 1;
            int idEndIndex = idIndex;
            while (data.charAt(idEndIndex) != fieldSeparator) {
                idEndIndex++;
            }
            pollId = Integer.parseInt(data.substring(idIndex, idEndIndex - idIndex));

            int userIndex = idEndIndex + 1;
            int userEndIndex = userIndex;
            while (data.charAt(userEndIndex) != fieldSeparator) {
                userEndIndex++;
            }
            pollUser = new SMSPeer(data.substring(userIndex, userEndIndex - userIndex));

            // TODO: get next character instead of parsing
            int resultIndex = userEndIndex + 1;
            int resultEndIndex = resultIndex;
            while (data.charAt(resultEndIndex) != fieldSeparator) {
                resultEndIndex++;
            }
            if (data.substring(resultIndex, resultEndIndex - resultIndex).equalsIgnoreCase("1")) {
                pollResult = true;
            } else pollResult = false;


            // finished parsing message fields

            // modifies pollResult of pollUser in Poll identified by pollId and pollAuthor
            Pair<SMSPeer, Integer> authorAndId = new Pair<SMSPeer, Integer>(pollAuthor, pollId);
            TernaryPoll poll = polls.get(authorAndId);
            if (pollResult) poll.setYes(pollUser);
            else poll.setNo(pollUser);
            polls.put(authorAndId, poll);
            // informs PollListener
            // TODO: send modified poll to all users (except for the voter)
        }
    }
}
