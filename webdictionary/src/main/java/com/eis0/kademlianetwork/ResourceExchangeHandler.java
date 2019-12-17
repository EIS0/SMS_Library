package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSHandler;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;

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
 * @author Edoardo Raimondi
 */
public class ResourceExchangeHandler {
    //Timer to know if my sent request has been taken by somebody (10 secs max)
    RespondTimer timer = new RespondTimer();
    //Maps containing the pending Requests waiting to be completed
    private Map<KademliaId, IRequest> pendingAddRequests;
    private Map<KademliaId, IRequest> pendingGetRequests;

    public ResourceExchangeHandler() {
        pendingAddRequests = new HashMap<KademliaId, IRequest>();
        pendingGetRequests = new HashMap<KademliaId, IRequest>();
    }


    /**
     * This method adds to the list of {@link Request} the new request, and send a message in the
     * network asking for a receiver, that is the node with the closest ID to the resource ID
     *
     * @param key      The String value of the key of the resource to add to the Dictionary
     * @param resource The String value of the resource itself to be added to the Dictionary
     */
    public void createAddRequest(String key, String resource) {
        //Create the Request object, insert it in the List
        Request currentRequest = new Request(key, resource);
        KademliaId idToFind = currentRequest.getKeyId();
        pendingAddRequests.put(currentRequest.getKeyId(), currentRequest);
        //Start to search for the closest ID
        SMSPeer searcher = KademliaNetwork.getInstance().getLocalNode().getPeer();
        processRequest(idToFind, searcher, ResearchMode.AddToDictionary);
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
    public void processRequest(KademliaId idToFind, SMSPeer searcher, ResearchMode researchMode) {
        if (searcher == null) throw new IllegalArgumentException();
        if (idToFind == null) throw new IllegalArgumentException();
        if (researchMode == null) throw new IllegalArgumentException();
        //If it's found, the Node with the closest id will send message containing:
        // 1. The code of the message
        // 2. the ID of the resource
        // 3. the SMSPeer of the
        IdFinderHandler.searchId(idToFind, searcher, researchMode);
    }


    /**
     * This method is called whenever the network returns the node ID closest to the resource ID
     * It search for the corresponding {@link Request} which sent the request in the network,
     * extract it from the list of pending requests, and send the <key, resource> pair contained in
     * the request to the targetPeer, that is the node which answered to the research
     *
     * @param idToFind   The {@link KademliaId} of the resource key; it's used to trace back the
     *                   Request in the pending requests list
     * @param targetPeer The {@link SMSPeer} of the node which answered the research and receive the
     *                   <key, resurce> pair
     * @throws IllegalArgumentException if the idToFind or the target peer are null
     */
    public void completeAddRequest(KademliaId idToFind, SMSPeer targetPeer) {
        if (idToFind == null || targetPeer == null) throw new IllegalArgumentException();
        //1. Find in the pendingAddRequests the Request to complete, remove it from the list
        IRequest toClose = pendingAddRequests.get(idToFind);
        pendingAddRequests.remove(idToFind);
        //Initialization of the values to send
        String key = toClose.getKey();
        String resource = toClose.getResource();
        //2. Send the <key, resource> pair
        String resourceToAdd = RequestTypes.AddToDict.ordinal() + " " + key + " " + resource;
        SMSMessage message = new SMSMessage(targetPeer, resourceToAdd);
        SMSHandler.getInstance().sendMessage(message);
    }


    /**
     *
     */
    public void createGetRequest() {
        //Create the Request object, insert it in the List

        //Start to search for the closest ID

    }

    /**
     * @return
     */
    public Map<KademliaId, IRequest> getPendingAddRequests() {
        return pendingAddRequests;
    }

    /**
     * @return
     */
    public Map<KademliaId, IRequest> getPendingGetRequests() {
        return pendingGetRequests;
    }


    /**
     * This class allows to create Request objects, which will be stored in the pendingAddRequests
     * list; every Request object contains the <key, resource> pair which will be stored in the
     * distributed dictionary, plus the {@link KademliaId} of the resource key, used to distinguish
     * each Request from the other
     */
    public class Request implements IRequest {
        private KademliaId resourceKeyId;
        private String key;
        private String resource;

        /**
         * This is the only constructor of the class, it automatically creates the ID of the
         * resource key
         *
         * @param key      The String value of the key of the <key, resource> pair
         * @param resource The String value of the resource of the <key, resource> pair
         * @throws IllegalArgumentException If the the key or the resource are null or invalid
         */
        public Request(String key, String resource) {
            if (key == null || resource == null) throw new IllegalArgumentException();
            if (key.length() == 0 || resource.length() == 0) throw new IllegalArgumentException();
            this.key = key;
            this.resource = resource;
            resourceKeyId = new KademliaId(key);
        }

        /**
         * This method returns the key of the <key, resource> pair stored in the Request
         *
         * @return The String value of the key of the <key, resource> pair
         */
        public String getKey() {
            return key;
        }

        /**
         * This method returns the key ID
         *
         * @return The {@link KademliaId} created from the key of the <key, resource> pair
         */
        public KademliaId getKeyId() {
            return resourceKeyId;
        }

        /**
         * This method returns the resource of the <key, resource> stored in the Request
         *
         * @return The String value of the resource of the <key, resource> pair
         */
        public String getResource() {
            return resource;
        }


        @Override
        public boolean equals(Object obj) {
            if (obj == null) throw new IllegalArgumentException();
            if (!(obj instanceof Request)) return false;
            Request toCompare = (Request) obj;
            return toCompare.getKeyId().toString().equals(this.getKeyId().toString());
        }

        @Override
        public String toString() {
            return this.getKeyId().toString();
        }
    }
}
