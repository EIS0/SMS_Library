package com.example.webdictionary;

/**
 * @author Marco Cognolato
 */
public interface Resource<T> extends Serializable{
    /**
     * Returns the Resource data
     */
    T getResource();

}
