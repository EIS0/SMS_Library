package com.eis0.kademlianetwork.commands.networkdictionary;

import androidx.annotation.NonNull;

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
        resourceExchangeHandler.createRequest(key, resource, ResearchMode.AddToDictionary);
    }

}
