package com.eis0.webdictionary;

/**
 * @author Marco Cognolato
 */
public interface Resource<T> extends Serializable{
    /**
     * Returns the Resource data
     */
    T getResource();

}
