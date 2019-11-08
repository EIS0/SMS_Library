package com.eis0.library_demo;

import com.eis0.smslibrary.SMSPeer;

    /**
 * Class to extend to create a poll
 */
abstract class Poll {

        /**
         * @return Int representing ID pool
         */
        abstract int getPollId();


        /**
         * Return the answer of a specific user
         * @param user
         * @return String representing the answer
         */
        abstract String getAnswer(SMSPeer user);
    }
