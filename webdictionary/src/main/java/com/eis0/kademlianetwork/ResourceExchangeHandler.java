package com.eis0.kademlianetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handle the resource requests, it allows the user to:
 * 1. Create a request for adding a resource to the dictionary
 * 2. Rack up multiple pending requests, waiting to be triggered
 * 3. Handle the response to the request, containing the node ID closest to the resource ID that the
 * request is trying to add to the network
 * 4. Send the resource, close the pending request, remove it from the list
 *
 * TODO: it needs a refresh method that removes from the list old requests, or try to solve them
 * (for a finite amount of attempts)
 * @author Enrico Cestaro
 */
public class ResourceExchangeHandler {

    private Map<KademliaId, AddRequest> pendingRequests;
    public ResourceExchangeHandler() {
        pendingRequests = new HashMap<KademliaId, AddRequest>();
    }

    /**
     * This method adds to the list of AddRequest the new request, and send a message in the network
     * asking for receiver
     * @param
     * @param
     */
    public void createAddRequest(String key, String resource) {
        //Create the AddRequest object, insert it in the List
        //Start to search for the closest ID
        AddRequest currentRequest = new AddRequest(key, resource);
        pendingRequests.put(currentRequest.getKeyId(), currentRequest);
    }

    /**
     * This method uses an adapted method, the SearchIdForAddRequest
     */
    public void processAddRequest(KademliaId idToFind, SMSPeer searcher) {
        //If it's found, the Node with the closest id will send message containing:
        // 1. The code of the message
        // 2. the ID of the resource
        // 3. the SMSPeer of the
        IdFinderHandler.searchIdForAddRequest(idToFind, searcher);
    }

    /**
     *
     * @param idToFind
     * @param targetPeer
     */
    public void completeAddRequest(KademliaId idToFind, SMSPeer targetPeer) {
        //1. Find in the pendingRequests the AddRequest to complete, remove it from the list
        AddRequest toClose = pendingRequests.get(idToFind);
        pendingRequests.remove(idToFind);
        //Initialization values to send
        String key = toClose.getKey();
        String resource = toClose.getResource();
        //2. Send the <key, resource> pair
        String resourceToAdd = RequestTypes.AddToDict.ordinal() + " " + key + " " + resource;
        SMSMessage message = new SMSMessage(targetPeer, resourceToAdd);
        SMSManager.getInstance().sendMessage(message);
    }



    /**
     * This class allows to create AddRequest objects, which will be stored in the
     */
    private class AddRequest {
        private KademliaId resourceKeyId;
        private String key;
        private String resource;

        /**
         * Constructor
         */
        public AddRequest(String key, String resource) {
            this.key = key;
            this.resource = resource;
            resourceKeyId = new KademliaId(key);
        }

        protected String getKey() {
            return key;
        }

        protected KademliaId getKeyId() {
            return resourceKeyId;
        }

        protected String getResource() {
            return resource;
        }
    }


}
