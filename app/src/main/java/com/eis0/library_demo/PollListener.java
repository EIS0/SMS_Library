package com.eis0.library_demo;

/**
 * Interface to implement to create a listener for new and modified TernaryPolls.
 * @author Giovanni Velludo
 */
public interface PollListener {

    /**
     * Called by PollManager whenever a new poll is received.
     * @param poll The poll received.
     */
    void onReceivePoll(TernaryPoll poll);

    /**
     * Called by PollManager whenever a poll is updated.
     * @param poll The poll updated.
     */
    void onSentPollUpdate(TernaryPoll poll);
}
