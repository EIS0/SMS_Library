package com.example.kademlia;

/**
 * Interface that defines a KadConfiguration object
 */
public interface KadConfiguration
{

    /**
     * @return K-Value used throughout Kademlia
     */
    public int k();

    /**
     * @return Size of replacement cache.
     */
    public int replacementCacheSize();

    /**
     * @return # of times a node can be marked as stale before it is actually removed.
     */
    public int stale();

    /**
     * Creates the folder in which this node data is to be stored.
     *
     * @param ownerId
     *
     * @return The folder path
     */
    public String getNodeDataFolder(String ownerId);

}