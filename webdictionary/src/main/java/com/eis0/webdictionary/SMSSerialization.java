package com.eis0.webdictionary;

import androidx.annotation.NonNull;

public class SMSSerialization extends SerializableObject<String> {

    private final String data;

    /**
     * Creates a Serializable object from the String to serialize
     * @param data The data serialized into a String
     */
    public SMSSerialization(String data){
        this.data = data;
    }

    /**
     * Serializes an object into a String
     * @return The String serialization of the object
     */
    public String serialize(){
        return data;
    }

    /**
     * Mandatory override for equals()
     *
     * @param toCompare object to compare
     * @return true objects are the same
     */
    public boolean equals(Object toCompare){
        if(toCompare == null || toCompare.getClass() != this.getClass())
            return false;
        SMSSerialization serialized = (SMSSerialization)toCompare;
        return serialized.serialize().equals(this.serialize());
    }

    /**
     * Mandatory override for toString()
     *
     * @return a string representing the state of the object
     */
    @NonNull
    public String toString(){
        return data;
    }
}
