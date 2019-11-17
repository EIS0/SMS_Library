package com.eis0.library_demo;

import androidx.annotation.NonNull;

import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * This class provides the creation of polls with two simple answers. Users involved in a
 * specific poll can only reply "Yes" or "No".
 * @author Edoardo Raimondi
 * @author Giovanni Velludo
 * @author Matteo Carnelos
 */
public class TernaryPoll extends Poll {

    static final SMSPeer SELF_PEER = new SMSPeer("self");

    private static int pollCount = 0;
    private enum PollResult {
        YES("Yes"), NO("No"), UNAVAILABLE("Unavailable");
        private String answer;
        PollResult(String pollAnswer) {
            this.answer = pollAnswer;
        }
        @NonNull
        @Override
        public String toString() {
            return answer;
        }
    }

    private HashMap<SMSPeer, PollResult> pollUsers;

    /**
     * Creates a local copy of a poll coming from another device.
     * @param author The user who created the poll.
     * @param id The id of the poll.
     * @param name The name of the poll.
     * @param question The question asked to all users.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    TernaryPoll(SMSPeer author, int id, String name, String question) {
        super(id, name, question, author);
    }

    /**
     * Creates a new poll from this device.
     * @param name The name of the poll.
     * @param question The question to ask all users.
     * @param users Users to include in the poll.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    TernaryPoll(String name, String question, ArrayList<SMSPeer> users) {
        super(++pollCount, name, question, SELF_PEER);
        pollUsers = new HashMap<>();
        for (SMSPeer user : users) addUser(user);
    }

    /**
     * Get all the users in the poll.
     * @return A Set representing all the users.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    Set<SMSPeer> getPollUsers() {
        return pollUsers.keySet();
    }

    /**
     * Tell if a poll is closed, a poll is closed when there aren't users without a given answer
     * associated.
     * @return True if the poll is closed, false otherwise.
     * @author Matteo Carnelos
     */
    boolean isClosed() {
        return !pollUsers.containsValue(PollResult.UNAVAILABLE);
    }

    /**
     * Returns a percentage value representing the quantity of answers received in relationship
     * with the total number of users that have to answer.
     * @return The closed percentage as an integer value.
     * @author Matteo Carnelos
     */
    public int getClosedPercentage() {
        float answerCount = countYes() + countNo();
        float ratio = answerCount/(float)pollUsers.size();
        return Math.round(ratio * 100);
    }

    /**
     * Get the number of answer "Yes".
     * @return An integer value representing the number of "Yes".
     * @author Matteo Carnelos
     */
    public int countYes() {
        int count = 0;
        for (PollResult result: pollUsers.values())
            if(result == PollResult.YES) count++;
        return count;
    }

    /**
     * Get the number of answer "No".
     * @return An integer value representing the number of "No".
     * @author Matteo Carnelos
     */
    public int countNo() {
        int count = 0;
        for (PollResult result: pollUsers.values())
            if(result == PollResult.NO) count++;
        return count;
    }

    /**
     * Check if the user is in the poll.
     * @param user The user for which the check is being requested.
     * @return True if the user is in the poll, false otherwise.
     * @author Edoardo Raimondi
     */
    boolean hasUser(SMSPeer user){
        return pollUsers.containsKey(user);
    }

    /**
     * Insert an user in the poll.
     * @param user The user to insert in the poll
     * @author Edoardo Raimondi
     */
    void addUser(SMSPeer user){
        // At the beginning we have no feedback by the user
        PollResult result = PollResult.UNAVAILABLE;
        pollUsers.put(user, result);
    }

    /**
     * Set user's answer to "Yes".
     * @param user User who answered "Yes".
     * @throws IllegalArgumentException If the specified user isn't in the poll.
     * @author Edoardo Raimondi
     * @author Matteo Carnelos
     */
    void setYes(SMSPeer user) {
        if(hasUser(user)) {
            PollResult result = PollResult.YES;
            pollUsers.put(user, result);
        }
        else throw new IllegalArgumentException("Trying to manage an inexistent user");
    }

    /**
     * Set user's answer to "No".
     * @param user user who answered "No".
     * @throws IllegalArgumentException If the specified user isn't in the poll.
     * @author Edoardo Raimondi
     * @author Matteo Carnelos
     */
    void setNo(SMSPeer user) {
        if (hasUser(user)) {
            PollResult result = PollResult.NO;
            pollUsers.put(user, result);
        }
        else throw new IllegalArgumentException("Trying to manage an inexistent user");
    }

    /**
     * Return the answer of the specific user.
     * @param user The user whose answer is being requested.
     * @return A string representing the answer.
     * @throws IllegalArgumentException when the user is not included in the poll.
     * @author Edoardo Raimondi
     */
    String getAnswer(SMSPeer user) throws IllegalArgumentException {
        if(hasUser(user)) return pollUsers.get(user).toString();
        else throw new IllegalArgumentException("The user is not part of the poll");
    }

    /**
     * @param   o the reference object with which to compare.
     * @return  {@code true} if this object is the same as the o
     *          argument; {@code false} otherwise.
     * @author Giovanni Velludo
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TernaryPoll that = (TernaryPoll) o;

        // when comparing polls with no users, as they were received from another device
        if (that.pollUsers == null && this.pollUsers == null) {
            return this.getPollId() == that.getPollId() &&
                    this.getPollAuthor().equals(that.getPollAuthor()) &&
                    this.getPollName().equals(that.getPollName()) &&
                    this.getPollQuestion().equals(that.getPollQuestion());
        }
        return this.getPollId() == that.getPollId() &&
                this.getPollAuthor().equals(that.getPollAuthor()) &&
                this.getPollName().equals(that.getPollName()) &&
                this.getPollQuestion().equals(that.getPollQuestion()) &&
                pollUsers.equals(that.pollUsers);
    }
}