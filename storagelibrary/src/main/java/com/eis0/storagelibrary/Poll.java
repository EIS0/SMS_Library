package com.eis0.storagelibrary;

import com.eis0.smslibrary.SMSPeer;

/**
 * Abstract class to extend to create a poll, it contains all the basics actions that a generic
 * poll could have. Management of users and answers is delelgated to subclasses.
 * @author Edoardo Raimondi
 * @author Giovanni Velludo
 * @author Matteo Carnelos
 */
abstract class Poll {

    private int pollId;
    private String pollName;
    private String pollQuestion;
    private SMSPeer pollAuthor;

    /**
     * Poll basic constructor, it sets all the variables that a poll needs in order to exist.
     * @param id The unique id for the poll to create.
     * @param name The name of the poll.
     * @param question The question of the poll.
     * @param author The author of the poll.
     * @author Matteo Carnelos
     */
    Poll(int id, String name, String question, SMSPeer author) {
        pollId = id;
        pollName = name;
        pollQuestion = question;
        pollAuthor = author;
    }

    /**
     * Get the poll unique id.
     * @return An integer representing the poll id.
     * @author Matteo Carnelos
     */
    public int getPollId() {
        return pollId;
    }

    /**
     * Get the poll name.
     * @return A string representing the poll name.
     * @author Matteo Carnelos
     */
    public String getPollName() {
        return pollName;
    }

    /**
     * Get the poll question.
     * @return A string representing the poll question.
     * @author Matteo Carnelos
     */
    public String getPollQuestion() {
        return pollQuestion;
    }

    /**
     * Get the poll author.
     * @return An SMSPeer representing the poll author.
     * @author Matteo Carnelos
     */
    public SMSPeer getPollAuthor() {
        return pollAuthor;
    }

    /**
     * Tell if a poll is closed.
     * @return True if the poll is closed, false otherwise.
     * @author Matteo Carnelos
     */
    abstract boolean isClosed();

    /**
     * Returns a percentage value representing the quantity of answers received in relationship
     * with the total number of users that have to answer.
     * @return The closed percentage as an integer value.
     * @author Matteo Carnelos
     */
    public abstract int getClosedPercentage();

    /**
     * Check if the user is in the poll.
     * @param user The user for which the check is being requested.
     * @return True if the user is in the poll, false otherwise.
     * @author Giovanni Velludo
     */
    abstract boolean hasUser(SMSPeer user);

    /**
     * Insert an user in the poll.
     * @param user The user to insert in the poll.
     * @author Giovanni Velludo
     */
    abstract void addUser(SMSPeer user);

    /**
     * Return the answer of a specific user.
     * @param user The user whose answer is being requested.
     * @return String representing the answer.
     * @author Giovanni Velludo
     */
    abstract String getAnswer(SMSPeer user);
}