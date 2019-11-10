package com.eis0.library_demo;

/**
 * Interface to implement to create a listener for new TernaryPolls.
 * @author Giovanni Velludo
 */
interface NewPollListener {

    /**
     * Called by PollManager whenever a new poll is received.
     * @param poll The poll received.
     */
    void onNewPollReceived(TernaryPoll poll);
}
