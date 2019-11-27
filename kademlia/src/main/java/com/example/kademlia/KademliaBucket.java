package com.example.kademlia;

import java.util.List;

/**
 * A bucket used to store Contacts(or SMSKademliaNode directly?) in the routing table.
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
     *
     * @return boolean
     */
    boolean containsContact(Contact c);

    /**
     * Checks if this bucket contain a node
     *
     * @param n The node to check for
     *
     * @return boolean
     */
    boolean containsNode(SMSKademliaNode n);

    /**
     * Remove a contact from this bucket.
     *
     * @param c The contact to remove
     *
     * @return Boolean whether the removal was successful.
     */
    boolean removeContact(Contact c);

    /**
     * Remove the contact object related to a node from this bucket
     *
     * @param n The node of the contact to remove
     *
     * @return Boolean whether the removal was successful.
     */
    boolean removeNode(SMSKademliaNode n);

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