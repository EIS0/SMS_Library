package com.eis0.library_demo;

/**
 * Interface to implement to create a listener for new and modified Polls.
 * @author Giovanni Velludo
 */
public interface PollListener {

    /**
     * Called by PollManager whenever a new poll is received.
     * @param poll The poll received.
     * @author Giovanni Velludo
     */
    void onPollReceived(TernaryPoll poll);

    /**
     * Called by PollManager whenever a poll is updated or created.
     * @param poll The poll updated/created.
     * @author Giovanni Velludo
     */
    void onSentPollUpdate(TernaryPoll poll);
}