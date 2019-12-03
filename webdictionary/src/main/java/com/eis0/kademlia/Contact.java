package com.eis0.kademlia;

/**
 * Keeps information about contacts of the Node;
 * Such as how many times it doesn't respond to a request (staleCount)
 * or his lastSeen
 *
 * @see <a href="https://pdos.csail.mit.edu/~petar/papers/maymounkov-kademlia-lncs.pdf">Kademlia's
 *      paper</a> for more details.
 */
public class Contact implements Comparable<Contact> { //has to be comparable to test it

    private final SMSKademliaNode n;
    private long lastSeen;
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
    public Contact(SMSKademliaNode n) {

        this.n = n;
        this.lastSeen = System.currentTimeMillis() / 1000L;
    }

    /**
     * This method updates the last seen timestamp for this contact.
     */
    public void setSeenNow()
    {
        this.lastSeen = System.currentTimeMillis() / 1000L;
    }

    /**
     * When last was this contact seen?
     *
     * @return long representing the last time this contact was seen.
     */
    public long lastSeen()
    {
        return this.lastSeen;
    }

    /**
     * @return The node associated to this contact
     */
    public SMSKademliaNode getNode() {
        return this.n;
    }

    /**
     * Method to compare two Nodes inside different contacts
     * @param toCompare Object to compare
     * @return true if equal
     */
    @Override
    public boolean equals(Object toCompare) {
        if (toCompare instanceof Contact) {
            return ((Contact) toCompare).getNode().equals(this.getNode());
        }
        return false;
    }

    /**
     * Increments the amount of times this count has failed to respond to a request.
     */
    public void incrementStaleCount() {
        staleCount++;
    }

    /**
     * @return Integer representing stale count
     */
    public int staleCount() {
        return this.staleCount;
    }

    /**
     * Reset the stale count of the contact if it's recently seen
     */
    public void resetStaleCount() {
        this.staleCount = 0;
    }

    /**
     * Method to compare two contacts
     * @param toCompare Contact to compare with this
     * @return 0 if equals, -1 otherwise
     */
    @Override
    public int compareTo(Contact toCompare) {
        if (this.getNode().equals(toCompare.getNode())) {
            return 0;
        }
        return -1;
    }

    /**
     * Get the node contact's hashcode
     * @return integer representing the node hashcode
     */
    @Override
    public int hashCode() {
        return this.getNode().hashCode();
    }
}

