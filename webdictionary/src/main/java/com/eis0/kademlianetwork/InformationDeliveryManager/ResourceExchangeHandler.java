package com.eis0.kademlianetwork.InformationDeliveryManager;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlianetwork.KademliaNetwork;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handle the resource {@link Request}s, it allows the user to:
 * 1. Create a request to process a resource inside the dictionary
 * 2. Rack up multiple pending requests, waiting to be triggered
 * 3. Handle the response to the request, which contains the node ID closest to the resource ID that
 * the request is trying to process
 * 4. Process the resource, closing the pending request and removing it from the list
 * => Resources can be added, removed, or obtained from the Dictionary
 *
 * @author Enrico Cestaro
 * @author Edoardo Raimondi
 */
public class ResourceExchangeHandler {

    private static final String ID_TO_FIND_NULL = "The idToFind parameter is null";
    private static final String SEARCHER_NULL = "The searcher parameter is null";
    private static final String RESEARCH_MODE_NULL = "The researchMode parameter is null";
    private static final String KEY_NULL = "The key parameter is null";
    private static final String INVALID_KEY_LENGTH = "The key must contain at least 1 character";
    private static final String TARGET_PEER_NULL = "The targetPeer parameter is null";


    //Maps containing the pending Requests waiting to be completed; each pending request is identified
    //inside the Map by the request ID that the request must process
    private Map<KademliaId, Request> pendingAddRequests;
    private Map<KademliaId, Request> pendingGetRequests;
    private Map<KademliaId, Request> pendingDeleteRequests;

    /**
     * This constructor initializes the three pending requests lists
     */
    public ResourceExchangeHandler() {
        pendingAddRequests = new HashMap<>();
        pendingGetRequests = new HashMap<>();
        pendingDeleteRequests = new HashMap<>();
    }

    /**
     * This method adds to the most suitable pending Requests list the new {@link Request}, and sends
     * a message in the network asking for a receiver, that is the node with the node ID closest to
     * the resource ID
     *
     * @param key          The String value of the key of the resource to add to the Dictionary
     * @param resource     The String value of the resource itself to be added to the Dictionary
     * @param researchMode The research mode, represents the final purpose of the research
     * @throws IllegalArgumentException If the key is null
     */
    public void createRequest(String key, String resource, ResearchMode researchMode) {
        if (key == null) throw new IllegalArgumentException(KEY_NULL);
        Request currentRequest;
        KademliaId idToFind = null;
        switch (researchMode) {
            case AddToDictionary:
                currentRequest = new Request(key, resource);
                idToFind = currentRequest.getKeyId();
                pendingAddRequests.put(idToFind, currentRequest);
                break;
            case FindInDictionary:
                currentRequest = new Request(key, null);
                idToFind = currentRequest.getKeyId();
                pendingGetRequests.put(idToFind, currentRequest);
                break;
            case RemoveFromDictionary:
                currentRequest = new Request(key, null);
                idToFind = currentRequest.getKeyId();
                pendingDeleteRequests.put(idToFind, currentRequest);
                break;
        }
        //Starts to search for the closest ID
        SMSPeer searcher = KademliaNetwork.getInstance().getLocalNode().getPeer();
        processRequest(idToFind, searcher, ResearchMode.AddToDictionary);
    }


    /**
     * This method starts the ID research inside the network, according to the type of research the
     * request is asking for
     *
     * @param idToFind     The {@link KademliaId} of the resource key, sent in the network asking for
     *                     the node with the closest node ID
     * @param searcher     The {@link SMSPeer} of the node which started the research, it's sent in the
     *                     network to allow the final receiver to send the answer right to the initial node
     * @param researchMode The research mode, represents the final purpose of the research
     * @throws IllegalArgumentException If the idToFind or the searcher are null
     */
    public void processRequest(KademliaId idToFind, SMSPeer searcher, ResearchMode researchMode) {
        if (idToFind == null) throw new IllegalArgumentException(ID_TO_FIND_NULL);
        if (searcher == null) throw new IllegalArgumentException(SEARCHER_NULL);
        if (researchMode == null) throw new IllegalArgumentException(RESEARCH_MODE_NULL);

        //If it's found, the Node with the closest id will send message containing:
        // 1. The code of the message
        // 2. the ID of the resource
        // 3. the SMSPeer of the
        IdFinderHandler.searchId(idToFind, searcher, researchMode);
    }


