package com.eis0.kademlianetwork.commands.networkdictionary;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlianetwork.KademliaJoinableNetwork;
import com.eis0.kademlianetwork.informationdeliverymanager.IdFinderHandler;
import com.eis0.kademlianetwork.informationdeliverymanager.ResourceRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.ResearchMode;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;
import com.eis0.netinterfaces.commands.Command;

public class FindResource extends Command {

    protected final String key;
    private RequestsHandler requestsHandler;

    /**
     * Constructor for the FindResource command
     *
     * @param key                     The key to find in the network
     * @param requestsHandler the RequestsHandler used by this command
     */
    public FindResource(@NonNull String key, @NonNull RequestsHandler requestsHandler) {
        this.key = key;
        this.requestsHandler = requestsHandler;
    }

    /**
     * Finds a node closest to a resource from the network dictionary
     *
     * @see {@link RequestsHandler} for more details
     */
    public void execute() {
        ResourceRequest currentResourceRequest = new ResourceRequest(key, null);
        KademliaId idToFind = currentResourceRequest.getKeyId();
        requestsHandler.getPendingFindRequests().put(idToFind, currentResourceRequest);
        //Starts to search for the closest ID
        SMSPeer searcher = KademliaJoinableNetwork.getInstance().getLocalNode().getPeer();
        IdFinderHandler.searchId(idToFind, searcher, ResearchMode.FindInDictionary);
    }
}
