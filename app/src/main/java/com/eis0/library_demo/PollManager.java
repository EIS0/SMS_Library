package com.eis0.library_demo;

import android.util.Pair;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Creates and modifies TernaryPoll objects based on inputs from Activities and SMS Messages.
 * To be notified of changes to polls, your class must implement PollListener, and you must pass it
 * to an instance of PollManager with addPollListener(PollListener listener).
 * Right now it communicates directly with SMSManager, but in the future it will send messages to a
 * class handling messages longer than a single SMS.
 * @author Giovanni Velludo, except where specified otherwise.
 */
class PollManager implements ReceivedMessageListener<SMSMessage> {

    public static final SMSPeer SELF_PEER = new SMSPeer("self");
    private static final char FIELD_SEPARATOR = '\r';
    private static final int AUTHOR_INDEX = 1;
    private static PollManager instance = null; // Must always be static for getInstance to work
    // TODO: write polls to disk when the program is removed from memory
    private HashMap<Pair<SMSPeer, Integer>, TernaryPoll> polls = new HashMap<>();
    private PollListener pollListener;
    private SMSManager smsManager = SMSManager.getInstance();

    // Singleton Design Pattern
    private PollManager() {
        smsManager.addReceiveListener(this);
    }

    /**
     * Returns a new instance of PollManager if none exist, otherwise the one already created as per
     * the Singleton Design Patter.
     * @return The only instance of this class.
     */
    static PollManager getInstance() {
        if(instance == null) instance = new PollManager();
        return instance;
    }

    /**
     * Adds the listener listening for incoming TernaryPolls.
     * @param listener The listener to wake up when a message is received.
     */
    void addPollListener(PollListener listener) {
        pollListener = listener;
    }

