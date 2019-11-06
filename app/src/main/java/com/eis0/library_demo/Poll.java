package com.eis0.library_demo;



    /**
     * Interface to implement to create a pool
     */
public interface Poll {

    /**
     * Returns ID pool
     */
    int getPoolId();

    /**
     * Set poll to yes
     */
     void setYes();

     /**
      * Set poll to no
      */
     void setNo();
    }
