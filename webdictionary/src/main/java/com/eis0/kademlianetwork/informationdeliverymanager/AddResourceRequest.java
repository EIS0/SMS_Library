package com.eis0.kademlianetwork.informationdeliverymanager;

import com.eis0.kademlia.KademliaId;

/**
 * This class allows to create DeleteResourceRequest objects, which will be stored in the pending requests
 * lists; every DeleteResourceRequest object contains the <key, resource> pair which will be stored in the
 * distributed dictionary, plus the {@link KademliaId} of the resource key, used to distinguish
 * each DeleteResourceRequest from the other
 *
 * @author Enrico Cestaro
 */
public class AddResourceRequest {
    private KademliaId resourceKeyId;
    private String key;
    private String resource;

    private static final String KEY_NULL = "The key parameter is null";
    private static final String INVALID_KEY_LENGTH = "The key must contain at least 1 character";

    private boolean hasBeenFulfilled = false;

    /**
     * This is the constructor of the class, it automatically creates the ID of the
     * resource key
     *
     * @param key      The String value of the key of the <key, resource> pair
     * @param resource The String value of the resource of the <key, resource> pair
     * @throws IllegalArgumentException If the the key or the resource are null or invalid
     */
    public AddResourceRequest(String key, String resource) {
        if (key == null) throw new IllegalArgumentException(KEY_NULL);
        if (key.length() == 0) throw new IllegalArgumentException(INVALID_KEY_LENGTH);
        this.key = key;
        this.resource = resource;
        this.resourceKeyId = new KademliaId(key);
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
     * This method returns the key ID
     *
     * @return The {@link KademliaId} created from the key of the <key, resource> pair
     */
    public KademliaId getKeyId() {
        return resourceKeyId;
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
     * Sets the Request as completed
     */
    public void setCompleted(){
        hasBeenFulfilled = true;
    }

    /**
     * @return Returns true if the Request has been completed, false otherwise
     */
    public boolean isCompleted(){
        return hasBeenFulfilled;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
        if (!(obj instanceof AddResourceRequest)) return false;
        AddResourceRequest toCompare = (AddResourceRequest) obj;
        return toCompare.getKeyId().toString().equals(this.getKeyId().toString());
    }
}