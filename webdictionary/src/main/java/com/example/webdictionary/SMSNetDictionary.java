package com.example.webdictionary;

import android.util.Log;

import com.eis0.smslibrary.Peer;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Edoardo Raimondi
 */
public class SMSNetDictionary implements NetworkDictionary<SMSPeer, SerializableObject, SerializableObject> {


    private Map <SerializableObject, SerializableObject> NetDict;
    private String LOG_KEY = "NET_DICTIONARY";

    public SMSNetDictionary(){

        NetDict = new HashMap<>();
    }

    /**
     * If available, it finds the first Key that has a given valid resource, else it returns null
     * @param resource
     * @return Key having that resource
     */
    public SerializableObject findKeyWithResource(SerializableObject resource) {
        for (Map.Entry <SerializableObject, SerializableObject> entry : NetDict.entrySet())
        {
            if(entry.getValue().equals(resource)){
            return entry.getKey();
            }
        }
        return null;
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
     * @return List of Keys on the network dictionary
     */
    public SerializableObject[] getAvailableKeys(){
        SerializableObject[] allAvailablePeers = new SerializableObject[NetDict.size()];
        int i = 0;
        for (Map.Entry <SerializableObject, SerializableObject> entry : NetDict.entrySet()){
            //Dictionary scanner
            allAvailablePeers[i++] = entry.getKey();
        }
        return allAvailablePeers;
    }

    /**
     * @return List of resources currently on the network dictionary
     */
    public SerializableObject[] getAvailableResources(){
        SerializableObject[] allAvailableResources = new SerializableObject[1]; //array to return
        int index = 0;
        for (Map.Entry <SerializableObject, SerializableObject> entry : NetDict.entrySet()) //Dictionary scanner
        {
                allAvailableResources[index++] = entry.getValue();
            }
        // Needs 24 seconds working with 50000 elements. Is there a better way?
        return allAvailableResources;

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

