package com.eis0.storagelibrary;

import com.eis0.smslibrary.SMSPeer;

/**
 * Class to extend to create a poll
 */
public abstract class Poll {


    /**
     * @return Int representing poll ID
     */
    protected abstract int getPollId();

    /**
     * Check if the user is in the poll
     * @param user the user for which the check is being requested
     * @return true if the user is in the poll, false otherwise
     */
    protected abstract boolean hasUser(SMSPeer user);


    /**
     * Insert an user in the poll.
     * @param user the user to insert in the poll
     */
    protected abstract void addUser(SMSPeer user);

    /**
     * Return the answer of a specific user
     * @param user the user whose answer is being requested
     * @return String representing the answer
     */
    protected abstract String getAnswer(SMSPeer user);
}
