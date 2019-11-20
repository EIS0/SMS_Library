package com.eis0.library_demo;

import com.eis0.library_demo.poll.main.TernaryPoll;
import java.util.ArrayList;
import java.util.Observable;

/**
 * It's the PollManager listener, it handles polls updates by splitting them out in three
 * categories (incoming, opened, closed) and providing them to the ListAdapters.
 *
 * @author Matteo Carnelos
 */
public class DataProvider extends Observable implements PollListener {

    // Must always be static for getInstance to work
    private static DataProvider instance = null;
    private static ArrayList<TernaryPoll> incomingPolls = new ArrayList<>();
    private static ArrayList<TernaryPoll> openedPolls = new ArrayList<>();
    private static ArrayList<TernaryPoll> closedPolls = new ArrayList<>();

    /**
     * DataProvider constructor, sets this as the PollManager listener.
     * It cannot be accessed from outside the class because this follows the Singleton Design Pattern.
     *
     * @author Matteo Carnelos
     */
    private DataProvider() {
        PollManager.getInstance().setPollListener(this);
    }

    /**
     * Returns a new instance of DataProvider if none exist, otherwise the one already created as per
     * the Singleton Design Patter.
     *
     * @return The only instance of this class.
     * @author Matteo Carnelos
     */
    public static DataProvider getInstance() {
        if (instance == null) instance = new DataProvider();
        return instance;
    }

    /**
     * Get all the incoming polls.
     *
     * @return The incoming polls ArrayList.
     * @author Matteo Carnelos
     */
    public static ArrayList<TernaryPoll> getIncomingPolls() {
        return incomingPolls;
    }

    /**
     * Get all the opened polls.
     *
     * @return The opened polls ArrayList.
     * @author Matteo Carnelos
     */
    public static ArrayList<TernaryPoll> getOpenedPolls() {
        return openedPolls;
    }

    /**
     * Get all the closed polls.
     *
     * @return The closed polls ArrayList.
     * @author Matteo Carnelos
     */
    public static ArrayList<TernaryPoll> getClosedPolls() {
        return closedPolls;
    }

    /**
     * Called whenever a new poll is received by other users, adds it to the incomingPoll list and
     * notifies all the observers.
     *
     * @param poll The poll received.
     * @author Matteo Carnelos
     */
    public void onPollReceived(TernaryPoll poll) {
        incomingPolls.add(poll);
        setChanged();
        notifyObservers(poll);
    }

    /**
     * Called whenever a sent poll is created or receives an update (after an answer). When a poll
     * receives all the answers it is considered closed, so it will be moved to the closedPolls list.
     *
     * @param poll The poll created or updated.
     * @author Matteo Carnelos
     */
    public void onSentPollUpdate(TernaryPoll poll) {
        // Two scenarios:
        // Poll Closed, move it from the openedPolls to the closedPolls list
        // Poll Opened, it can be either a new poll or an update to an existing one
        if (poll.isClosed()) {
            openedPolls.remove(poll);
            closedPolls.add(poll);
        } else {
            int pollIndex = openedPolls.indexOf(poll);
            if (pollIndex == -1) openedPolls.add(poll);
            else openedPolls.set(pollIndex, poll);
        }
        setChanged();
        notifyObservers(poll);
    }
}