package com.example.webdictionary;

import com.eis0.smslibrary.Peer;

/**
 * @author Marco Cognolato
 */
public interface Resource<P extends Peer, T> extends Serializable{
    /**
     * Returns the Resource data
     */
    T getResource();

    /**
     * Returns the Peer who has the resource
     */
    P getPeer();
}
