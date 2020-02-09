package com.eis0.webdictionary;

import com.eis0.netinterfaces.NetDictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Edoardo Raimondi, edits by Marco Cognolato
 */
public class SMSNetVocabulary implements NetDictionary<String, String> {

    private final Map<String, String> netDict = new HashMap<>();

    public SMSNetVocabulary(){ }

    /**
     * If present, it returns the given key resource, else it returns null
     * @param key The key for which we want a resource
     * @return Returns the key's resource if found, else returns null
     */
    public String getResource(String key){
       return netDict.get(key);
    }

    /**
     * Adds a valid Key-Resource couple to the network dictionary.
     * If already presents, updates the key-resource pair
     * @param key A key to add to the dictionary
     * @param resource A Resource to add to the dictionary
     * @throws NullPointerException If the key or resource is null
     */
    public void addResource(String key, String resource){
        if(key == null || resource == null)
            throw new NullPointerException("Cannot add null keys/resources");
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
    public void removeResource(String key) {
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
    public void update(String key, String resource){
        netDict.remove(key);
        netDict.put(key, resource);
    }

    /**
     * Removes all keys and resources from the dictionary.
     */
    public void clear(){
        netDict.clear();
    }
}

