package com.eis0.kademlianetwork.commands.networkdictionary;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlianetwork.KademliaJoinableNetwork;
import com.eis0.kademlianetwork.informationdeliverymanager.IdFinderHandler;
import com.eis0.kademlianetwork.informationdeliverymanager.Request;
import com.eis0.kademlianetwork.informationdeliverymanager.ResearchMode;
import com.eis0.kademlianetwork.informationdeliverymanager.ResourceExchangeHandler;

public class SetResource {

    protected final String key;
    protected final String resource;
    private final ResourceExchangeHandler resourceExchangeHandler;

    /**
     * Set a <key, resource> pair in the network dictionary
     * @param key
     * @param resource
     */
    public SetResource(@NonNull String key, @NonNull String resource, @NonNull ResourceExchangeHandler resourceExchangeHandler){
        this.key = key;
        this.resource = resource;
        this.resourceExchangeHandler = resourceExchangeHandler;
    }

    /**
     * Search for the proper node to contain the resource. Add it.
     * @see {@link ResourceExchangeHandler} for more details
     */
    public void execute(){
        Request currentRequest = new Request(key, resource);
        KademliaId idToFind = currentRequest.getKeyId();
        resourceExchangeHandler.getPendingAddRequests().put(idToFind, currentRequest);
        //Starts to search for the closest ID
        SMSPeer searcher = KademliaJoinableNetwork.getInstance().getLocalNode().getPeer();
        IdFinderHandler.searchId(idToFind, searcher, ResearchMode.AddToDictionary);
    }
}
