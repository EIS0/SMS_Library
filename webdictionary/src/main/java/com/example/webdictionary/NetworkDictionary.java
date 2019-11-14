package com.example.webdictionary;


/**
 * Extend this class to extend your type of NetworkDictionary,
 * Simply instantiate this to use it
 * @author Marco Cognolato
 */
interface NetworkDictionary<K extends Key, V extends Resource> {

    /**
     * Finds the Peer that has a given valid resource, if available, else returns null
     */
    K findKeyWithResource(V resource);

    /**
     * Returns the list of resources of a given peer if present, else returns null
     */
    V[] findPeerResources(K key);

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
     * @param resources A list of Resources to add to the dictionary
     */
    void add(K peer, V[] resources);

    /**
     * Removes a given valid Peer (and all its Resources) from the network dictionary
     */
    void remove(K peer);
}
