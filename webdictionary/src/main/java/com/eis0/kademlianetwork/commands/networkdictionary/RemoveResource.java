package com.eis0.kademlianetwork.commands.networkdictionary;

import androidx.annotation.NonNull;

import com.eis0.kademlianetwork.informationdeliverymanager.ResearchMode;
import com.eis0.kademlianetwork.informationdeliverymanager.ResourceExchangeHandler;

public class RemoveResource {

    protected final String key;
    private ResourceExchangeHandler resourceExchangeHandler;

    /**
     * @param key
     */
    public RemoveResource(@NonNull String key){
        this.key = key;
        resourceExchangeHandler = new ResourceExchangeHandler();
    }

    /**
     *Remove a node from the network dictionary
     * Search where it is. Remove it.
     * @see {@link ResourceExchangeHandler} for more details
     */
    public void execute(){
        resourceExchangeHandler.createRequest(key, null, ResearchMode.RemoveFromDictionary );
    }
}
