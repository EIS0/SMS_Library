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
 * <p>
 * TODO: it needs a refresh method that removes from the list old requests, or try to solve them
 * (for a finite amount of attempts)
 * <p>
 * TODO: right now, key and resource are separated be a space, BUT this means that to visualize
 * the entire resource, it must be written without spaces
 * => Resource_to_visualize
 * MUST BE FIXED
 *
 * @author Enrico Cestaro
 */
public class ResourceExchangeHandler {
    //Map containing the pending AddRequests waiting to be completed
    private Map<KademliaId, AddRequest> pendingRequests;

    /**
     * TODO: the list of AddRequests is reinitialized every time the node disconnect itself from the
     *  network, is this something to fix?
     * Constructor of the ResourceExchangeHandler
     */
    public ResourceExchangeHandler() {
        pendingRequests = new HashMap<KademliaId, AddRequest>();
    }


    /**
     * This method adds to the list of {@link AddRequest} the new request, and send a message in the
     * network asking for a receiver, that is the node with the closest ID to the resource ID
     *
     * @param key      The String value of the key of the resource to add to the Dictionary
     * @param resource The String value of the resource itself to be added to the Dictionary
     * @throws IllegalArgumentException If the the key or the resource are null or invalid
     */
    public void createAddRequest(String key, String resource) {
        if (key == null || resource == null) throw new IllegalArgumentException();
        if (key.length() == 0 || resource.length() == 0) throw new IllegalArgumentException();
        //Create the AddRequest object, insert it in the List
        AddRequest currentRequest = new AddRequest(key, resource);
        KademliaId idToFind = currentRequest.getKeyId();
        pendingRequests.put(currentRequest.getKeyId(), currentRequest);
        //Start to search for the closest ID
        SMSPeer searcher = KademliaNetwork.getInstance().getLocalNode().getPeer();
        processAddRequest(idToFind, searcher);
    }


    /**
     * This method uses an adapted method, the SearchIdForAddRequest in the {@link IdFinderHandler}
     * class
     *
     * @param idToFind The {@link KademliaId} of the resource key, sent in the network asking for
     *                 the node with the closest node ID
     * @param searcher The {@link SMSPeer} of the node which started the research, it's sent in the
     *                 network to allow the final receiver to send the answer right to the initial node
     * @throws IllegalArgumentException If the idToFind or the searcher are null
     */
    public void processAddRequest(KademliaId idToFind, SMSPeer searcher) {
        if (searcher == null) throw new IllegalArgumentException();
        if (idToFind == null) throw new IllegalArgumentException();
        //If it's found, the Node with the closest id will send message containing:
        // 1. The code of the message
        // 2. the ID of the resource
        // 3. the SMSPeer of the
        IdFinderHandler.searchIdForAddRequest(idToFind, searcher);
    }


    /**
     * This method is called whenever the network returns the node ID closest to the resource ID
     * It search for the corresponding {@link AddRequest} which sent the request in the network,
     * extract it from the list of pending requests, and send the <key, resource> pair contained in
     * the request to the targetPeer, that is the node which answered to the research
     *
     * @param idToFind   The {@link KademliaId} of the resource key; it's used to trace back the
     *                   AddRequest in the pending requests list
     * @param targetPeer The {@link SMSPeer} of the node which answered the research and receive the
     *                   <key, resurce> pair
     * @throws IllegalArgumentException if the idToFind or the target peer are null
     */
    public void completeAddRequest(KademliaId idToFind, SMSPeer targetPeer) {
        if (idToFind == null || targetPeer == null) throw new IllegalArgumentException();
        //1. Find in the pendingRequests the AddRequest to complete, remove it from the list
        AddRequest toClose = pendingRequests.get(idToFind);
        pendingRequests.remove(idToFind);
        //Initialization of the values to send
        String key = toClose.getKey();
        String resource = toClose.getResource();
        //2. Send the <key, resource> pair
        String resourceToAdd = RequestTypes.AddToDict.ordinal() + " " + key + " " + resource;
        SMSMessage message = new SMSMessage(targetPeer, resourceToAdd);
        SMSManager.getInstance().sendMessage(message);
    }


    /**
     * This class allows to create AddRequest objects, which will be stored in the pendingRequests
     * list; every AddRequest object contains the <key, resource> pair which will be stored in the
     * distributed dictionary, plus the {@link KademliaId} of the resource key, used to distinguish
     * each AddRequest from the other
     */
    private class AddRequest {
        private KademliaId resourceKeyId;
        private String key;
        private String resource;

        /**
         * This is the only constructor of the class, it automatically creates the ID of the
         * resource key
         *
         * @param key
         * @param resource
         */
        public AddRequest(String key, String resource) {
            this.key = key;
            this.resource = resource;
            resourceKeyId = new KademliaId(key);
        }


        /**
         * This method returns the key of the <key, resource> pair stored in the AddRequest
         *
         * @return The String value of the key of the <key, resource> pair
         */
        protected String getKey() {
            return key;
        }


        /**
         * This method returns the key ID
         *
         * @return The {@link KademliaId} created from the key of the <key, resource> pair
         */
        protected KademliaId getKeyId() {
            return resourceKeyId;
        }


        /**
         * This method returns the resource of the <key, resource> stored in the AddRequest
         *
         * @return The String value of the resource of the <key, resource> pair
         */
        protected String getResource() {
            return resource;
        }
    }


}
