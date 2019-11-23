package com.example.webdictionary;


import androidx.annotation.NonNull;

/**
 * A resource key or value has to be represented by a string in order to be sent by a sms.
 * Every user-defined key or value extends this class.
 * That force the user to override equals() and toString().
 * @author Edoardo Raimondi
 */
public abstract class SerializableObject{

    /**
     * Mandatory override for equals()
     *
     * @param toCompare object to compare
     * @return true objects are the same
     */
    public abstract boolean equals(Object toCompare);

    /**
     * Mandatory override for toString()
     *
     * @return a string representing the state of the object
     */

    @NonNull
    public abstract String toString();

}

