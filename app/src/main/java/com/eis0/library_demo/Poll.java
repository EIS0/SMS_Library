package com.eis0.library_demo;

import com.eis0.smslibrary.SMSPeer;

/**
 * Interface to implement to create a poll
 */
abstract class Poll {

    /**
     * @return poll ID
     */
    abstract int getPollId();

    /**
     * Sets user's answer to yes.
     * If not inside the map, this method inserts it.
     * @param user who answered yes
     */
    abstract void setYes(SMSPeer user);

    /**
     * Sets user's answer to no.
     * If not inside the map, this method inserts it.
     * @param user who answered no
     */
    abstract void setNo(SMSPeer user);
}
