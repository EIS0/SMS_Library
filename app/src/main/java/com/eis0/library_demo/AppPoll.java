package com.eis0.library_demo;


import com.eis0.smslibrary.SMSPeer;

import java.util.HashMap;
import java.util.Map;

public class AppPoll implements Poll {
    public enum resultPoll {YES, NO, INDISPONIBILE}
    private static int poolCount = 0;
    private int poolId;
    private Map<String, resultPoll> pollUsers;

     public AppPoll() {
         poolId = ++this.poolCount;
         pollUsers = new HashMap<>();
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
     * If not inside the map, it insert it.
     * @param user that said yes.
     */
    public void setYes(SMSPeer user) {
         resultPoll result = resultPoll.YES;
         pollUsers.put(user.getAddress(), result);
     }

    /**
     * Set specific user pool to no.
     * If not inside the map, it insert it.
     * @param user that said yes.
     */
    public void setNo(SMSPeer user) {
        resultPoll result = resultPoll.NO;
        pollUsers.put(user.getAddress(), result);
    }

    /**
     * @return poll ID.
     */
     public int getPoolId() {
         return this.poolId;
     }
}
