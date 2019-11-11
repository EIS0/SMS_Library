package com.eis0.library_demo;

import android.content.Context;
import android.util.Pair;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
    // TODO: write to disk when the program is removed from memory
    private HashMap<Pair<SMSPeer, Integer>, TernaryPoll> polls = new HashMap<>();
    private PollListener pollListener;
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
     * @return The only instance of this class.
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
     * Returns a poll given the author and the id.
     * @param author The creator of the poll.
     * @param id The creator's unique id of the poll.
     * @return The requested poll.
     * @throws InvalidParameterException When the given author and id are not associated to any poll.
     */
    TernaryPoll getPoll(SMSPeer author, int id) throws InvalidParameterException {
        Pair<SMSPeer, Integer> key = new Pair<>(author, id);
        if (polls.containsKey(key)) return polls.get(key);
        else throw new InvalidParameterException(MessageFormat.format("Missing value for key {0}, {1}", author, id));
    }

    /**
     * @return A collection containing all polls managed by PollManager.
     */
    Collection<TernaryPoll> getAllPolls() {
        // TODO: throw an exception when polls is empty?
        return polls.values();
    }

    /**
     * Creates a new poll and sends it to all included users, except for the current user.
     * @param question The question to ask users.
     * @param author The current user of the app.
     * @param users Users to which the question should be asked.
     */
    void createPoll(String question, SMSPeer author, ArrayList<SMSPeer> users) {
        TernaryPoll poll = new TernaryPoll(question, author, users);
        polls.put(new Pair<>(poll.pollAuthor, poll.pollId), poll);
        sendNewPoll(poll);
    }

    /**
     * Sets the answer of the user in the local copy of the poll and sends the updated poll to
     * the author. If the current user is also the author of the poll, he will receive an SMS from
     * himself and thus pollListener will be informed with onPollUpdated
     * @param author The author of the poll.
     * @param id ID of the poll.
     * @param user The current user of the application.
     * @param answer The user's answer, true equals "Yes" and false equals "No".
     */
    void answerPoll(SMSPeer author, int id, SMSPeer user, boolean answer) {
        // TODO: if user is the same as the author, he shouldn't waste a message to himself
        Pair<SMSPeer, Integer> key = new Pair<>(author, id);
        TernaryPoll poll = polls.get(key);
        if (answer) poll.setYes(user);
        else poll.setNo(user);
        polls.put(key, poll);
        sendAnswer(poll, user);
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
            /* Received a new TernaryPoll.
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
             *  we could use a single ArrayList for all fields and convert them later
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


        // TODO: merge handling of messages with messageCode 1 and 2, as it's almost the same
        } else if (messageCode == 1) {
            /* Received new answer from an user.
             *
             * SMSMessage fields:
             * messageCode + pollAuthor + pollId + pollUser + result
             * Fields are separated by the character CR, except for messageCode
             * and pollAuthor because the first is always only the first character.
             * result is 1 if the answer is yes, 0 if the answer is no.
             */
            // TODO: check if pollUser is the same as peer and act accordingly
            SMSPeer pollAuthor;
            int pollId;
            SMSPeer pollUser;
            boolean pollResult;

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

            int resultIndex = userEndIndex + 1;
            pollResult = data.substring(resultIndex).equals("1");
            // finished parsing message fields

            // modifies pollResult of pollUser in Poll identified by pollId and pollAuthor
            Pair<SMSPeer, Integer> authorAndId = new Pair<>(pollAuthor, pollId);
            TernaryPoll poll = polls.get(authorAndId);
            if (pollResult) poll.setYes(pollUser);
            else poll.setNo(pollUser);
            polls.put(authorAndId, poll);
            // informs PollListener
            pollListener.onPollUpdated(poll);
            // sends modified poll to all users (except for the voter)
            ArrayList<SMSPeer> destinations = new ArrayList<>();
            for (SMSPeer user : poll.pollUsers.keySet()){
                if (!user.equals(pollUser)) destinations.add(user); // pollUser is the voter
            }
            this.sendUpdatedPoll(poll, destinations, pollUser);


        } else if (messageCode == '2') {
            /* Received an update from pollAuthor.
             *
             * SMSMessage fields:
             * messageCode + pollAuthor + pollId + pollUser + result
             * Fields are separated by the character CR, except for messageCode
             * and pollAuthor because the first is always only the first character.
             * result is 1 if the answer is yes, 0 if the answer is no.
             */
            // TODO: check if pollAuthor is the same as peer and act accordingly
            SMSPeer pollAuthor;
            int pollId;
            SMSPeer pollUser;
            boolean pollResult;

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

            int resultIndex = userEndIndex + 1;
            pollResult = data.substring(resultIndex).equals("1");
            // finished parsing message fields

            // modifies pollResult of pollUser in Poll identified by pollId and pollAuthor
            Pair<SMSPeer, Integer> authorAndId = new Pair<>(pollAuthor, pollId);
            TernaryPoll poll = polls.get(authorAndId);
            if (pollResult) poll.setYes(pollUser);
            else poll.setNo(pollUser);
            polls.put(authorAndId, poll);
            // informs PollListener
            pollListener.onPollUpdated(poll);
        }
    }

    /**
     * Sends a new poll as a text message to each pollUser.
     * @author Giovanni Velludo
     */
    private void sendNewPoll(TernaryPoll poll) {
        String message = newPollToMessage(poll);
        for (SMSPeer user : poll.pollUsers.keySet()) {
            if (!user.equals(poll.pollAuthor)) {
                smsManager.sendMessage(new SMSMessage(user, message));
            }
        }
    }

    /**
     * Converts a new poll to the following String:
     * messageCode + pollAuthor + pollId + pollQuestion + pollUsers + CR
     * Fields are separated by the character CR, except for messageCode
     * and pollAuthor because the first is always only the first character.
     * Different pollUsers are separated by the character CR.
     *
     * messageCode assumes the following values:
     * 0 when the message contains a new poll
     * 1 when the message is sent from a user to the author and contains an answer
     * 2 when the message is sent from the author to users and contains updated poll data
     *
     * @return message to send to poll users
     * @author Giovanni Velludo
     */
    private static String newPollToMessage(TernaryPoll poll) {
        // TODO: write getters in TernaryPoll and use those instead of accessing variables directly
        String message = "0" + poll.pollAuthor + "\r" + poll.pollId + "\r" + poll.pollQuestion + "\r";
        // adds each pollUser to the end of the message
        for (SMSPeer user : poll.pollUsers.keySet()) {
            message = message + user + "\r";
        }
        return message;
    }

    /**
     * Sends an updated poll as a text message from the author to the target users.
     * @author Giovanni Velludo
     */
    private void sendUpdatedPoll(TernaryPoll poll, ArrayList<SMSPeer> users, SMSPeer voter) {
        String message = updatedPollToMessage(poll, voter);
        for (SMSPeer user : users) {
            smsManager.sendMessage(new SMSMessage(user, message));
        }
    }

    /**
     * Converts a poll update to the following String:
     * messageCode + pollAuthor + pollId + pollUser + pollResult
     * Fields are separated by the character CR, except for messageCode
     * and pollAuthor because the first is always only the first character.
     *
     * messageCode assumes the following values:
     * 0 when the message contains a new poll
     * 1 when the message is sent from a user to the author and contains an answer
     * 2 when the message is sent from the author to users and contains updated poll data
     *
     * @param poll The updated poll.
     * @return Message to send to poll users.
     * @author Giovanni Velludo
     */
    private static String updatedPollToMessage(TernaryPoll poll, SMSPeer voter) {
        // TODO: write getters in TernaryPoll and use those instead of accessing variables directly
        String message = "2" + poll.pollAuthor + "\r" + poll.pollId + "\r" + voter + "\r";
        int result;
        if (poll.getAnswer(voter).equals("Yes")) result = 1;
        else result = 0;
        return message + result;
    }

    /**
     * Sends an answer as a text message from a user to the author.
     * @param poll The poll which was answered.
     * @param voter The current user, who answered the poll.
     * @author Giovanni Velludo
     */
    private void sendAnswer(TernaryPoll poll, SMSPeer voter) {
        String message = answerToMessage(poll, voter);
        smsManager.sendMessage(new SMSMessage(poll.pollAuthor, message));
    }

    /**
     * Converts a poll answer to the following String:
     * messageCode + pollAuthor + pollId + pollUser + pollResult
     * Fields are separated by the character CR, except for messageCode
     * and pollAuthor because the first is always only the first character.
     *
     * messageCode assumes the following values:
     * 0 when the message contains a new poll
     * 1 when the message is sent from a user to the author and contains an answer
     * 2 when the message is sent from the author to users and contains updated poll data
     *
     * @param poll The updated poll.
     * @return Message to send to poll users.
     * @author Giovanni Velludo
     */
    private static String answerToMessage(TernaryPoll poll, SMSPeer voter) {
        // TODO: write getters in TernaryPoll and use those instead of accessing variables directly
        String message = "1" + poll.pollAuthor + "\r" + poll.pollId + "\r" + voter + "\r";
        int result;
        if (poll.getAnswer(voter).equals("Yes")) result = 1;
        else result = 0;
        return message + result;
    }
}
