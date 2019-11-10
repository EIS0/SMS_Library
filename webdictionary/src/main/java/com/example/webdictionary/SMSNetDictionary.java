package com.example.webdictionary;

import android.util.Log;

import com.eis0.smslibrary.SMSPeer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Skeleton: Marco Cognolato
 *         Methods: Edoardo Raimondi
 */
public class SMSNetDictionary implements NetworkDictionary<SMSPeer,SMSResource> {

    private Map<SMSPeer, SMSResource[]> NetDict;
    private String LOG_KEY = "NET_DICTIONARY";

    public SMSNetDictionary(){
        NetDict = new HashMap<>();
    }

    /**
     * If available, it finds the first Peer that has a given valid resource, else returns null
     */
    public SMSPeer findPeerWithResource(SMSResource resource) {
        for (Map.Entry <SMSPeer, SMSResource[]> entry : NetDict.entrySet())
        {
            for(int i = 0; i < entry.getValue().length - 1; i++) { //Scanner of a single array resource
                if (entry.getValue()[i].equals(resource)) return entry.getKey();
            }
        }
        return null;
    }

    /**
     * If present, it returns the list of resources of a given Peer, else returns null
     */
    public SMSResource[] findPeerResources(SMSPeer peer){
        try{
           return NetDict.get(peer);
        }
        catch(Exception e){
            Log.i(LOG_KEY, "User not present.");
            return null;
        }
    }

    /**
     * Returns the list of Peers currently on the network dictionary
     */
    public SMSPeer[] getAvailablePeers(){
        SMSPeer[] allAvailablePeers = new SMSPeer[NetDict.size()];
        int i = 0;
        for (Map.Entry <SMSPeer, SMSResource[]> entry : NetDict.entrySet()){
            allAvailablePeers[i++] = entry.getKey();
        }
        if(allAvailablePeers.length != 0) return allAvailablePeers;
        return null;
    }

    /**
     * Returns the list of resources currently on the network dictionary
     */
    public SMSResource[] getAvailableResources(){
        int securityLength = 1000; //is there a better way?
        int cont = 0; //to keep the return array (allAvailableResource) absolute position
        SMSResource[] allAvailableResources = new SMSResource[securityLength]; //array to return
        for (Map.Entry <SMSPeer, SMSResource[]> entry : NetDict.entrySet())
        {
            int indexToAdd = entry.getValue().length;
            for(int i = 0; i <= entry.getValue().length - 1; i++) { //Scanner of a single array resource
                int index = i + cont;
                allAvailableResources[index] = entry.getValue()[i];
            }
            cont += indexToAdd;
        }
        // Need 38 seconds working with 50000 elements. Is there a better way?
        if(allAvailableResources.length != 0) return allAvailableResources;
        return null;
    }

    /**
     * Adds a valid Peer-Resources[] couple to the network dictionary
     * @param peer A peer to add to the dictionary
     * @param resources A list of Resources to add to the dictionary
     */
    public void add(SMSPeer peer, SMSResource[] resources){
        NetDict.put(peer, resources);
    }

    /**
     * Removes a given valid Peer (and all its Resources) from the network dictionary
     */
    public void remove(SMSPeer peer){
        NetDict.remove(peer);
    }
}
