package com.eis0.library_demo;


import android.util.Log;

import com.eis0.smslibrary.SMSPeer;

import java.util.HashMap;
import java.util.Map;

public class AppPoll implements Poll {
    public enum resultPoll {YES, NO, INDISPONIBILE}
    private static int pollCount = 0;
    private int pollId;
    private Map<String, resultPoll> pollUsers;
    private String LOG_KEY = "APP_POLL";

     public AppPoll() {
         pollId = ++this.pollCount;
         pollUsers = new HashMap<>();
     }

    /**
     * Check if the user is in the poll
     * @param user
     */
    public boolean hasUser(SMSPeer user){
        return pollUsers.containsKey(user.getAddress());
    }

    /**
     * Insert an user in the poll.
     * Ask for a SMSPeer representing an user
     */
    public void setUser(SMSPeer user){
        resultPoll result = resultPoll.INDISPONIBILE; //at the beginning we have no feedback by the user
         pollUsers.put(user.getAddress(), result);
     }

    /**
     * Set specific user poll to yes.
     * @param user that said yes.
     */
    public void setYes(SMSPeer user) {
        if(hasUser(user)) {
            resultPoll result = resultPoll.YES;
            pollUsers.put(user.getAddress(), result);
        }
        else Log.i(LOG_KEY, "trying to manage an inexistent user");
     }

    /**
     * Set specific user poll to no.
     * @param user that said yes.
     */
    public void setNo(SMSPeer user) {
        if(hasUser(user)) {
            resultPoll result = resultPoll.NO;
            pollUsers.put(user.getAddress(), result);
        }
        else Log.i(LOG_KEY, "trying to manage an inexistent user");
    }

    /**
     * @return poll ID.
     */
     public int getPollId() {
         return this.pollId;
     }
}
