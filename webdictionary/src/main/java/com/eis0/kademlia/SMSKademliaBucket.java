package com.eis0.kademlia;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/**
 * A bucket inside the routing table
 * For each 0 < i < 64, every node keeps a list of ID for nodes of distance
 * between 2i and 2i+1 from itself.
 * These List are called bucket.
 *
 * @see <a href="https://pdos.csail.mit.edu/~petar/papers/maymounkov-kademlia-lncs.pdf">Kademlia's
 * *      paper</a> for more details.
 * @author Edoardo Raimondi
 * @author edits by Marco Cognolato
 */
public class SMSKademliaBucket implements KademliaBucket {

    /* How deep is this bucket in the Routing Table */
    private int depth;

    /* Contacts stored in this routing table */
    private TreeSet<Contact> contacts;

    /* A set of last seen contacts that can replace any current contact that is unresponsive */
    private TreeSet<Contact> replacementCache;

    /*Configuration of this kademlia implementation*/
    private KadConfiguration config;

    {
        //I use tree set so I'll have an ordered list of contact based on their lastSeen param
        contacts = new TreeSet<>();
        replacementCache = new TreeSet<>();
    }

    /**
     * Constructor for the bucket
     *
     * @param depth How deep in the routing tree is this bucket
     * @param config Configuration for this kademlia bucket
     */
    public SMSKademliaBucket(int depth, KadConfiguration config) {
        this.depth = depth;
        this.config = config;
    }

    /**
     * Insert a given valid contact in the bucket.
     * If the bucket is filled, it puts the contact in the replacement cache
     *
     * @param contact The contact to add
     */
    @Override
    public void insert(Contact contact) {
        if (this.contacts.contains(contact)) {
            /*
             * If the contact is already in the bucket, lets update that we've seen it
             * We need to remove and re-add the contact to get the Sorted Set to update sort order
             * (see SMSKademliaRoutingTable)
             */
            this.removeContact(contact);
            contact.setSeenNow();
            contact.resetStaleCount();
            this.contacts.add(contact);
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
                    this.contacts.add(contact);
                } else {
                    /* No stale contact, lets insert this into replacement cache */
                    this.insertIntoReplacementCache(contact);
                }
            } else {
                this.contacts.add(contact);
            }
        }
    }

    /**
     * Insert a contact form a node
     *
     * @param node The node to create the contact from
     */
    @Override
    public void insert(SMSKademliaNode node) {
        this.insert(new Contact(node));
    }

    /**
     * @param contact The contact to check for
     * @return true if there is contact
     */
    @Override
    public boolean containsContact(Contact contact) {
        return this.contacts.contains(contact);
    }

    /**
     * @param node The node to check for
     * @return true if there is node
     */
    @Override
    public boolean containsNode(SMSKademliaNode node) {
        return this.containsContact(new Contact(node));
    }

    /**
     * Removes a contact from the bucket, if a
     * replacement exists in the cache it's also replaced
     * @param contact The contact to remove
     *
     * @author Edoardo Raimondi
     * @author edits by Marco Cognolato
     */
    @Override
    public void removeContact(Contact contact) {
        /* If the contact does not exist, we don't have anything to remove */
        if (!this.contacts.contains(contact)) {
            return;
        }
        //Contact exists, remove it
        this.contacts.remove(contact);

        //If we have a replacement...
        if (!this.replacementCache.isEmpty()) {
            /* Replace the contact with one from the replacement cache */
            Contact replacement = this.replacementCache.first();
            this.contacts.add(replacement);
            this.replacementCache.remove(replacement);
        }
    }

    /**
     * @param node The node to get the contact from
     * @return The contact associated with that node
     * @throws NoSuchElementException if the contact isn't in the list
     */
    public Contact getFromContacts(SMSKademliaNode node) {
        for (Contact contact : this.contacts) {
            if (contact.getNode().equals(node)) {
                return contact;
            }
        }

        /* This contact does not exist */
        throw new NoSuchElementException("The contact does not exist in the contacts list.");
    }

    /**
     * @param node The node of the contact to remove
     */
    @Override
    public void removeNode(SMSKademliaNode node) {
        this.removeContact(new Contact(node));
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
    public List<Contact> getContacts()
    {
        final ArrayList<Contact> ret = new ArrayList<>();

        /* If we have no contacts, return the blank arraylist */
        if (this.contacts.isEmpty())
        {
            return ret;
        }

        /* We have contacts, lets copy put them into the arraylist and return */
        for (Contact c : this.contacts)
        {
            ret.add(c);
        }

        return ret;
    }

    /**
     * When the bucket is filled, we keep extra contacts in the replacement cache.
     * Private because user has not to use it
     *
     * @param contact to insert
     */
    private void insertIntoReplacementCache(Contact contact) {
        /* Just return if this contact is already in our replacement cache */
        if (this.replacementCache.contains(contact)) {
            /**
             * If the contact is already in the bucket, lets update that we've seen it
             * We need to remove and re-add the contact to get the Sorted Set to update sort order
             */
            Contact tmp = this.removeFromReplacementCache(contact.getNode());
            tmp.setSeenNow();
            this.replacementCache.add(tmp);
        } else if (this.replacementCache.size() > this.config.k()) {
            /* if our cache is filled, we remove the least recently seen contact */
            this.replacementCache.remove(this.replacementCache.last());
            this.replacementCache.add(contact);
        } else {
            this.replacementCache.add(contact);
        }
    }

    /**
     * Remove a contact from the replacement cache
     * Private because user has not to use it
     *
     * @param node Node to remove
     * @return Contact removed
     * @throws NoSuchElementException if the node isn't in the list
     */
    private Contact removeFromReplacementCache(SMSKademliaNode node) {
        for (Contact contact : this.replacementCache) {
            if (contact.getNode().equals(node)) {
                this.replacementCache.remove(contact);
                return contact;
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
        for (Contact contact : this.contacts) {
            sb.append("Node: ");
            sb.append(contact.getNode().getId().toString());
            sb.append(" (stale: ");
            sb.append(contact.staleCount());
            sb.append(")");
            sb.append("\n");
        }

        return sb.toString();
    }
}
