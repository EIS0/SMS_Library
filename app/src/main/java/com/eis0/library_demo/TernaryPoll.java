package com.eis0.library_demo;

import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * This class provides the creation of polls.
 * Users involved in a specific poll can only
 * reply "Yes" or "No".
 *
 * @author Edoardo Raimondi
 * @author Giovanni Velludo
 * @author Matteo Carnelos
 */
public class TernaryPoll extends Poll {

    static final SMSPeer SELF_PEER = new SMSPeer("self");

    // TODO: save this value when the program is shutdown
    private static int pollCount = 0;

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

    private Map<SMSPeer, PollResult> pollUsers;

    /**
     * Creates a local copy of a poll coming from another device.
     * @param question the question asked to all users.
     * @param author the user who created the poll.
     * @param id the id of the poll.
     */
    TernaryPoll(int id, String name, String question, SMSPeer author) {
        super(id, name, question, author);
    }

    /**
     * Creates a new poll from this device.
     * @param question the question to ask all users.
     * @param users users to include in the poll.
     */
    TernaryPoll(String name, String question, ArrayList<SMSPeer> users) {
        super(++pollCount, name, question, SELF_PEER);
        pollUsers = new HashMap<>();
        for (SMSPeer user : users) addUser(user);
    }

    public Set<SMSPeer> getPollUsers() {
        return pollUsers.keySet();
    }

    public boolean isClosed() {
        return !pollUsers.containsValue(PollResult.UNAVAILABLE);
    }

    public  int getClosedPercentage() {
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
        else throw new IllegalArgumentException("Trying to manage an inexistent user");
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
        else throw new IllegalArgumentException("Trying to manage an inexistent user");
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
}
