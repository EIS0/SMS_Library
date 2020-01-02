package com.eis0.easypoll.poll;

import android.content.SharedPreferences;

import com.eis0.networklibrary.Network;
import com.eis0.networklibrary.NetworksPool;

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
    public static final String SELF_OWNER_NAME = "You";

    private static long pollsCount = 0;
    private static SharedPreferences mSharedPreferences;

    private long id;
    private String name;
    private String question;
    private Network authors;
    private Network users;
    private int yesCount = 0;
    private int noCount = 0;
    private boolean closed = false;
    private boolean incoming;

    // ---------------------------- CONSTRUCTORS ---------------------------- //

    /**
     * Creates a local copy of a poll coming from another device.
     *
     * @param id       The id of the poll.
     * @param name     The name of the poll.
     * @param question The question asked to all users.
     * @throws IllegalArgumentException When one of the arguments is empty.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    public BinaryPoll(long id, String name, String question, Network authors, Network users) {
        if(name.isEmpty()) throw new IllegalArgumentException("Can't create poll with empty name.");
        if(question.isEmpty()) throw new IllegalArgumentException("Can't create poll with empty question.");
        if(users.isLocalNetwork()) throw new IllegalArgumentException("A poll must contain at least one user.");
        this.id = id;
        this.name = name;
        this.question = question;
        this.authors = authors;
        this.users = users;
        incoming = true;
    }

    /**
     * Creates a new poll from this device.
     *
     * @param name     The name of the poll.
     * @param question The question to ask all users.
     * @param users    Users to include in the poll.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    public BinaryPoll(String name, String question, Network users) {
        this(pollsCount+1, name, question, NetworksPool.obtainLocalNetwork(), users);
        incoming = false;
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
    private static void savePollsCountToInternal() {
        if(mSharedPreferences == null) return;
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(POLLS_COUNT_KEY, pollsCount);
        editor.apply();
    }

    /**
     * Load the polls counter (for Ids) from the internal storage.

     * @author Matteo Carnelos
     */
    public static void loadPollsCountFromInternal() {
        pollsCount = mSharedPreferences.getLong(POLLS_COUNT_KEY, 0);
    }

    // ---------------------------- GETTERS ---------------------------- //

    public long getId() {
        if(authors.isLocalNetwork()) return getLocalId();
        return Long.parseLong(authors.getAddresses().get(0) + getLocalId());
    }

    /**
     * Get the poll unique id.
     *
     * @return An integer representing the poll id.
     * @author Matteo Carnelos
     */
    public long getLocalId() {
        return id;
    }

    /**
     * Get the poll name.
     *
     * @return A string representing the poll name.
     * @author Matteo Carnelos
     */
    public String getName() {
        return name;
    }

    /**
     * Get the poll question.
     *
     * @return A string representing the poll question.
     * @author Matteo Carnelos
     */
    public String getQuestion() {
        return question;
    }

    public Network getUsers() {
        return users;
    }

    public Network getAuthors() {
        return authors;
    }

    public String getOwnerName() {
        if(authors.isLocalNetwork()) return SELF_OWNER_NAME;
        return authors.getAddresses().get(0);
    }

    public int getYesCount() {
        return yesCount;
    }

    public int getNoCount() {
        return noCount;
    }

    public boolean isIncoming() {
        return incoming;
    }

    /**
     * Tell if a poll is closed, a poll is closed when there aren't users without a given setAnswer
     * associated.
     *
     * @return True if the poll is closed, false otherwise.
     * @author Matteo Carnelos
     */
    public boolean isClosed() {
        return closed;
    }

    // ---------------------------- SETTERS ---------------------------- //

    /**
     * Set user's answer.
     *
     * @param answer True if the answer is "Yes", false if the answer is "No".
     * @throws IllegalArgumentException If the specified user isn't in the poll.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    public void setAnswer(boolean answer) {
        if(answer) yesCount++;
        else noCount++;
        closed = (yesCount + noCount == getUsersCount());
    }

    // ---------------------------- INSPECTIONS ---------------------------- //

    /**
     * Returns a percentage value representing the quantity of answers received in relationship
     * with the total number of users that have to setAnswer.
     *
     * @return The closed percentage as an integer value.
     * @author Matteo Carnelos
     */
    public int getClosedPercentage() {
        float answerCount = getYesCount() + getNoCount();
        float ratio = answerCount / (float) getUsersCount();
        return Math.round(ratio * 100);
    }

    public int getUsersCount() {
        return authors.isLocalNetwork() ? users.size() : users.size()+1;
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
        return this.getLocalId() == that.getLocalId() && this.getAuthors().equals(that.getAuthors());
    }
}
