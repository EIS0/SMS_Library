package com.example.webdictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Edoardo Raimondi, edits by Marco Cognolato
 */
public class SMSNetVocabulary implements NetworkVocabulary<SerializableObject, SerializableObject> {

    private Map<SerializableObject, SerializableObject> netDict = new HashMap<>();

    public SMSNetVocabulary(){ }

    /**
     * If present, it returns the given key resource, else it returns null
     * @param key The key for which we want a resource
     * @return Returns the key's resource if found, else returns null
     */
    public SerializableObject getResource(SerializableObject key){
       return netDict.get(key);
    }

    /**
     * Adds a valid Key-Resource couple to the network dictionary.
     * If already presents, updates the key-resource pair
     * @param key A key to add to the dictionary
     * @param resource A Resource to add to the dictionary
     * @throws NullPointerException If the key or resource is null
     */
    public void add(SerializableObject key, SerializableObject resource){
        if(key == null || resource == null) throw new NullPointerException();
        if(!netDict.containsKey(key)) netDict.put(key, resource);
        else {
            //update the resource since it's already present
            update(key, resource);
        }
    }

    /**
     * Removes a given valid Key (and its Resource) from the network dictionary
     * @param key to remove
     * @throws NullPointerException If key is null
     */
    public void remove(SerializableObject key) {
        if(key == null) throw new NullPointerException();
        netDict.remove(key);
    }

    /**
     * Updates a resource associated with a key
     * @param key The key associated with the Resource to update
     * @param resource The Resource to update
     * @throws NullPointerException If key or resource is null
     * @author Marco Cognolato
     */
    public void update(SerializableObject key, SerializableObject resource){
        netDict.remove(key);
        netDict.put(key, resource);
    }
}

