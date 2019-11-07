package com.eis0.library_demo;

import android.content.Context;
import android.util.Log;

import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.HashMap;
import java.util.Map;

class TernaryPoll extends Poll {
    private enum PollResult {YES, NO, UNAVAILABLE}
    private static int pollCount = 0;
    private int pollId;
    private String pollQuestion;
    private Map<SMSPeer, PollResult> pollUsers;
    private final static String LOG_KEY = "APP_POLL";

    TernaryPoll() {
        pollId = ++TernaryPoll.pollCount;
        pollQuestion = "";
        pollUsers = new HashMap<>();
    }

    /**
     * Creates a poll and sends it to all users included in it
     * @param question the question to ask all users
     * @param users users to include in the poll
     * @param context context of the application to use when needed, needed by SMSManager
     */
    TernaryPoll(String question, SMSPeer[] users, Context context) {
        pollId = ++TernaryPoll.pollCount;
        pollQuestion = question;
        pollUsers = new HashMap<>();
        for (SMSPeer user : users) this.addUser(user);
        for (SMSPeer user : pollUsers.keySet()) {
            // TODO: send Poll instead of empty message
            SMSManager.getInstance(context).sendMessage(new SMSMessage(user, ""));
        }
    }

    /**
     * Check if the user is in the poll
     * @param user the user for which the check is being requested
     * @return true if the user is in the poll, false otherwise
     */
    boolean hasUser(SMSPeer user){
        return pollUsers.containsKey(user);
    }

    /**
     * Insert an user in the poll.
     * @param user the user to insert in the poll
     */
    void addUser(SMSPeer user){
        // at the beginning we have no feedback by the user
        PollResult result = PollResult.UNAVAILABLE;
        pollUsers.put(user, result);
    }

    /**
     * Set user's answer to yes.
     * @param user user who answered yes.
     */
    void setYes(SMSPeer user) {
        if (hasUser(user)) {
            PollResult result = PollResult.YES;
            pollUsers.put(user, result);
        }
        else Log.i(LOG_KEY, "trying to manage an inexistent user");
    }

    /**
     * Set user's answer to no.
     * @param user user who answered no.
     */
    void setNo(SMSPeer user) {
        if (hasUser(user)) {
            PollResult result = PollResult.NO;
            pollUsers.put(user, result);
        }
        else Log.i(LOG_KEY, "trying to manage an inexistent user");
    }

    /**
     * @return poll ID.
     */
    int getPollId() {
        return this.pollId;
    }
}
