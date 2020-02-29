package com.eis0.kademlianetwork.informationdeliverymanager.Requests;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;

/**
 * This class allows to create DeleteResourceRequest objects, which will be stored in the pending requests
 * lists; every DeleteResourceRequest object contains the <key, resource> pair which will be stored in the
 * distributed dictionary, plus the {@link KademliaId} of the resource key, used to distinguish
 * each DeleteResourceRequest from the other
 *
 * @author Enrico Cestaro
 * @author Marco Cognolato
 */
public class FindIdRequest {

    private boolean hasBeenFulfilled = false;
    private KademliaId idToFind;

    private SMSPeer peerFound;

    /**
     * This is the constructor of the class, it automatically creates the ID of the
     * resource key
     *
     * @param idToFind The KademliaId corresponding to the id to find
     * @throws IllegalArgumentException If the the key or the resource are null or invalid
     */
    public FindIdRequest(@NonNull KademliaId idToFind) {
        this.idToFind = idToFind;
    }

    /**
     * Sets the FindIdRequest as completed
     *
     * @param peer The SMSPeer found during the FindIdRequest
     */
    public void setCompleted(SMSPeer peer) {
        this.peerFound = peer;
        hasBeenFulfilled = true;
    }

    /**
     * @return Returns true if this FindIdRequest has been completed
     */
    public boolean isCompleted() {
        return hasBeenFulfilled;
    }

    /**
     * @return Returns the SMSPeer found by the FindIdRequest. Null if no peer has been found.
     */
    public SMSPeer getPeerFound() {
        return peerFound;
    }

    /**
     * Compares this object to another
     * @param obj The object to compare to this
     * @return Returns true if the object are equal or are the same, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof FindIdRequest)) return false;
        FindIdRequest toCompare = (FindIdRequest) obj;
        return toCompare.idToFind.equals(this.idToFind);
    }

    /**
     * @return Returns the hashcode of this object
     */
    @Override
    public int hashCode() {
        return idToFind.hashCode();
    }
}