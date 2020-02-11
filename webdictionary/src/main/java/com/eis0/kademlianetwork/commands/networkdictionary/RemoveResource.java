package com.eis0.kademlianetwork.commands.networkdictionary;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlianetwork.KademliaJoinableNetwork;
import com.eis0.kademlianetwork.informationdeliverymanager.IdFinderHandler;
import com.eis0.kademlianetwork.informationdeliverymanager.Request;
import com.eis0.kademlianetwork.informationdeliverymanager.ResearchMode;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;
import com.eis0.netinterfaces.commands.Command;

public class RemoveResource extends Command {

    protected final String key;
    private RequestsHandler requestsHandler;

    /**
     * Constructor for the RemoveResource command
     *
     * @param key                     The key to Remove from the network
     * @param requestsHandler The RequestsHandler used by the command
     */
    public RemoveResource(@NonNull String key, @NonNull RequestsHandler requestsHandler) {
        this.key = key;
        this.requestsHandler = requestsHandler;
    }

    /**
     * Removes a node from the network dictionary
     * Search where it is. Remove it.
     *
     * @see {@link RequestsHandler} for more details
     */
    public void execute() {
        Request currentRequest = new Request(key, null);
        KademliaId idToFind = currentRequest.getKeyId();
        requestsHandler.getPendingDeleteRequests().put(idToFind, currentRequest);
        //Starts to search for the closest ID
        SMSPeer searcher = KademliaJoinableNetwork.getInstance().getLocalNode().getPeer();
        IdFinderHandler.searchId(idToFind, searcher, ResearchMode.RemoveFromDictionary);
    }
}
