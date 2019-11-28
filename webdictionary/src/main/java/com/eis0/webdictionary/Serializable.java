package com.eis0.webdictionary;

/**
 * Common structure of a Serializable object
 * An object is defined as "Serializable" if it can be
 * converted to a given type T that identifies that object
 * @author Marco Cognolato
 */
public interface Serializable<T> {
    /**
     * A serializable object must be serialized,
     * meaning that it must be converted to a type T identifying that object
     * @return A type T object that identifies the object serialized
     */
    T serialize();
}
