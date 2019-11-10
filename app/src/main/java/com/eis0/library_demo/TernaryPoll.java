/**
 *
 * This class provides the creation of polls.
 * Users involved in a specific poll can only
 * reply "Yes" or "No".
 *
 * @author Edoardo Raimondi with some advice from Giovanni Velludo, except where specified
 * otherwise.
 */
package com.eis0.library_demo;

import android.util.Log;

import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class TernaryPoll extends Poll {
    private enum PollResult {
        YES("Yes"), NO("No"), UNAVAILABLE("Unavailable");
        private String answer;
        PollResult(String pollAnswer){
            this.answer = pollAnswer;
        }
        @Override
        public String toString(){
            return answer;
        }
    }
    private static int pollCount = 0;
    private int pollId;
    private SMSPeer pollAuthor;
    private String pollQuestion;
    private Map<SMSPeer, PollResult> pollUsers;
    private static final String LOG_KEY = "APP_POLL";

    /**
     * Creates a local copy of a poll coming from another device.
     * @param question the question to ask all users.
     * @param author the user creating the poll.
     * @param users users to include in the poll.
     * @param id the id of the poll.
     */
    TernaryPoll(String question, SMSPeer author, ArrayList<SMSPeer> users, int id) {
        pollAuthor = author;
        pollId = id;
        pollQuestion = question;
        pollUsers = new HashMap<>();
        for (SMSPeer user : users) this.addUser(user);
    }

    /**
     * Creates a new poll from this device.
     * @param question the question to ask all users.
     * @param author the user creating the poll.
     * @param users users to include in the poll.
     */
    TernaryPoll(String question, SMSPeer author, ArrayList<SMSPeer> users) {
        pollId = ++TernaryPoll.pollCount;
        pollQuestion = question;
        pollAuthor = author;
        pollUsers = new HashMap<>();
        for (SMSPeer user : users) this.addUser(user);
    }

    /**
     * Check if the user is in the poll
     * @param user the user for which the check is being requested
     * @return true if the user is in the poll, false otherwise
     */
    boolean hasUser(SMSPeer user){
        return pollUsers.containsKey(user);
    }

    /**
     * Insert an user in the poll.
     * @param user the user to insert in the poll
     */
    void addUser(SMSPeer user){
        // at the beginning we have no feedback by the user
        PollResult result = PollResult.UNAVAILABLE;
        pollUsers.put(user, result);
    }

    /**
     * Set user's answer to yes.
     * @param user user who answered yes.
     */
    void setYes(SMSPeer user) {
        if (hasUser(user)) {
            PollResult result = PollResult.YES;
            pollUsers.put(user, result);
        }
        else Log.i(LOG_KEY, "trying to manage an inexistent user");
    }

    /**
     * Set user's answer to no.
     * @param user user who answered no.
     */
    void setNo(SMSPeer user) {
        if (hasUser(user)) {
            PollResult result = PollResult.NO;
            pollUsers.put(user, result);
        }
        else Log.i(LOG_KEY, "trying to manage an inexistent user");
    }

    /**
     * Return the answer of the specific user
     * @param user the user whose answer is being requested
     * @return String representing answer
     * @throws IllegalArgumentException when the user is not included in the poll
     */
    String getAnswer(SMSPeer user) throws IllegalArgumentException {
        if (hasUser(user))
            return pollUsers.get(user).toString();
        else
            throw new IllegalArgumentException("The user is not part of the poll");
    }

    /**
     * @return poll ID.
     */
    int getPollId() {
        return this.pollId;
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
    private String pollToMessage() {
        String message = "0" + pollAuthor + "\r" + pollId + "\r" + pollQuestion + "\r";
        // adds each pollUser to the end of the message
        for (SMSPeer user : pollUsers.keySet()) {
            message = message + "\r" + user;
        }
        return message;
    }

    /**
     * Sends a poll as a text message to each pollUser.
     * @author Giovanni Velludo
     */
    private void sendPoll() {
        String message = this.pollToMessage();
        for (SMSPeer user : pollUsers.keySet()) {
            smsManager.sendMessage(new SMSMessage(user, message));
        }
    }

}
