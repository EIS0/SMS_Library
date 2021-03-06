package com.eis0.easypoll.poll;

/**
 * Interface to implement to create a listener for new and modified Polls.
 *
 * @author Giovanni Velludo
 * @author Matteo Carnelos
 */
public interface PollListener {

    /**
     * Called by PollManager whenever a poll is answered.
     *
     * @param poll The poll answered.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    void onPollAnswered(BinaryPoll poll);

    /**
     * Called by PollManager whenever a new poll is received.
     *
     * @param poll The poll received.
     * @author Giovanni Velludo
     */
    void onPollReceived(BinaryPoll poll);

    /**
     * Called by PollManager whenever a poll is updated or created.
     *
     * @param poll The poll updated/created.
     * @author Giovanni Velludo
     */
    void onSentPollUpdate(BinaryPoll poll);
}
