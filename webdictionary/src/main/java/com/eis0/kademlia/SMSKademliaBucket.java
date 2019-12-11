package com.eis0.kademlia;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/**
 * A bucket inside the routing table
 * For each 0 < i < 160, every node keeps a list of ID for nodes of distance
 * between 2i and 2i+1 from itself.
 * These List are called bucket.
 *
 * @see <a href="https://pdos.csail.mit.edu/~petar/papers/maymounkov-kademlia-lncs.pdf">Kademlia's
 * *      paper</a> for more details.
 * @author Edoardo Raimondi
 */
public class SMSKademliaBucket implements KademliaBucket {

    /* How deep is this bucket in the Routing Table */
    private int depth;

    /* Contacts stored in this routing table */
    private TreeSet<Contact> contacts = new TreeSet<>();

    /* A set of last seen contacts that can replace any current contact that is unresponsive */
    private TreeSet<Contact> replacementCache = new TreeSet<>();

    private KadConfiguration config;

    /**
     * @param depth How deep in the routing tree is this bucket
     */
    public SMSKademliaBucket(int depth, KadConfiguration config) {
        this.depth = depth;
        this.config = config;
    }

    /**
     * Insert a contact in the bucket
     *
     * @param c the new contact
     */
    @Override
    public void insert(Contact c) {
        if (this.contacts.contains(c)) {
            /*
             * If the contact is already in the bucket, lets update that we've seen it
             * We need to remove and re-add the contact to get the Sorted Set to update sort order
             */
            Contact tmp = this.removeFromContacts(c.getNode());
            tmp.setSeenNow();
            tmp.resetStaleCount();
            this.contacts.add(tmp);
        } else {
            /* If the bucket is filled, so put the contacts in the replacement cache */
            if (contacts.size() >= this.config.k()) {
                /* If the cache is empty, we check if any contacts are stale and replace the stalest one */
                Contact stalest = null;
                for (Contact tmp : this.contacts) {
                    if (tmp.staleCount() >= this.config.stale()) {
                        /* Contact is stale */
                        if (stalest == null) {
                            stalest = tmp;
                        } else if (tmp.staleCount() > stalest.staleCount()) {
                            stalest = tmp;
                        }
                    }
                }

                /* If we have a stale contact, remove it and add the new contact to the bucket */
                if (stalest != null) {
                    this.contacts.remove(stalest);
                    this.contacts.add(c);
                } else {
                    /* No stale contact, lets insert this into replacement cache */
                    this.insertIntoReplacementCache(c);
                }
            } else {
                this.contacts.add(c);
            }
        }
    }

    /**
     * Insert a contact form a node
     *
     * @param n The node to create the contact from
     */
    @Override
    public void insert(SMSKademliaNode n) {
        this.insert(new Contact(n));
    }

    /**
     * @param c The contact to check for
     * @return true if there is c
     */
    @Override
    public boolean containsContact(Contact c) {
        return this.contacts.contains(c);
    }

    /**
     * @param n The node to check for
     * @return true if there is n
     */
    @Override
    public boolean containsNode(SMSKademliaNode n) {
        return this.containsContact(new Contact(n));
    }

    /**
     * @param c The contact to remove
     * @return true if well removed
     */
    @Override
    public boolean removeContact(Contact c) {
        /* If the contact does not exist, then we failed to remove it */
        if (!this.contacts.contains(c)) {
            return false;
        }

        /* Contact exist, lets remove it only if our replacement cache has a replacement */
        if (!this.replacementCache.isEmpty()) {
            /* Replace the contact with one from the replacement cache */
            this.contacts.remove(c);
            Contact replacement = this.replacementCache.first();
            this.contacts.add(replacement);
            this.replacementCache.remove(replacement);
        } else {
            /* There is no replacement, just increment the contact's stale count */
            this.getFromContacts(c.getNode()).incrementStaleCount();
        }

        return true;
    }

    /**
     * @param n The node to get the contact from
     * @return The contact
     * @throw NoSuchElementException if the contact isn't in the list
     */
    public Contact getFromContacts(SMSKademliaNode n) {
        for (Contact c : this.contacts) {
            if (c.getNode().equals(n)) {
                return c;
            }
        }

        /* This contact does not exist */
        throw new NoSuchElementException("The contact does not exist in the contacts list.");
    }

    /**
     * @param n The node representing the contact to remove
     * @return The contact removed
     * @throw NoSuchElementException if the contact isn't in the list
     */
    public Contact removeFromContacts(SMSKademliaNode n) {
        for (Contact c : this.contacts) {
            if (c.getNode().equals(n)) {
                this.contacts.remove(c);
                return c;
            }
        }

        /* We got here means this element does not exist */
        throw new NoSuchElementException("Node does not exist in the replacement cache. ");
    }

    /**
     * @param n The node of the contact to remove
     * @return true if well removed
     */
    @Override
    public boolean removeNode(SMSKademliaNode n) {
        return this.removeContact(new Contact(n));
    }

    /**
     * @return Int representing the contacts number
     */
    @Override
    public int numContacts() {
        return this.contacts.size();
    }

    /**
     * @return Int representing the bucket depth
     */
    @Override
    public int getDepth() {
        return this.depth;
    }

    /**
     * @return A list of all the contacts in the bucket
     */
    @Override
    public List<Contact> getContacts() {
        final ArrayList<Contact> ret = new ArrayList<>();

        /* If we have no contacts, return the blank arraylist */
        if (this.contacts.isEmpty()) {
            return ret;
        }

        /* We have contacts, lets copy put them into the arraylist and return */
        for (Contact c : this.contacts) {
            ret.add(c);
        }

        return ret;
    }

    /**
     * When the bucket is filled, we keep extra contacts in the replacement cache.
     */
    private void insertIntoReplacementCache(Contact c) {
        /* Just return if this contact is already in our replacement cache */
        if (this.replacementCache.contains(c)) {
            /**
             * If the contact is already in the bucket, lets update that we've seen it
             * We need to remove and re-add the contact to get the Sorted Set to update sort order
             */
            Contact tmp = this.removeFromReplacementCache(c.getNode());
            tmp.setSeenNow();
            this.replacementCache.add(tmp);
        } else if (this.replacementCache.size() > this.config.k()) {
            /* if our cache is filled, we remove the least recently seen contact */
            this.replacementCache.remove(this.replacementCache.last());
            this.replacementCache.add(c);
        } else {
            this.replacementCache.add(c);
        }
    }

    /**
     * Remove a contact from the replacement cache
     *
     * @param n Node to remove
     * @return Contact removed
     * @throw NoSuchElementException if the node isn't in the list
     */
    private Contact removeFromReplacementCache(SMSKademliaNode n) {
        for (Contact c : this.replacementCache) {
            if (c.getNode().equals(n)) {
                this.replacementCache.remove(c);
                return c;
            }
        }

        /* We got here means this element does not exist */
        throw new NoSuchElementException("Node does not exist in the replacement cache. ");
    }

    /**
     * @return replacementChace size
     */
    public int getReplacementCacheSize() {
        return this.replacementCache.size();
    }

    /**
     * @return A String with all the bucket's informations
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Bucket at depth: ");
        sb.append(this.depth);
        sb.append("\n Nodes: \n");
        for (Contact n : this.contacts) {
            sb.append("Node: ");
            sb.append(n.getNode().getId().toString());
            sb.append(" (stale: ");
            sb.append(n.staleCount());
            sb.append(")");
            sb.append("\n");
        }

        return sb.toString();
    }
}
