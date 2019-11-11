package com.example.webdictionary;

import android.util.Log;

import com.eis0.smslibrary.SMSPeer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Edoardo Raimondi
 */
public class SMSNetDictionary implements NetworkDictionary<SMSPeer,SMSResource> {

    private Map<SMSPeer, SMSResource[]> NetDict;
    private String LOG_KEY = "NET_DICTIONARY";

    public SMSNetDictionary(){
        NetDict = new HashMap<>();
    }

    /**
     * If available, it finds the first Peer that has a given valid resource, else it returns null
     * @param resource
     * @return Peer having that resource
     */
    public SMSPeer findPeerWithResource(SMSResource resource) {
        for (Map.Entry <SMSPeer, SMSResource[]> entry : NetDict.entrySet())
        {
            for(int i = 0; i <= entry.getValue().length - 1; i++) { //Scanner of a single array resource
                if ( entry.getValue()[i].equals(resource) ) return entry.getKey();
            }
        }
        return null;
    }

    /**
     * If present, it returns the list of resources of a given Peer, else it returns null
     * @param peer having resources we want
     * @return SMSResources[] containing all resources' peer
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
     * @return List of Peers currently on the network dictionary
     */
    public SMSPeer[] getAvailablePeers(){
        SMSPeer[] allAvailablePeers = new SMSPeer[NetDict.size()];
        int i = 0;
        for (Map.Entry <SMSPeer, SMSResource[]> entry : NetDict.entrySet()){
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

        for (Map.Entry <SMSPeer, SMSResource[]> entry : NetDict.entrySet()) //Dictionary scanner
        {
            int indexToAdd = entry.getValue().length;
            for(int i = 0; i <= entry.getValue().length - 1; i++) { //Single array resource scanner
                int index = i + cont;
                //check if full
                if(allAvailableResources.length == index) {
                    allAvailableResources = doubleArraySize(allAvailableResources);
                }
                allAvailableResources[index] = entry.getValue()[i];
            }
            cont += indexToAdd;
        }

        // Need 38 seconds working with 50000 elements. Is there a better way?
        if(allAvailableResources[0] != null) return allAvailableResources;
        //If the first element is null, the others will be null for sure
        return null;

    }

    /**
     * Adds a valid Peer-Resources[] couple to the network dictionary
     * @param peer A peer to add to the dictionary
     * @param resources A list of Resources to add to the dictionary
     */
    public void add(SMSPeer peer, SMSResource[] resources){
        if(!NetDict.containsKey(peer)) NetDict.put(peer, resources);

        else { //if already present, add the new resources
            SMSResource[] current = findPeerResources(peer);
            NetDict.put(peer, concatAll(current, resources));
        }
    }

    /**
     * Removes a given valid Peer (and all its Resources) from the network dictionary
     * @param peer to remove
     */
    public void remove(SMSPeer peer) {
        NetDict.remove(peer);
    }

    /**
     * Doubles array size
     * @param array to expand
     * @return Same array with double size
     */
    public SMSResource[] doubleArraySize(SMSResource[] array) {
        return java.util.Arrays.copyOf(array, array.length * 2);
    }

    /**
     *  Concatenates two arrays
     * @param array1 to concatenates with array2
     * @param array2 to concatenates with array1
     * @return Array containing all the elements of param's arrays
     */
    public SMSResource[] concatAll(SMSResource[] array1, SMSResource[] array2) {
        int totalLength = array1.length;
            totalLength += array2.length;

        SMSResource[] result = Arrays.copyOf(array1, totalLength);

        int offset = array1.length;

            System.arraycopy(array2, 0, result, offset, array2.length);
        return result;
    }
}

