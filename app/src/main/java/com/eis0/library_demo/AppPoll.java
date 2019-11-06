package com.eis0.library_demo;


public class AppPoll implements Poll {
    public enum resultPoll {YES, NO, INDISPONIBILE}
    private static int poolCount = 0;
    private int poolId;
    private resultPoll result;

     public AppPoll() {
         poolId = ++this.poolCount;
         result = resultPoll.INDISPONIBILE;
     }

    /**
     * Set poll to yes
     */
    public void setYes() {
         result = resultPoll.YES;
     }

    /**
     * Set poll to no
     */
    public void setNo() {
        result = resultPoll.NO;
    }

    /**
     * @return poll ID
     */
     public int getPoolId() {
         return this.poolId;
     }
}
