package com.eis0.library_demo;

import java.util.ArrayList;
import java.util.Observable;

public class DataProvider extends Observable implements PollListener {

    private static DataProvider instance = null;
    private static ArrayList<TernaryPoll> incomingPolls = new ArrayList<>();
    private static ArrayList<TernaryPoll> openedPolls = new ArrayList<>();
    private static ArrayList<TernaryPoll> closedPolls = new ArrayList<>();
    private static PollManager pollManager = PollManager.getInstance();

    // Singleton Design Pattern
    private DataProvider() {
        pollManager.setPollListener(this);
    }

    public static DataProvider getInstance() {
        if(instance == null) instance = new DataProvider();
        return instance;
    }

    public static ArrayList<TernaryPoll> getIncomingPolls() {
        return incomingPolls;
    }

    public static ArrayList<TernaryPoll> getOpenedPolls() {
        return openedPolls;
    }

    public static ArrayList<TernaryPoll> getClosedPolls() {
        return closedPolls;
    }

    public void onPollReceived(TernaryPoll poll) {
        incomingPolls.add(poll);
        setChanged();
        notifyObservers(poll);
    }

    public void onSentPollUpdate(TernaryPoll poll) {
        if(poll.isClosed()) {
            openedPolls.remove(poll);
            closedPolls.add(poll);
        }
        else {
            int pollIndex = openedPolls.indexOf(poll);
            if(pollIndex == -1) openedPolls.add(poll);
            else openedPolls.set(pollIndex, poll);
        }
        setChanged();
        notifyObservers(poll);
    }
}
