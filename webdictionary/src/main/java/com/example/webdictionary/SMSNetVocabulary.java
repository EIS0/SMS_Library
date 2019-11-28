package com.example.webdictionary;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Edoardo Raimondi, edits by Marco Cognolato
 */
public class SMSNetVocabulary implements NetworkVocabulary<SerializableObject, SerializableObject> {

    private Map <SerializableObject, SerializableObject> NetDict;
    private String LOG_KEY = "NET_DICTIONARY";

    public SMSNetVocabulary(){
        NetDict = new HashMap<>();
    }

    /**
     * If present, it returns the given key resource, else it returns null
     * @param key having resources we want
     * @return key's resource
     */
    public SerializableObject getResource(SerializableObject key){
        try{
           return NetDict.get(key);
        }
        catch(Exception e){
            Log.i(LOG_KEY, "User not present.");
            return null;
        }
    }

    /**
     * Adds a valid Key-Resource couple to the network dictionary.
     * If already presents, change the key resource
     * @param key A key to add to the dictionary
     * @param resource A Resource to add to the dictionary
     */
    public void add(SerializableObject key, SerializableObject resource){
        if(!NetDict.containsKey(key)) NetDict.put(key, resource);
        else { //associate the new resource
            NetDict.remove(key);
            NetDict.put(key, resource); //didn't find a more elegant way
        }
    }

    /**
     * Removes a given valid Key (and its Resource) from the network dictionary
     * @param key to remove
     */
    public void remove(SerializableObject key) {
        NetDict.remove(key);
    }
}

