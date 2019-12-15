package com.eis0.kademlia;

import java.util.List;

/**
 * A bucket used to store Contacts in the routing table.
 *
 * @author Edoardo Raimondi
 */
public interface KademliaBucket {

    /**
     * Adds a contact to the bucket
     *
     * @param c the new contact
     */
    void insert(Contact c);

    /**
     * Create a new contact and insert it into the bucket.
     *
     * @param n The node to create the contact from
     */
    void insert(SMSKademliaNode n);

    /**
     * Checks if this bucket contain a contact
     *
     * @param c The contact to check for
     * @return boolean
     */
    boolean containsContact(Contact c);

    /**
     * Checks if this bucket contain a node
     *
     * @param n The node to check for
     * @return boolean
     */
    boolean containsNode(SMSKademliaNode n);

    /**
     * Removes a contact from this bucket.
     *
     * @param c The contact to remove
     */
    void removeContact(Contact c);

    /**
     * Removes the contact object related to a node from this bucket
     *
     * @param n The node of the contact to remove
     */
    void removeNode(SMSKademliaNode n);

    /**
     * Counts the number of contacts in this bucket.
     *
     * @return Integer The number of contacts in this bucket
     */
    int numContacts();

    /**
     * @return Integer The depth of this bucket in the RoutingTable
     */
    int getDepth();

    /**
     * @return An Iterable structure with all contacts in this bucket
     */
    List<Contact> getContacts();
}