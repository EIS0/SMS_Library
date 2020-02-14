package com.eis0.kademlianetwork.informationdeliverymanager;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.AddResourceRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.DeleteResourceRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.FindIdRequest;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.FindResourceRequest;

import java.util.ArrayList;
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
    private static Map<KademliaId, FindResourceRequest> pendingFindResourceRequests;
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
     *
     * @param idToFind The KademliaId to find, used as a key to identify a pending request
     * @return An instance of a specific FindIdRequest
     */
    public FindIdRequest startFindIdRequest(@NonNull KademliaId idToFind) {
        FindIdRequest findIdRequest = new FindIdRequest(idToFind);
        pendingFindIdRequests.put(idToFind, findIdRequest);
        return findIdRequest;
    }

    /**
     * Starts a new FindResourceRequest
     *
     * @param key The key of the resource to find, used to identify the pending request
     * @return An instance of a specific FindResourceRequest
     */
    public FindResourceRequest startFindResourceRequest(@NonNull String key) {
        FindResourceRequest resourceRequest = new FindResourceRequest(key);
        pendingFindResourceRequests.put(new KademliaId(key), resourceRequest);
        return resourceRequest;
    }

    /**
     * Starts a new FindResourceRequest
     *
     * @param key The key of the resource to find, used to identify the pending request
     * @return An instance of a specific FindResourceRequest
     */
    public AddResourceRequest startAddResourceRequest(@NonNull String key, @NonNull String resource) {
        AddResourceRequest resourceRequest = new AddResourceRequest(key, resource);
        pendingAddRequests.put(resourceRequest.getKeyId(), resourceRequest);
        return resourceRequest;
    }

    /**
     * Starts a new FindResourceRequest
     *
     * @param key The key of the resource to find, used to identify the pending request
     * @return An instance of a specific FindResourceRequest
     */
    public DeleteResourceRequest startDeleteResourceRequest(@NonNull String key) {
        DeleteResourceRequest resourceRequest = new DeleteResourceRequest(key);
        pendingDeleteRequests.put(resourceRequest.getKeyId(), resourceRequest);
        return resourceRequest;
    }

    /**
     * Sets the corresponding FindIdRequest as completed
     *
     * @param idToFind  The KademliaId to find originally
     * @param peerFound The SMSPeer found for the FindIdRequest
     */
    public void completeFindIdRequest(KademliaId idToFind, SMSPeer peerFound) {
        if (pendingFindIdRequests.containsKey(idToFind)) {
            pendingFindIdRequests.get(idToFind).setCompleted(peerFound);
            pendingFindIdRequests.remove(idToFind);
        }
    }

    /**
     * Sets the corresponding FindIdRequest as completed
     *
     * @param key           The KademliaId to find originally
     * @param resourceFound The SMSPeer found for the FindIdRequest
     */
    public void completeFindResourceRequest(String key, String resourceFound) {
        KademliaId requestId = new KademliaId(key);
        if (pendingFindResourceRequests.containsKey(requestId)) {
            pendingFindResourceRequests.get(requestId).setCompleted(resourceFound);
            pendingFindResourceRequests.remove(requestId);
        }
    }

    /**
     * Sets the corresponding AddResourceRequest as completed
     *
     * @param key The key identifier of the AddResourceRequest
     */
    public void completeAddResourceRequest(String key) {
        KademliaId requestId = new KademliaId(key);
        if (pendingAddRequests.containsKey(requestId)) {
            pendingAddRequests.get(requestId).setCompleted();
            pendingAddRequests.remove(requestId);
        }
    }

    /**
     * Sets the corresponding AddResourceRequest as completed
     *
     * @param key The key identifier of the AddResourceRequest
     */
    public void completeDeleteResourceRequest(String key) {
        KademliaId requestId = new KademliaId(key);
        if (pendingDeleteRequests.containsKey(requestId)) {
            pendingDeleteRequests.get(requestId).setCompleted();
            pendingDeleteRequests.remove(requestId);
        }
    }


}
