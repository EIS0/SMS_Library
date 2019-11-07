package com.eis0.library_demo;

import com.eis0.smslibrary.SMSPeer;

    /**
     * Interface to implement to create a pool
     */
public interface Poll {

        /**
         * @return Int representing ID pool
         */
        int getPoolId();

        /**
         * Return the answer of a specific user
         * @param user
         * @return String representing the answer
         */
        String getAnswer(SMSPeer user);
    }

