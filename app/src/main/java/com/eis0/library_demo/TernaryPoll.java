package com.eis0.library_demo;

import android.util.Log;

import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * This class provides the creation of polls.
 * Users involved in a specific poll can only
 * reply "Yes" or "No".
 *
 * @author Edoardo Raimondi with some advice from Giovanni Velludo, except where specified
 * otherwise.
 */
public class TernaryPoll extends Poll {

    private static int pollCount = 0; // TODO: save this value when the program is shutdown
    static final SMSPeer SELF_PEER = new SMSPeer("self");
    private enum PollResult {
        YES("Yes"), NO("No"), UNAVAILABLE("Unavailable");
        private String answer;
        PollResult(String pollAnswer) {
            this.answer = pollAnswer;
        }
        @Override
        public String toString() {
            return answer;
        }
    }
    private int pollID;
    private SMSPeer pollAuthor;
    private String pollName;
    private String pollQuestion;
    private HashMap<SMSPeer, PollResult> pollUsers;
    private static final String LOG_KEY = "APP_POLL";

    /**
     * Creates a local copy of a poll coming from another device.
     * @param name The name of the poll.
     * @param question The question asked to all users.
     * @param author The user who created the poll.
     * @param users Users included in the poll.
     * @param id The id of the poll.
     */
    TernaryPoll(String name, String question, SMSPeer author, int id, ArrayList<SMSPeer> users) {
        pollAuthor = author;
        pollID = id;
        pollName = name;
        pollQuestion = question;
        pollUsers = new HashMap<>();
        for (SMSPeer user : users) this.addUser(user);
    }

    /**
     * Creates a new poll from this device.
     * @param name The name of the poll.
     * @param question The question to ask all users.
     * @param users Users to include in the poll.
     */
    TernaryPoll(String name, String question, ArrayList<SMSPeer> users) {
        pollID = ++TernaryPoll.pollCount;
        pollName = name;
        pollQuestion = question;
        pollAuthor = SELF_PEER;
        pollUsers = new HashMap<>();
        for (SMSPeer user : users) this.addUser(user);
    }

    public String getPollName() {
        return pollName;
    }

    public String getPollQuestion() {
        return pollQuestion;
    }

    SMSPeer getPollAuthor() {
        return pollAuthor;
    }

    HashMap<SMSPeer, PollResult> getPollUsers() {
        return pollUsers;
    }

    boolean isClosed() {
        return !pollUsers.containsValue(PollResult.UNAVAILABLE);
    }

    public int getClosedPercentage() {
        float answerCount = countYes() + countNo();
        float ratio = answerCount/(float)pollUsers.size();
        return Math.round(ratio * 100);
    }

    public int countYes() {
        int count = 0;
        for (PollResult result: pollUsers.values())
            if(result == PollResult.YES) count++;
        return count;
    }

    public int countNo() {
        int count = 0;
        for (PollResult result: pollUsers.values())
            if(result == PollResult.NO) count++;
        return count;
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
        // At the beginning we have no feedback by the user
        PollResult result = PollResult.UNAVAILABLE;
        pollUsers.put(user, result);
    }

    /**
     * Set user's answer to yes.
     * @param user user who answered yes.
     */
    void setYes(SMSPeer user) {
        if(hasUser(user)) {
            PollResult result = PollResult.YES;
            pollUsers.put(user, result);
        }
        else Log.e(LOG_KEY, "Trying to manage an inexistent user");
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
        else Log.e(LOG_KEY, "Trying to manage an inexistent user");
    }

    /**
     * Return the answer of the specific user
     * @param user the user whose answer is being requested
     * @return String representing answer
     * @throws IllegalArgumentException when the user is not included in the poll
     */
    String getAnswer(SMSPeer user) throws IllegalArgumentException {
        if(hasUser(user)) return pollUsers.get(user).toString();
        else throw new IllegalArgumentException("The user is not part of the poll");
    }

    /**
     * @return poll ID.
     */
    public int getPollID() {
        return this.pollID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TernaryPoll that = (TernaryPoll) o;
        return pollID == that.pollID &&
                pollAuthor.equals(that.pollAuthor) &&
                pollName.equals(that.pollName) &&
                pollQuestion.equals(that.pollQuestion) &&
                pollUsers.equals(that.pollUsers);
    }
}
