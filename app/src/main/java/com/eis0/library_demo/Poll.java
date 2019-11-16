package com.eis0.library_demo;

import com.eis0.smslibrary.SMSPeer;

/**
 * Abstract class to extend to create a poll, it contains all the basics actions that a generic
 * poll could have.
 * @author Edoardo Raimondi
 * @author Giovanni Velludo
 * @author Matteo Carnelos
 */
abstract class Poll {

    private int pollId;
    private String pollName;
    private String pollQuestion;
    private SMSPeer pollAuthor;

    Poll(int id, String name, String question, SMSPeer author) {
        pollId = id;
        pollName = name;
        pollQuestion = question;
        pollAuthor = author;
    }

    /**
     * @return Int representing poll id
     */
    public int getPollId() {
        return pollId;
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

    abstract boolean isClosed();

    abstract int getClosedPercentage();

    /**
     * Check if the user is in the poll
     * @param user the user for which the check is being requested
     * @return true if the user is in the poll, false otherwise
     */
    abstract boolean hasUser(SMSPeer user);

    /**
     * Insert an user in the poll.
     * @param user the user to insert in the poll
     */
    abstract void addUser(SMSPeer user);

    /**
     * Return the answer of a specific user
     * @param user the user whose answer is being requested
     * @return String representing the answer
     */
    abstract String getAnswer(SMSPeer user);
}
