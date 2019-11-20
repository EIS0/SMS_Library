package com.eis0.library_demo;

import android.content.Context;

import com.eis0.library_demo.poll.PollListStoring;
import com.eis0.library_demo.poll.PollStoring;
import com.eis0.library_demo.poll.TernaryPoll;

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
    private static PollStoring pollStoring = null  ;
    private static PollListStoring pollListStoring = null;
    private static ArrayList<TernaryPoll> incomingPolls = new ArrayList<>();
    private static ArrayList<TernaryPoll> openedPolls = new ArrayList<>();
    private static ArrayList<TernaryPoll> closedPolls = new ArrayList<>();
    private static String incomingPollsListName = "";
    private static String openedPollsListName = "";
    private static String closedPollsListName = "";
    private Context mContext = null;

    /**
     * DataProvider constructor, sets this as the PollManager listener.
     * It cannot be accessed from outside the class because this follows the Singleton Design Pattern.
     *
     * @author Matteo Carnelos
     */
    private DataProvider(Context context) {
        mContext = context;
        pollListStoring = new PollListStoring(mContext);
        pollStoring = new PollStoring();
        incomingPollsListName = pollListStoring.incomingListName;
        openedPollsListName = pollListStoring.openedListName;
        closedPollsListName = pollListStoring.closedListName;
        ArrayList<String> incomingPollsFiles = pollListStoring.getPollList(incomingPollsListName);
        ArrayList<String> openedPollsFiles = pollListStoring.getPollList(openedPollsListName);
        ArrayList<String> closedPollsFiles = pollListStoring.getPollList(closedPollsListName);
        PollManager.getInstance().setPollListener(this);
        for(String fileName : incomingPollsFiles) {
            incomingPolls.add(pollStoring.loadPoll(mContext, fileName));
        }
        for(String fileName :openedPollsFiles) {
            openedPolls.add(pollStoring.loadPoll(mContext, fileName));
        }
        for(String fileName :closedPollsFiles) {
            closedPolls.add(pollStoring.loadPoll(mContext, fileName));
        }

    }

    /**
     * Returns a new instance of DataProvider if none exist, otherwise the one already created as per
     * the Singleton Design Patter.
     *
     * @return The only instance of this class.
     * @author Matteo Carnelos
     */
    public static DataProvider getInstance(Context context) {
        if (instance == null) instance = new DataProvider(context);
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
        //Local list updated
        incomingPolls.add(poll);
        //File names list updated
        String pollFileName = pollStoring.setFileName(poll);
        pollListStoring.addToPollList(incomingPollsListName, pollFileName);
        //Remote copy of the local poll object updated in the InternalStorage
        pollStoring.savePoll(mContext, pollFileName, poll);
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
            //Local lists updated
            openedPolls.remove(poll);
            closedPolls.add(poll);
            //File names list updated
            String pollFileName = pollStoring.setFileName(poll);
            pollListStoring.addToPollList(incomingPollsListName, pollFileName);
            //Remote copy of the local poll object updated in the InternalStorage
            pollStoring.savePoll(mContext, pollFileName, poll);
        } else {
            int pollIndex = openedPolls.indexOf(poll);
            if (pollIndex == -1) openedPolls.add(poll);
            else openedPolls.set(pollIndex, poll);
        }
        setChanged();
        notifyObservers(poll);
    }
}