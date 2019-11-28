package com.eis0.kademlia;

/**
 * Interface that defines a KadConfiguration object
 */
public interface KadConfiguration {

    /**
     * @return K-Value used throughout Kademlia
     */
     int k();

    /**
     * @return Size of replacement cache.
     */
     int replacementCacheSize();

    /**
     * @return # of times a node can be marked as stale before it is actually removed.
     */
     int stale();

    /**
     * Creates the folder in which this node data is to be stored.
     *
     * @param ownerId
     *
     * @return The folder path
     */
     String getNodeDataFolder(String ownerId);
}