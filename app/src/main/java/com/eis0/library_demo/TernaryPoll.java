package com.eis0.library_demo;

import android.content.Context;
import android.util.Log;

import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

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
     * Creates an empty poll given the author
     * @param author the user creating the poll
     */
    TernaryPoll(SMSPeer author) {
        pollAuthor = author;
        pollId = ++TernaryPoll.pollCount;
        pollQuestion = "";
        pollUsers = new HashMap<>();
    }

    /**
     * Creates a poll and sends it to all users included in it
     * @param question the question to ask all users
     * @param users users to include in the poll
     * @param activity context of the application creating the poll, needed by SMSManager
     */
    TernaryPoll(String question, SMSPeer[] users, Context activity) {
        pollId = ++TernaryPoll.pollCount;
        pollQuestion = question;
        pollUsers = new HashMap<>();
        for (SMSPeer user : users) this.addUser(user);
        this.sendPoll(activity);
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
     * messageCode + pollAuthor + pollId + pollQuestion + pollUsers
     * Fields are separated by the character CR, except for messageCode
     * and pollAuthor because the first is always only the first character.
     * Various pollUsers are separated by the character CR.
     *
     * messageCode assumes the following values:
     * 0 when the message contains a new poll
     * 1 when the message is sent from a user to the author and contains an answer
     * 2 when the message is sent from the author to users and contains updated poll data
     *
     * @return message to send to poll users
     */
    private String pollToMessage() {
        String message = "0" + pollAuthor + "\r" + pollId + "\r" + pollQuestion;
        // adds each pollUser to the end of the message
        for (SMSPeer user : pollUsers.keySet()) {
            message = message + "\r" + user;
        }
        return message;
    }

    private void sendPoll(Context activity) {
        String message = this.pollToMessage();
        for (SMSPeer user : pollUsers.keySet()) {
            SMSManager.getInstance(activity).sendMessage(new SMSMessage(user, message));
        }
    }
}
