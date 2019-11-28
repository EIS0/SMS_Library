package com.example.webdictionary;

/**
 * General interface of the network vocabulary.
 * @author Marco Cognolato
 */
interface NetworkVocabulary<K, V> {

    /**
     * Returns Resource of a key if present, else returns null
     */
    V getResource(K key);

    /**
     * Adds a valid Key-Resource couple to the network vocabulary
     * @param key A key to add to the vocabulary
     * @param resource A Resource to add to the vocabulary for the specific key
     */
    void add(K key, V resource);

    /**
     * Removes a given valid Key (and its Resource pair) from the network vocabulary
     * @param key The key to remove from the Vocabulary
     */
    void remove(K key);
}
