package com.example.webdictionary;

/**
 * A Kademlia node organizes its contacts in this class, with a maximun of K nodes.
 * Bucket zero has only one possible member, the key which differs from the nodeID only in the high order bit.
 */
public interface Kbucket {


    /**
     * Add a valid Node to the bucket
     */
    void addNode();


    /**
     *  Remove a Node from the bucket
     */
    void removeNode();


    /**
     * Return a list of the nodes currently in the bucket
     */
    NetworkNode[] getNodes();
}
