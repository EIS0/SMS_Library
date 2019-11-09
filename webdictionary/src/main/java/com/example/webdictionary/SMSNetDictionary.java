package com.example.webdictionary;

import com.eis0.smslibrary.SMSPeer;

/**
 * @author Marco Cognolato
 */
public class SMSNetDictionary implements NetworkDictionary<SMSPeer,SMSResource> {
    /**
     * Finds the Peer that has a given valid resource, if available, else returns null
     */
    public SMSPeer findPeerWithResource(SMSResource resource){
        return null;
    }

    /**
     * Returns the list of resources of a given peer if present, else returns null
     */
    public SMSResource[] findPeerResources(SMSPeer peer){
        return null;
    }

    /**
     * Returns the list of Peers currently on the network dictionary
     */
    public SMSPeer[] getAvailablePeers(){
        return null;
    }

    /**
     * Returns the list of resources currently on the network dictionary
     */
    public SMSResource[] getAvailableResources(){
        return null;
    }

    /**
     * Adds a valid Peer-Resources[] couple to the network dictionary
     * @param peer A peer to add to the dictionary
     * @param resources A list of Resources to add to the dictionary
     */
    public void add(SMSPeer peer, SMSResource[] resources){

    }

    /**
     * Removes a given valid Peer (and all its Resources) from the network dictionary
     */
    public void remove(SMSPeer peer){

    }
}
