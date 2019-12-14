package com.eis0.kademlianetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;

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
 */
public class ResourceExchangeHandler {

    private ArrayList<AddRequest> pendingRequests = new ArrayList<AddRequest>();

    /**
     * This method adds to the list of AddRequest the new request, and send a message in the network
     * asking for receiver
     * @param
     * @param
     */
    public void createAddRequest() {
        //Create the AddRequest object, insert it in the List
        //Start to search for the closest ID

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
     * This method is a
     */
    public void answerAddRequest() {
        //Returns to the calling node the ID of the receiving node, and the ID of the resource
        //tha will be added to the Dictionary
    }

    public void comleteAddRequest() {

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
        public AddRequest() {

        }


    }


}
