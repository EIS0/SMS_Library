package com.example.webdictionary;

/**
 * Class to implements to create your kademlia NetoworkNode
 * ID has to be a binary number of length B = 160 bits
 * @author Edoardo Raimondi
 */
interface NetworkNode {

    /**
     * return node ID
     */
    int getId();

    /**
     * A node joins the network as follows:
     * 1. If it does not already have a nodeID n, it generates one
     * 2. It inserts the value of some known node c into the appropriate bucket as its first contact
     * 3. It does an iterativeFindNode for n
     * 4. It refreshes all buckets further away than its closest neighbor, which will be in the occupied bucket with the lowest index.
     * If the node saved a list of contacts and used one of these as the "known node" it would be consistent with this protocol.
     */
    void join();

    /**
     * Use this method to leave the network
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
