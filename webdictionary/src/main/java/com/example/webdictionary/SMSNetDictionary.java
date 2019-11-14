package com.example.webdictionary;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Edoardo Raimondi
 */
public class SMSNetDictionary implements NetworkDictionary<SMSKey,SMSResource> {

    private Map <SMSKey, SMSResource[]> NetDict;
    private String LOG_KEY = "NET_DICTIONARY";

    public SMSNetDictionary(){
        NetDict = new HashMap<>();
    }

    /**
     * If available, it finds the first Peer that has a given valid resource, else it returns null
     * @param resource
     * @return key having that resource
     */
    public SMSKey findKeyWithResource(SMSResource resource) {
        for (Map.Entry <SMSKey, SMSResource[]> entry : NetDict.entrySet())
        {
            for(int i = 0; i <= entry.getValue().length - 1; i++) { //Scanner of a single array resource
                if ( entry.getValue()[i].equals(resource) ) return entry.getKey();
            }
        }
        return null;
    }

    /**
     * If present, it returns the list of resources of a given Peer, else it returns null
     * @param key having resources we want
     * @return SMSResources[] containing all resources' peer
     */
    public SMSResource[] findPeerResources(SMSKey key){
        try{
           return NetDict.get(key);
        }
        catch(Exception e){
            Log.i(LOG_KEY, "User not present.");
            return null;
        }
    }

    /**
     * @return List of Peers currently on the network dictionary
     */
    public SMSKey[] getAvailableKeys(){
        SMSKey[] allAvailablePeers = new SMSKey[NetDict.size()];
        int i = 0;
        for (Map.Entry <SMSKey, SMSResource[]> entry : NetDict.entrySet()){
            //Dictionary scanner
            allAvailablePeers[i++] = entry.getKey();
        }
        return allAvailablePeers;
    }

    /**
     * @return List of resources currently on the network dictionary
     */
    public SMSResource[] getAvailableResources(){
        int cont = 0; //to keep the return array (allAvailableResource) absolute position
        SMSResource[] allAvailableResources = new SMSResource[1]; //array to return

        for (Map.Entry <SMSKey, SMSResource[]> entry : NetDict.entrySet()) //Dictionary scanner
        {
            int indexToAdd = entry.getValue().length;
            for(int i = 0; i <= entry.getValue().length - 1; i++) { //Single array resource scanner
                int index = i + cont;
                //check if full
                if(allAvailableResources.length == index) {
                    allAvailableResources = SMSNetDictionarySupport.doubleArraySize(allAvailableResources);
                }
                allAvailableResources[index] = entry.getValue()[i];
            }
            cont += indexToAdd;
        }

        // Need 38 seconds working with 50000 elements. Is there a better way?
        return allAvailableResources;

    }


    /**
     * Adds a valid Peer-Resources[] couple to the network dictionary
     * @param key A peer to add to the dictionary
     * @param resources A list of Resources to add to the dictionary
     */
    public void add(SMSKey key, SMSResource[] resources){
        if(!NetDict.containsKey(key)){
            NetDict.put(key, resources);
        }
        else { //if already present, add the new resources
            SMSResource[] current = findPeerResources(key);
            NetDict.put(key, SMSNetDictionarySupport.concatAll(current, resources));
        }
    }

    /**
     * Removes a given valid Peer (and all its Resources) from the network dictionary
     * @param key to remove
     */
    public void remove(SMSKey key) {
        NetDict.remove(key);
    }

}

