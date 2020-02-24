package com.eis0.kademlianetwork.informationdeliverymanager.Requests;

import androidx.annotation.NonNull;

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
public class FindResourceRequest {
    private KademliaId resourceKeyId;
    private String key;
    private String resource;

    private boolean hasBeenFulfilled = false;

    /**
     * This is the constructor of the class, it automatically creates the ID of the
     * resource key
     *
     * @param key The String value of the key of the <key, resource> pair
     * @throws IllegalArgumentException If the the key or the resource are null or invalid
     */
    public FindResourceRequest(@NonNull String key) {
        this.key = key;
    }

    /**
     * This method returns the key of the <key, resource> pair stored in the DeleteResourceRequest
     *
     * @return The String value of the key of the <key, resource> pair
     */
    public String getKey() {
        return key;
    }

    /**
     * This method returns the resource of the <key, resource> stored in the DeleteResourceRequest
     *
     * @return The String value of the resource of the <key, resource> pair
     */
    public String getResource() {
        return resource;
    }


    /**
     * Sets the FindResourceRequest as completed
     *
     * @param resource The resource found during the FindResourceRequest
     */
    public void setCompleted(@NonNull String resource) {
        this.resource = resource;
        hasBeenFulfilled = true;
    }

    /**
     * This method returns the key ID
     *
     * @return The {@link KademliaId} created from the key of the <key, resource> pair
     */
    public KademliaId getKeyId() {
        return new KademliaId(key);
    }

    /**
     * @return Returns true if this FindResourceRequest has been completed
     */
    public boolean isCompleted() {
        return hasBeenFulfilled;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
        if (!(obj instanceof FindResourceRequest)) return false;
        FindResourceRequest toCompare = (FindResourceRequest) obj;
        return toCompare.getKeyId().equals(this.getKeyId());
    }
}