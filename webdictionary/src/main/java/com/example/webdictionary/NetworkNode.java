package com.example.webdictionary;

/**
 * Extend this interface to extend your type of NetworkNode
 * @author Edoardo Raimondi
 */
interface NetworkNode {

    /**
     * return node ID
     */
    int getId();

    /**
     * Use this method to join the network (be added to the routing table)
     */
    void join();

    /**
     * Use this method to leave the network (be removed by the routing table)
     */
    void leave();

    /**
     * return boot peer's routing table
     */
    String[] copyRoutingTable();

    /**
     * return the closest node
     */
    NetworkNode findClosest();

}
