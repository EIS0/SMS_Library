package com.eis0.easypoll.poll;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eis0.networklibrary.Network;
import com.eis0.networklibrary.NetworksPool;

/**
 * This class represent a binary poll object, that is a poll where users can reply with "Yes" or "No".
 * Every poll has a {@link Network} of users and an author. Users can answer and see poll updates,
 * the author can only see poll updates (if the author want to submit his answer it has to be also
 * subscribed as a user).<br>
 * The author network is a special type of {@link Network}, always composed by one peer, that can
 * eventually be the local net, in case of polls created by the local host.<br>
 * A poll is universally identified by the combination of the author address and the persistent number
 * of the poll (stored in internal memory of the author).
 *
 * @author Matteo Carnelos
 */
public class BinaryPoll {

    private static final String POLLS_COUNT_KEY = "com.eis0.easypoll.polls_count_key";
    public static final String SELF_AUTHOR_NAME = "You";

    private static long pollsCount = 0;
    private static SharedPreferences mSharedPreferences;

    private long number;
    private String name;
    private String question;
    private Network author;
    private Network users;
    private int yesCount = 0;
    private int noCount = 0;
    private boolean closed = false;
    private boolean incoming;

    // ---------------------------- CONSTRUCTORS ---------------------------- //

    /**
     * Create a poll given all of its information. By default, every new poll is set as opened
     * (it hasn't received all the answers yet).
     *
     * @param number The owner's local number of the poll.
     * @param name The name of the poll.
     * @param question The question asked to all users.
     * @param author The one-peer {@link Network} representing the author of the poll.
     * @param users The {@link Network} representing users subscribed to the poll.
     * @throws IllegalArgumentException When one of the arguments is empty or different authors are given.
     * @author Matteo Carnelos
     */
    public BinaryPoll(long number, @NonNull String name, @NonNull String question,
                      @NonNull Network author, @NonNull Network users) {
        if(name.isEmpty()) throw new IllegalArgumentException("Can't create poll with empty name.");
        if(question.isEmpty()) throw new IllegalArgumentException("Can't create poll with empty question.");
        if(author.size() > 1) throw new IllegalArgumentException("There can only be one poll author.");
        if(users.isLocalNetwork()) throw new IllegalArgumentException("A poll must contain at least one user.");
        this.number = number;
        this.name = name;
        this.question = question;
        this.author = author;
        this.users = users;
        incoming = true;
    }

    /**
     * Create a new poll from this device.<br>
     * The poll number is calculated from the counter stored in the internal memory of the device.
     * The author's {@link Network} is the local network, that is the one composed only with the
     * local host, see {@link Network} for more details.
     *
     * @param name The name of the poll.
     * @param question The question to ask all users.
     * @param users Users to include in the poll.
     * @throws IllegalArgumentException If one of the arguments is empty.
     * @author Matteo Carnelos
     */
    public BinaryPoll(@NonNull String name, @NonNull String question, @NonNull Network users) {
        this(pollsCount+1, name, question, NetworksPool.obtainLocalNetwork(), users);
        incoming = false;
        pollsCount++;
        savePollsCountToInternal();
    }

    // ---------------------------- DATA STORING ---------------------------- //

