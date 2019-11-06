package com.eis0.library_demo;

import android.util.Log;

import com.eis0.smslibrary.SMSPeer;

import java.util.HashMap;
import java.util.Map;

class AppPoll extends Poll {
    private enum PollResult {YES, NO, UNAVAILABLE}
    private static int pollCount = 0;
    private int pollId;
    private Map<String, PollResult> pollUsers;
    private final String LOG_KEY = "APP_POLL";

    AppPoll() {
        pollId = ++AppPoll.pollCount;
        pollUsers = new HashMap<>();
    }

    /**
     * Check if the user is in the poll
     * @param user the user for which the check is being requested
     * @return true if the user is in the poll, false otherwise
     */
    boolean hasUser(SMSPeer user){
        return pollUsers.containsKey(user.getAddress());
    }

    /**
     * Insert an user in the poll.
     * @param user the user to insert in the poll
     */
    void setUser(SMSPeer user){
        // at the beginning we have no feedback by the user
        PollResult result = PollResult.UNAVAILABLE;
        pollUsers.put(user.getAddress(), result);
    }

    /**
     * Set user's answer to yes.
     * @param user user who answered yes.
     */
    void setYes(SMSPeer user) {
        if (hasUser(user)) {
            PollResult result = PollResult.YES;
            pollUsers.put(user.getAddress(), result);
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
            pollUsers.put(user.getAddress(), result);
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
