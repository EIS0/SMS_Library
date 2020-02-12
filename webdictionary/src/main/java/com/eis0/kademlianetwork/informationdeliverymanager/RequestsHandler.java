package com.eis0.kademlianetwork.informationdeliverymanager;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handle the resource {@link DeleteResourceRequest}s, it allows the user to:
 * 1. Create a request to process a resource inside the dictionary
 * 2. Rack up multiple pending requests, waiting to be triggered
 * 3. Handle the response to the request, which contains the node ID closest to the resource ID that
 * the request is trying to process
 * 4. Process the resource, closing the pending request and removing it from the list
 * => Resources can be added, removed, or obtained from the Dictionary
 *
 * @author Enrico Cestaro
 * @author Edoardo Raimondi
 * @author Marco Cognolato
 */
public class RequestsHandler {

    //Maps containing the pending Requests waiting to be completed; each pending request is identified
    //inside the Map by the request ID that the request must process
    private static Map<KademliaId, AddResourceRequest> pendingAddRequests;
    private static Map<String, FindResourceRequest> pendingFindResourceRequests;
    private static Map<KademliaId, DeleteResourceRequest> pendingDeleteRequests;
    private static Map<KademliaId, FindIdRequest> pendingFindIdRequests;

    /**
     * This constructor initializes the three pending requests lists
     */
    public RequestsHandler() {
        pendingAddRequests = new HashMap<>();
        pendingFindResourceRequests = new HashMap<>();
        pendingDeleteRequests = new HashMap<>();
        pendingFindIdRequests = new HashMap<>();
    }

    /**
     * Starts a new FindIdRequest
     * @param idToFind The KademliaId to find, used as a key to identify a pending request
     * @return An instance of a specific FindIdRequest
     */
    public FindIdRequest startFindIdRequest(@NonNull KademliaId idToFind){
        FindIdRequest newRequest = new FindIdRequest(idToFind);
        pendingFindIdRequests.put(idToFind, newRequest);
        return newRequest;
    }

    /**
     * Starts a new FindResourceRequest
     * @param key The key of the resource to find, used to identify the pending request
     * @return An instance of a specific FindResourceRequest
     */
    public FindResourceRequest startFindResourceRequest(@NonNull String key){
        FindResourceRequest resourceRequest = new FindResourceRequest(key);
        pendingFindResourceRequests.put(key, resourceRequest);
        return resourceRequest;
    }

    /**
     * Starts a new FindResourceRequest
     * @param key The key of the resource to find, used to identify the pending request
     * @return An instance of a specific FindResourceRequest
     */
    public AddResourceRequest startAddResourceRequest(@NonNull String key, @NonNull String resource){
        AddResourceRequest resourceRequest = new AddResourceRequest(key, resource);
        pendingAddRequests.put(resourceRequest.getKeyId(), resourceRequest);
        return resourceRequest;
    }

    /**
     * Starts a new FindResourceRequest
     * @param key The key of the resource to find, used to identify the pending request
     * @return An instance of a specific FindResourceRequest
     */
    public DeleteResourceRequest startDeleteResourceRequest(@NonNull String key){
        DeleteResourceRequest resourceRequest = new DeleteResourceRequest(key);
        pendingDeleteRequests.put(resourceRequest.getKeyId(), resourceRequest);
        return resourceRequest;
    }

    /**
     * Sets the corresponding FindIdRequest as completed
     *
     * @param peerFound The SMSPeer found for the FindIdRequest
     */
    public void completeFindIdRequest(SMSPeer peerFound){
        SMSKademliaNode node = new SMSKademliaNode(peerFound);
        KademliaId idFound = node.getId();
        if(pendingFindIdRequests.containsKey(idFound)){
            pendingFindIdRequests.get(idFound).setCompleted(peerFound);
        }
    }

    /**
     * Sets the corresponding FindIdRequest as completed
     *
     * @param key The KademliaId to find originally
     * @param resourceFound The SMSPeer found for the FindIdRequest
     */
    public void completeFindResourceRequest(String key, String resourceFound){
        if(pendingFindResourceRequests.containsKey(key)){
            pendingFindResourceRequests.get(key).setCompleted(resourceFound);
        }
    }

    /**
     * Sets the corresponding AddResourceRequest as completed
     *
     * @param key The key identifier of the AddResourceRequest
     */
    public void completeAddResourceRequest(String key){
        KademliaId requestId = new KademliaId(key);
        if(pendingAddRequests.containsKey(requestId)){
            pendingAddRequests.get(requestId).setCompleted();
        }
    }

    /**
     * Sets the corresponding AddResourceRequest as completed
     *
     * @param key The key identifier of the AddResourceRequest
     */
    public void completeDeleteResourceRequest(String key){
        KademliaId requestId = new KademliaId(key);
        if(pendingDeleteRequests.containsKey(requestId)){
            pendingDeleteRequests.get(requestId).setCompleted();
        }
    }
}

/*
When a resource needs to be added to the network dictionary:
1. A DeleteResourceRequest is created, it contains the <key, resource> pair
2. The DeleteResourceRequest is added to the pendingAddRequests list, and is processed
3. Processing the DeleteResourceRequest means that:
    - The node which will contain the resource must be found, a message identified by the code
    FindIdForAddRequest is sent 'inside the network'
    (createAddRequest >> processRequest >> idFinderHandler >> 'Network / TargetNode')
    - The node which will contain the resource respond directly to the local node, with a message
    identified by the code ResultAddRequest and the ID of the Resource which needs to be added to
    the Dictionary
    ('Network / TargetNode' >> SMSKademliaListener >> completeAddRequest)
4. When the local node receives the result of the research, it searches inside the list of pending
    AddRequests, find the corresponding one, and complete the DeleteResourceRequest sending to
    the node which will store it the resource, inside a message identified by the code AddToDict
5. The final node will receive the message, recognize it (with the SMSKademliaListener) as a request
    to store a resource, and do so


=> This process is repeated almost identically to delete or get a resource from the network dictionary
 */