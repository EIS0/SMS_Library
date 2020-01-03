package com.eis0.kademlianetwork.InformationDeliveryManager;

import com.eis0.kademlia.KademliaId;

/**
 * Interface for the pending resource requests that can be used by the {@link ResourceExchangeHandler}
 * to add, ask or delete resources from the dictionary
 * Interface of an internal class, private, not testable
 */
public interface IRequest {
    String getKey();
    KademliaId getKeyId();
    String getResource();
    String toString();
    boolean equals(Object obj);
}