    /**
     * This method is called whenever the network returns the node ID closest to the resource ID.
     * It searches for the corresponding {@link Request} which sent the ID research in the network,
     * extracts it from the list of pending requests, and sends the <key, resource> pair contained in
     * the request to the targetPeer, that is the node which answered to the research.
     * The {@link KademliaId} must correspond to an existing opened Request for the method to work.
     *
     * @param idToFind     The {@link KademliaId} of the resource key; it's used to trace back the
     *                     Request in the pending requests list
     * @param targetPeer   The {@link SMSPeer} of the node which answered the research and receive the
     *                     <key, resurce> pair
     * @param researchMode The research mode, represents the final purpose of the research
     * @throws IllegalArgumentException If the idToFind or the targetPeer are null
     */
    public void completeRequest(KademliaId idToFind, SMSPeer targetPeer, ResearchMode researchMode) {
        if (idToFind == null) throw new IllegalArgumentException(ID_TO_FIND_NULL);
        if (targetPeer == null) throw new IllegalArgumentException(TARGET_PEER_NULL);

        Map<KademliaId, Request> pendingRequests = null;
        Request aboutToClose = null;
        RequestTypes requestType = null;

        //1. Verify what kind of request must be completed
        switch (researchMode) {
            case AddToDictionary:
                pendingRequests = pendingAddRequests;
                requestType = RequestTypes.AddToDict;
                aboutToClose = pendingRequests.get(idToFind);
                break;
            case FindInDictionary:
                pendingRequests = pendingGetRequests;
                requestType = RequestTypes.GetFromDict;
                aboutToClose = pendingRequests.get(idToFind);
                break;
            case RemoveFromDictionary:
                pendingRequests = pendingDeleteRequests;
                requestType = RequestTypes.RemoveFromDict;
                aboutToClose = pendingRequests.get(idToFind);
                break;
        }
        //Remove the request from the corresponding list
        pendingRequests.remove(idToFind);
        //Extract the <key, resource> pair contained by the Request; the resource value can be null
        String key = aboutToClose.getKey();
        String resource = aboutToClose.getResource();
        //2. Send the message
        KademliaMessage kadMessage = new KademliaMessage(requestType, null, null, key, resource);
        SMSMessage message = new SMSMessage(targetPeer, kadMessage.toString());
        SMSManager.getInstance().sendMessage(message);
    }


    /**
     * This method returns the pendingAddRequests object of the class
     *
     * @return The pendingAddRequests map of the ResourceExchangeHandler class
     */
    public Map<KademliaId, Request> getPendingAddRequests() {
        return pendingAddRequests;
    }

    /**
     * This method returns the pendingGetRequests object of the class
     *
     * @return The pendingGetRequests map of the ResourceExchangeHandler class
     */
    public Map<KademliaId, Request> getPendingGetRequests() {
        return pendingGetRequests;
    }

    /**
     * This method returns the pendingGetRequests object of the class
     *
     * @return The pendingGetRequests map of the ResourceExchangeHandler class
     */
    public Map<KademliaId, Request> getPendingDeleteRequests() {
        return pendingDeleteRequests;
    }


    /**
     * This class allows to create Request objects, which will be stored in the pending requests
     * lists; every Request object contains the <key, resource> pair which will be stored in the
     * distributed dictionary, plus the {@link KademliaId} of the resource key, used to distinguish
     * each Request from the other
     */
    public class Request implements IRequest {
        private KademliaId resourceKeyId;
        private String key;
        private String resource;

        /**
         * This is the constructor of the class, it automatically creates the ID of the
         * resource key
         *
         * @param key      The String value of the key of the <key, resource> pair
         * @param resource The String value of the resource of the <key, resource> pair
         * @throws IllegalArgumentException If the the key or the resource are null or invalid
         */
        public Request(String key, String resource) {
            if (key == null) throw new IllegalArgumentException(KEY_NULL);
            if (key.length() == 0) throw new IllegalArgumentException(INVALID_KEY_LENGTH);
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
    }
}

/*
When a resource needs to be added to the network dictionary:
1. A Request is created, it contains the <key, resource> pair
2. The Request is added to the pendingAddRequests list, and is processed
3. Processing the Request means that:
    - The node which will contain the resource must be found, a message identified by the code
    FindIdForAddRequest is sent 'inside the network'
    (createAddRequest >> processRequest >> idFinderHandler >> 'Network / TargetNode')
    - The node which will contain the resource respond directly to the local node, with a message
    identified by the code ResultAddRequest and the ID of the Resource which needs to be added to
    the Dictionary
    ('Network / TargetNode' >> SMSKademliaListener >> completeAddRequest)
4. When the local node receives the result of the research, it searches inside the list of pending
    AddRequests, find the corresponding one, and complete the Request sending to
    the node which will store it the resource, inside a message identified by the code AddToDict
5. The final node will receive the message, recognize it (with the SMSKademliaListener) as a request
    to store a resource, and do so


=> This process is repeated almost identically to delete or get a resource from the network dictionary
 */