    /**
     * Set the SharedPreferences reference to which save the {@link #pollsCount} value.
     * @param sharedPreferences The reference to the shared preferences of the application.
     *
     * @author Matteo Carnelos
     */
    public static void setSharedPreferences(@Nullable SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    /**
     * Save the polls counter (for ids) to the internal storage.<br>
     * Note: This method needs shared preferences to be previously set with
     * {@link #setSharedPreferences(SharedPreferences)} otherwise it will return doing nothing.
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
     * Load the polls counter (for ids) from the internal storage. If none is found, load the default
     * one (0).<br>
     * Note: This method needs shared preferences to be previously set with
     * {@link #setSharedPreferences(SharedPreferences)} otherwise it will return doing nothing.
     *
     * @author Matteo Carnelos
     */
    public static void loadPollsCountFromInternal() {
        if(mSharedPreferences == null) return;
        pollsCount = mSharedPreferences.getLong(POLLS_COUNT_KEY, 0);
    }

    // ---------------------------- GETTERS ---------------------------- //

    /**
     * Get the unique id for this poll.
     * The id is composed by joining the author address and his local poll number. If the author is the
     * local host the unique identifier is composed only by the local number, this because it can't exist
     * an author with an empty address that creates id conflicts.<br>
     * Examples:<br>
     * authorAddress: "3401234567"<br>
     * author's local number for this poll: "8"<br>
     * unique id = 34012345678<br>
     * ----------------------------------------<br>
     * authorAddress: "localnet"<br>
     * author's local number for this poll: "1"<br>
     * unique id = 1
     *
     * @return A long number representing the unique id.
     * @author Matteo Carnelos
     */
    public long getId() {
        return Long.parseLong(author.getAddresses().get(0) + getNumber());
    }

    /**
     * Get the local number of the poll.
     *
     * @return An integer representing the poll number.
     * @author Matteo Carnelos
     */
    public long getNumber() {
        return number;
    }

    /**
     * Get the poll name.
     *
     * @return A string representing the poll name.
     * @author Matteo Carnelos
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Get the poll question.
     *
     * @return A string representing the poll question.
     * @author Matteo Carnelos
     */
    @NonNull
    public String getQuestion() {
        return question;
    }

    /**
     * Get the poll users network.
     *
     * @return The {@link Network} object representing all the users subscribed to the poll.
     * @author Matteo Carnelos
     */
    @NonNull
    public Network getUsers() {
        return users;
    }

    /**
     * Get the author network. It can be the local network if the author is the local host.
     *
     * @return The {@link Network} object representing the author.
     * @author Matteo Carnelos
     */
    @NonNull
    public Network getAuthor() {
        return author;
    }

    /**
     * Get the author display name. This method is useful for displaying the name in the UI.
     * The local host display name is by default the {@link #SELF_AUTHOR_NAME}. For a generic
     * author, its name is represented by its address.
     *
     * @return A {@link String} representing the name of the author.
     */
    @NonNull
    public String getAuthorName() {
        if(author.isLocalNetwork()) return SELF_AUTHOR_NAME;
        return author.getAddresses().get(0);
    }

    /**
     * Get the number of "Yes" for this poll.
     *
     * @return An integer representing the number of "Yes".
     * @author Matteo Carnelos
     */
    public int getYesCount() {
        return yesCount;
    }

    /**
     * Get the number of "No" for this poll.
     *
     * @return An integer representing the number of "No".
     * @author Matteo Carnelos
     */
    public int getNoCount() {
        return noCount;
    }

    /**
     * Tell if the poll is incoming, that means it is coming from another device.
     *
     * @return True if the poll is incoming, false otherwise.
     * @author Matteo Carnelos
     */
    public boolean isIncoming() {
        return incoming;
    }

    /**
     * Tell if a poll is closed. A poll is closed when all the users have replied.
     *
     * @return True if the poll is closed, false otherwise.
     * @author Matteo Carnelos
     */
    public boolean isClosed() {
        return closed;
    }

    // ---------------------------- SETTERS ---------------------------- //

    /**
     * Set a user's answer and eventually close the poll.
     *
     * @param answer True if the answer is "Yes", false if the answer is "No".
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
     * with the total number of users that have to answer.
     *
     * @return The closed percentage as an integer value.
     * @author Matteo Carnelos
     */
    public int getClosedPercentage() {
        float answerCount = getYesCount() + getNoCount();
        float ratio = answerCount / (float) getUsersCount();
        return Math.round(ratio * 100);
    }

    /**
     * Get the total number of users for this poll.
     *
     * @return The users count as an integer value.
     */
    public int getUsersCount() {
        return users.size();
    }

    // ---------------------------- OVERRIDDEN METHODS ---------------------------- //

    /**
     * Compare two {@link BinaryPoll} objects and tell if they are equal. Two {@link BinaryPoll} are
     * said to be equals if they have the same id.
     *
     * @param o The object to compare.
     * @return True if the two objects are equal, false otherwise.
     * @author Matteo Carnelos
     */
    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryPoll that = (BinaryPoll) o;
        return this.getId() == that.getId();
    }
}
