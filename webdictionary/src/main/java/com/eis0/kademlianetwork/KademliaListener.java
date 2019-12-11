package com.eis0.kademlianetwork;

import com.eis0.kademlia.Contact;

/**
 * Interface to implement in order to get notified by the KademliaNetwork about new events.
 *
 * @author Matteo Carnelos
 */
public interface KademliaListener {

    /**
     * Event called when a Contact is added in the routing table.
     *
     * @param contact The contact added.
     * @author Matteo Carnelos
     */
    void onNewContact(Contact contact);
}
