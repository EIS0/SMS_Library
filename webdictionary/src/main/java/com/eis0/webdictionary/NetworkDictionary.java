package com.eis0.webdictionary;


import com.eis0.smslibrary.Peer;

/**
 * Extend this class to extend your type of NetworkDictionary,
 * Simply instantiate this to use it
 * @author Marco Cognolato
 */
interface NetworkDictionary<P extends Peer, K, V> {


    /**
     * Returns the list of resources of a given peer if present, else returns null
     */
    V getResource(K key);

    /**
     * Returns the list of Peers currently on the network dictionary
     */
    K[] getAvailableKeys();

    /**
     * Returns the list of resources currently on the network dictionary
     */
    V[] getAvailableResources();

    /**
     * Adds a valid Peer-Resources[] couple to the network dictionary
     * @param peer A peer to add to the dictionary
     * @param resource A list of Resources to add to the dictionary
     */
    void add(K peer, V resource);

    /**
     * Removes a given valid Peer (and all its Resources) from the network dictionary
     */
    void remove(K peer);
}
