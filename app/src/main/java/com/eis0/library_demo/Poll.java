package com.eis0.library_demo;

import com.eis0.smslibrary.SMSPeer;

/**
     * Interface to implement to create a poll
     */
public interface Poll {

    /**
     * Returns ID poll
     */
    int getPollId();

    /**
     * Set poll of a specific user to yes.
     * If not inside the map, it insert it.
     * @param user that said yes
     */
     void setYes(SMSPeer user);

     /**
      * Set poll of a specific user to no.
      * If not inside the map, it insert it.
      * @param user that said yes
      */
     void setNo(SMSPeer user);
    }
