package com.eis0.kademlia;

/**
 * Interface that defines a KadConfiguration object
 *
 * @author Edoardo Raimondi
 */
interface KadConfiguration {

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

}