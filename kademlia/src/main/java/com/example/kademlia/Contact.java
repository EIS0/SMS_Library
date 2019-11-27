package com.example.kademlia;


/**
 * Keeps information about contacts of the Node;
 * Contacts are stored
 *
 */
public class Contact implements  Comparable<Contact>{ //has to be comparable to testing it

    private final SMSKademliaNode n;

    /**
     * When a contact fails to respond, if there is no replacement for the contact,
     * just mark it as stale.
     *
     * When a new contact is added, if the contact is stale, it is removed.
     */
    private int staleCount;

    /**
     * Create a contact
     *
     * @param n The node associated with this contact
     */
    public Contact(SMSKademliaNode n)
    {
        this.n = n;
    }

    public SMSKademliaNode getNode()
    {
        return this.n;
    }


    @Override
    public boolean equals(Object toCompare)
    {
        if (toCompare instanceof Contact)
        {
            return ((Contact) toCompare).getNode().equals(this.getNode());
        }

        return false;
    }

    /**
     * Increments the amount of times this count has failed to respond to a request.
     */
    public void incrementStaleCount()
    {
        staleCount++;
    }

    /**
     * @return Integer Stale count
     */
    public int staleCount()
    {
        return this.staleCount;
    }

    /**
     * Reset the stale count of the contact if it's recently seen
     */
    public void resetStaleCount()
    {
        this.staleCount = 0;
    }

    @Override
    public int compareTo(Contact o) {
        if (this.getNode().equals(o.getNode())) {
            return 0;
        }
        return -1;
    }
    @Override
    public int hashCode()
    {
        return this.getNode().hashCode();
    }

}