    /**
     * Returns a poll given the author and the id.
     * @param author The creator of the poll.
     * @param id The creator's unique id of the poll.
     * @return The requested poll.
     * @throws InvalidParameterException When the given author and id are not associated to
     * any poll.
     */
    TernaryPoll getPoll(SMSPeer author, int id) throws InvalidParameterException {
        Pair<SMSPeer, Integer> key = new Pair<>(author, id);
        if (polls.containsKey(key)) return polls.get(key);
        else throw new InvalidParameterException(
                MessageFormat.format("Missing value for key {0}, {1}", author, id));
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
     * @param users Users to which the question should be asked.
     */
    void createPoll(String question, ArrayList<SMSPeer> users) {
        TernaryPoll poll = new TernaryPoll(question, PollManager.SELF_PEER, users);
        polls.put(new Pair<>(poll.pollAuthor, poll.pollId), poll);
        sendNewPoll(poll);
    }

    /**
     * Sets the answer of the user in the local copy of the poll and sends the updated poll to
     * the author.
     * @param author The author of the poll.
     * @param id ID of the poll.
     * @param user The current user of the application.
     * @param answer The user's answer, true equals "Yes" and false equals "No".
     */
    void answerPoll(SMSPeer author, int id, SMSPeer user, boolean answer) {
        if(author.equals(SELF_PEER)) throw new IllegalArgumentException("Trying to answer an owning poll");
        Pair<SMSPeer, Integer> key = new Pair<>(author, id);
        TernaryPoll poll = polls.get(key);
        if (answer) poll.setYes(user);
        else poll.setNo(user);
        polls.put(key, poll);
        sendAnswer(poll, user);
    }

    /**
     * Receives an SMSMessage and updates poll data accordingly.
     *
     * messageCode is the first header, and it's always one of the following values:
     * 0 when the message contains a new poll;
     * 1 when the message is sent from a user to the author and contains an answer;
     * 2 when the message is sent from the author to users and contains updated poll data.
     *
     * @param message The SMS messaged passed by SMSHandler
     */
    public void onMessageReceived(SMSMessage message) {
        // TODO: remove redundant SMSPeer fields and use the sender of the message instead
        // SMSPeer peer = message.getPeer();
        String data = message.getData();
        char messageCode = data.charAt(0);
        if (messageCode == '0') {
            /* Received a new TernaryPoll.
             *
             * SMSMessage fields:
             * messageCode + pollAuthor + pollId + pollQuestion + pollUsers[]
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

            int authorEndIndex = AUTHOR_INDEX;
            while (data.charAt(authorEndIndex) != FIELD_SEPARATOR) {
                authorEndIndex++;
            }
            pollAuthor = new SMSPeer(data.substring(AUTHOR_INDEX, authorEndIndex));

            int idIndex = authorEndIndex + 1;
            int idEndIndex = idIndex;
            while (data.charAt(idEndIndex) != FIELD_SEPARATOR) {
                idEndIndex++;
            }
            pollId = Integer.parseInt(data.substring(idIndex, idEndIndex));

            int questionIndex = idEndIndex + 1;
            int questionEndIndex = questionIndex;
            while (data.charAt(questionEndIndex) != FIELD_SEPARATOR) {
                questionEndIndex++;
            }
            pollQuestion = data.substring(questionIndex, questionEndIndex);

            int userIndex = questionEndIndex + 1;
            int userEndIndex = userIndex;
            while (userEndIndex < data.length() - 1) {
                while (data.charAt(userEndIndex) != FIELD_SEPARATOR
                        && userEndIndex < data.length() - 1) {
                    userEndIndex++;
                    }
                pollUsers.add(new SMSPeer(data.substring(userIndex, userEndIndex)));
                userIndex = userEndIndex + 1;
                userEndIndex = userIndex;
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

            int authorEndIndex = AUTHOR_INDEX;
            while (data.charAt(authorEndIndex) != FIELD_SEPARATOR) {
                authorEndIndex++;
            }
            pollAuthor = new SMSPeer(data.substring(AUTHOR_INDEX, authorEndIndex));

            int idIndex = authorEndIndex + 1;
            int idEndIndex = idIndex;
            while (data.charAt(idEndIndex) != FIELD_SEPARATOR) {
                idEndIndex++;
            }
            pollId = Integer.parseInt(data.substring(idIndex, idEndIndex));

            int userIndex = idEndIndex + 1;
            int userEndIndex = userIndex;
            while (data.charAt(userEndIndex) != FIELD_SEPARATOR) {
                userEndIndex++;
            }
            pollUser = new SMSPeer(data.substring(userIndex, userEndIndex));

            int resultIndex = userEndIndex + 1;
            pollResult = data.charAt(resultIndex) == '1';
            // finished parsing message fields

            // modifies pollResult of pollUser in Poll identified by pollId and pollAuthor
            Pair<SMSPeer, Integer> authorAndId = new Pair<>(pollAuthor, pollId);
            TernaryPoll poll = polls.get(authorAndId);
            if (pollResult) poll.setYes(pollUser);
            else poll.setNo(pollUser);
            polls.put(authorAndId, poll);
            // informs PollListener
            pollListener.onPollUpdated(poll);
            // sends modified poll to all users (except for the voter and the author)
            this.sendUpdatedPoll(poll, pollUser);


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

            int authorEndIndex = AUTHOR_INDEX;
            while (data.charAt(authorEndIndex) != FIELD_SEPARATOR) {
                authorEndIndex++;
            }
            pollAuthor = new SMSPeer(data.substring(AUTHOR_INDEX, authorEndIndex));

            int idIndex = authorEndIndex + 1;
            int idEndIndex = idIndex;
            while (data.charAt(idEndIndex) != FIELD_SEPARATOR) {
                idEndIndex++;
            }
            pollId = Integer.parseInt(data.substring(idIndex, idEndIndex));

            int userIndex = idEndIndex + 1;
            int userEndIndex = userIndex;
            while (data.charAt(userEndIndex) != FIELD_SEPARATOR) {
                userEndIndex++;
            }
            pollUser = new SMSPeer(data.substring(userIndex, userEndIndex));

            int resultIndex = userEndIndex + 1;
            pollResult = data.charAt(resultIndex) == '1';
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
     * @param poll The poll to send.
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
     * @param poll The poll to convert to a String.
     * @return The message to send to poll users.
     */
    private static String newPollToMessage(TernaryPoll poll) {
        // TODO: write getters in TernaryPoll and use those instead of accessing variables directly
        String message = "0" + poll.pollAuthor + "\r" + poll.pollId + "\r" + poll.pollQuestion;
        // adds each pollUser to the end of the message
        for (SMSPeer user : poll.pollUsers.keySet()) {
            message = message + "\r" + user;
        }
        return message;
    }

    /**
     * Sends an updated poll as a text message from the author to all users except author and voter.
     * @param poll The poll which was updated with a new answer.
     * @param voter The user who gave the new answer.
     */
    private void sendUpdatedPoll(TernaryPoll poll, SMSPeer voter) {
        String message = updatedPollToMessage(poll, voter);
        for (SMSPeer user : poll.pollUsers.keySet()) {
            if (!(user.equals(poll.pollAuthor) || user.equals(voter))) {
                smsManager.sendMessage(new SMSMessage(user, message));
            }
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
     * @param voter The user who casted their vote.
     * @return Message to send to poll users.
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
     * @param voter The current user, who casted their vote.
     * @return Message to send to poll users.
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
