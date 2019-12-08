package com.eis0.easypoll.poll;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * This class provides the creation of polls with two simple answers. Users involved in a
 * specific poll can only reply "Yes" or "No".
 *
 * @author Edoardo Raimondi
 * @author Giovanni Velludo
 * @author Matteo Carnelos
 */
public class BinaryPoll {

    private static final String POLLS_COUNT_KEY = "com.eis0.easypoll.polls_count_key";

    private static int pollsCount = 0;
    private static SharedPreferences mSharedPreferences;

    public enum PollResult {
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

    private int pollId;
    private String pollName;
    private String pollQuestion;
    private SMSPeer pollAuthor;
    private HashMap<SMSPeer, PollResult> pollUsers = new HashMap<>();

    // ---------------------------- CONSTRUCTORS ---------------------------- //

    /**
     * Creates a local copy of a poll coming from another device.
     *
     * @param author   The user who created the poll.
     * @param id       The id of the poll.
     * @param name     The name of the poll.
     * @param question The question asked to all users.
     * @throws IllegalArgumentException If the name or the question are empty.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    public BinaryPoll(SMSPeer author, int id, String name, String question) {
        if(name.isEmpty()) throw new IllegalArgumentException("Can't create poll with empty name");
        if(question.isEmpty()) throw new IllegalArgumentException("Can't create poll with empty question");
        pollId = id;
        pollName = name;
        pollQuestion = question;
        pollAuthor = author;
    }

    /**
     * Creates a new poll from this device.
     *
     * @param name     The name of the poll.
     * @param question The question to ask all users.
     * @param users    Users to include in the poll.
     * @throws IllegalArgumentException When one of the arguments is empty.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    public BinaryPoll(String name, String question, ArrayList<SMSPeer> users) {
        this(null, pollsCount+1, name, question);
        if(users.isEmpty()) throw new IllegalArgumentException("Can't create poll with no users");
        for (SMSPeer user : users) addUser(user);
        // If everything goes fine, saves the new pollsCount
        pollsCount++;
        savePollsCountToInternal();
    }

    // ---------------------------- DATA STORING ---------------------------- //

    /**
     * Set the SharedPreferences reference to which save the pollsCount value.
     *
     * @author Matteo Carnelos
     */
    public static void setSharedPreferences(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    /**
     * Save the polls counter (for Ids) to the internal storage.
     *
     * @author Matteo Carnelos
     */
    public static void savePollsCountToInternal() {
        if(mSharedPreferences == null) return;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(POLLS_COUNT_KEY, pollsCount);
        editor.apply();
    }

    /**
     * Load the polls counter (for Ids) from the internal storage.

     * @author Matteo Carnelos
     */
    public static void loadPollsCountFromInternal() {
        pollsCount = mSharedPreferences.getInt(POLLS_COUNT_KEY, 0);
    }

    // ---------------------------- GETTERS ---------------------------- //

    /**
     * Get the poll unique id.
     *
     * @return An integer representing the poll id.
     * @author Matteo Carnelos
     */
    public int getPollId() {
        return pollId;
    }

    /**
     * Get the poll name.
     *
     * @return A string representing the poll name.
     * @author Matteo Carnelos
     */
    public String getPollName() {
        return pollName;
    }

    /**
     * Get the poll question.
     *
     * @return A string representing the poll question.
     * @author Matteo Carnelos
     */
    public String getPollQuestion() {
        return pollQuestion;
    }

    /**
     * Get the poll author.
     *
     * @return An SMSPeer representing the poll author.
     * @author Matteo Carnelos
     */
    SMSPeer getPollAuthor() {
        return pollAuthor;
    }

    /**
     * Get all the users in the poll.
     *
     * @return A Set representing all the users.
     * @author Giovanni Velludo
     */
    Set<SMSPeer> getPollUsers() {
        return pollUsers.keySet();
    }

    /**
     * Return the answer of the specific user.
     *
     * @param user The user whose answer is being requested.
     * @return A string representing the answer.
     * @throws IllegalArgumentException when the user is not included in the poll.
     * @author Giovanni Velludo
     */
    public String getAnswer(SMSPeer user) throws IllegalArgumentException {
        if (hasUser(user)) return pollUsers.get(user).toString();
        else throw new IllegalArgumentException("The user is not part of the poll");
    }

    // ---------------------------- SETTERS ---------------------------- //

    /**
     * Set user's answer to "Yes".
     *
     * @param user User who answered "Yes".
     * @throws IllegalArgumentException If the specified user isn't in the poll.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    public void setYes(SMSPeer user) {
        if (hasUser(user)) {
            PollResult result = PollResult.YES;
            pollUsers.put(user, result);
        } else throw new IllegalArgumentException("Trying to manage an inexistent user");
    }

    /**
     * Set user's answer to "No".
     *
     * @param user User who answered "No".
     * @throws IllegalArgumentException If the specified user isn't in the poll.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    public void setNo(SMSPeer user) {
        if (hasUser(user)) {
            PollResult result = PollResult.NO;
            pollUsers.put(user, result);
        } else throw new IllegalArgumentException("Trying to manage an inexistent user");
    }

    // ---------------------------- INSPECTIONS ---------------------------- //

    /**
     * Check if the user is in the poll.
     *
     * @param user The user for which the check is being requested.
     * @return True if the user is in the poll, false otherwise.
     * @author Giovanni Velludo
     */
    public boolean hasUser(SMSPeer user) {
        return pollUsers.containsKey(user);
    }

    /**
     * Tell if a poll is closed, a poll is closed when there aren't users without a given answer
     * associated.
     *
     * @return True if the poll is closed, false otherwise.
     * @author Matteo Carnelos
     */
    public boolean isClosed() {
        return !pollUsers.containsValue(PollResult.UNAVAILABLE);
    }

    /**
     * Returns a percentage value representing the quantity of answers received in relationship
     * with the total number of users that have to answer.
     *
     * @return The closed percentage as an integer value.
     * @author Matteo Carnelos
     */
    public int getClosedPercentage() {
        float answerCount = countYes() + countNo();
        float ratio = answerCount / (float) pollUsers.size();
        return Math.round(ratio * 100);
    }

    // ---------------------------- ACTIONS ---------------------------- //

    /**
     * Insert an user in the poll.
     *
     * @param user The user to insert in the poll
     * @author Giovanni Velludo
     */
    public void addUser(SMSPeer user) {
        // At the beginning we have no feedback by the user
        PollResult result = PollResult.UNAVAILABLE;
        pollUsers.put(user, result);
    }

    /**
     * Get the number of answer "Yes".
     *
     * @return An integer value representing the number of "Yes".
     * @author Matteo Carnelos
     */
    public int countYes() {
        int count = 0;
        for (PollResult result : pollUsers.values())
            if (result == PollResult.YES) count++;
        return count;
    }

    /**
     * Get the number of answer "No".
     *
     * @return An integer value representing the number of "No".
     * @author Matteo Carnelos
     */
    public int countNo() {
        int count = 0;
        for (PollResult result : pollUsers.values())
            if (result == PollResult.NO) count++;
        return count;
    }

    // ---------------------------- OVERRIDDEN METHODS ---------------------------- //

    /**
     * Compare two BinaryPoll object and tell if they are equal.
     *
     * @param o The object to compare.
     * @return True if the two objects are equal, false otherwise.
     * @author Giovanni Velludo
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryPoll that = (BinaryPoll) o;

        // When comparing polls with no users, as they were received from another device
        if (that.pollUsers == null && this.pollUsers == null) {
            return this.getPollId() == that.getPollId() &&
                    this.getPollAuthor().equals(that.getPollAuthor()) &&
                    this.getPollName().equals(that.getPollName()) &&
                    this.getPollQuestion().equals(that.getPollQuestion());
        }
        // When comparing polls with no author, as they were sent from the device
        return this.getPollId() == that.getPollId() &&
                this.getPollName().equals(that.getPollName()) &&
                this.getPollQuestion().equals(that.getPollQuestion()) &&
                pollUsers.equals(that.pollUsers);
    }
}
