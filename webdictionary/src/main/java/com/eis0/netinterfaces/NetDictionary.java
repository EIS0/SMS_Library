package com.eis0.netinterfaces;

import java.util.Map;

/**
 * Interface to define a network dictionary
 *
 * @param <K> The Key for each resource
 * @param <R> The Resource
 * @author Marco Cognolato
 * @author Giovanni Velludo
 */
public interface NetDictionary<K, R> {

    /**
     * Adds a resource to the network dictionary
     *
     * @param key      The key which defines the resource to be added
     * @param resource The resource to add
     */
    void addResource(K key, R resource);

    /**
     * Removes a resource from the dictionary
     *
     * @param key The key which defines the resource to be removed
     */
    void removeResource(K key);

    /**
     * Returns a resource in the dictionary
     *
     * @param key The key which defines the resource to get
     * @return Returns a resource corresponding to the key if present in the dictionary,
     * else returns null
     */
    R getResource(K key);

    /**
     * Removes all keys and resources from the dictionary.
     */
    void clear();
    Map<K, R> getDictionaryCopy();
}
