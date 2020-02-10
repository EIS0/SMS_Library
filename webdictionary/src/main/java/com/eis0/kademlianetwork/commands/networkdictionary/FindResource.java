package com.eis0.kademlianetwork.commands.networkdictionary;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlianetwork.KademliaJoinableNetwork;
import com.eis0.kademlianetwork.informationdeliverymanager.IdFinderHandler;
import com.eis0.kademlianetwork.informationdeliverymanager.Request;
import com.eis0.kademlianetwork.informationdeliverymanager.ResearchMode;
import com.eis0.kademlianetwork.informationdeliverymanager.ResourceExchangeHandler;

public class FindResource {

    protected final String key;
    private ResourceExchangeHandler resourceExchangeHandler;

    /**
     * @param key
     */
    public FindResource(@NonNull String key){
        this.key = key;
        resourceExchangeHandler = new ResourceExchangeHandler();
    }

    /**
     *Remove a node from the network dictionary
     * Search where it is. Remove it.
     * @see {@link ResourceExchangeHandler} for more details
     */
    public void execute(){
        //resourceExchangeHandler.createRequest(key, null, ResearchMode.RemoveFromDictionary );
        Request currentRequest = new Request(key, null);
        KademliaId idToFind = currentRequest.getKeyId();
        resourceExchangeHandler.getPendingFindRequests().put(idToFind, currentRequest);
        //Starts to search for the closest ID
        SMSPeer searcher = KademliaJoinableNetwork.getInstance().getLocalNode().getPeer();
        IdFinderHandler.searchId(idToFind, searcher, ResearchMode.FindInDictionary);
    }
}
