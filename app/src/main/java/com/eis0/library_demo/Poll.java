package com.eis0.library_demo;

import com.eis0.smslibrary.SMSPeer;

/**
 * Class to extend to create a poll
 */
abstract class Poll {

    /**
     * @return Int representing poll ID
     */
    abstract int getPollId();

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
