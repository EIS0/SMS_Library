package com.eis0.kademlia;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Implementation of a Kademlia routing table.
 * Every Node has his own table. It contains his well known contacts.
 * A routing table is composed by different Buckets (see SMSKademliaBucket)
 * Every routing table has a configuration that, at the creation, will be a DefaultConfiguration
 *
 * @see <a href="https://pdos.csail.mit.edu/~petar/papers/maymounkov-kademlia-lncs.pdf">Kademlia's
 * paper</a> for more details.
 * @author Edoardo Raimondi
 */
public class SMSKademliaRoutingTable implements KademliaRoutingTable {

    private final SMSKademliaNode localNode;  // The current node
    private transient SMSKademliaBucket[] buckets;
    private transient KadConfiguration config;

    public SMSKademliaRoutingTable(SMSKademliaNode localNode, KadConfiguration config) {
        this.localNode = localNode;
        this.config = config;

        /* Initialize all of the buckets to a specific depth */
        initialize();

        /* Insert the local node */
        insert(localNode);
    }

    /**
     * Initialize the RoutingTable to it's default state
     */
    @Override
    public final void initialize() {
        this.buckets = new SMSKademliaBucket[KademliaId.ID_LENGTH];
        for (int i = 0; i < KademliaId.ID_LENGTH; i++) {
            buckets[i] = new SMSKademliaBucket(i, this.config);
        }
    }

    /**
     * Set configuration as default.
     *
     * @param config The configuration to set as default.
     */
    public void setConfiguration(KadConfiguration config) {
        this.config = config;
    }

    /**
     * Adds a contact to the routing table based on how far it is from the LocalNode.
     *
     * @param contact The contact to add
     */
    @Override
    public final void insert(Contact contact) {
        this.buckets[this.getBucketId(contact.getNode().getId())].insert(contact);
    }

    /**
     * Adds a node to the routing table based on how far it is from the LocalNode.
     *
     * @param node The node to add
     */

    public synchronized final void insert(SMSKademliaNode node) {
        this.buckets[this.getBucketId(node.getId())].insert(node);
    }

    /**
     * Compute the bucket ID in which a given node should be placed.
     * The bucketId is computed based on how far the node is away from the Local Node.
     *
     * @param nodeId The NodeId for which we want to find which bucket it belong to
     * @return Integer The bucket ID in which the given node should be placed.
     */
    @Override
    public final int getBucketId(KademliaId nodeId) {
        int bId = this.localNode.getId().getDistance(nodeId);

        /* If we are trying to insert a node into it's own routing table, then the bucket ID will be -1, so let's just keep it in bucket 0 */
        return bId-1 < 0 ? 0 : bId-1;
    }

    /**
     * Find the closest set of contacts to a given NodeId
     *
     * @param target           The NodeId to find contacts close to
     * @param numNodesRequired The number of contacts to find
     * @return List A List of contacts closest to target
     */
    @Override
    public synchronized final List<SMSKademliaNode> findClosest(KademliaId target, int numNodesRequired) {
        TreeSet<SMSKademliaNode> sortedSet = new TreeSet<>(new KeyComparator(target));
        sortedSet.addAll(this.getAllNodes());

        List<SMSKademliaNode> closest = new ArrayList<>(numNodesRequired);

        /* Now we have the sorted set, lets get the top numRequired */
        int count = 0;
        for (SMSKademliaNode n : sortedSet) {
            closest.add(n);
            if (++count == numNodesRequired) {
                break;
            }
        }
        return closest;
    }

    /**
     * @return List of all Nodes in this RoutingTable
     */
    @Override
    public final List<SMSKademliaNode> getAllNodes() {
        List<SMSKademliaNode> nodes = new ArrayList<>();

        for (KademliaBucket bucket : this.buckets) {
            for (Contact contact : bucket.getContacts()) {
                nodes.add(contact.getNode());
            }
        }

        return nodes;
    }

    /**
     * @return List A List of all Contacts in this RoutingTable
     */
    @Override
    public final List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();

        for (KademliaBucket b : this.buckets) {
            contacts.addAll(b.getContacts());
        }

        return contacts;
    }

    /**
     * @return Bucket[] The buckets in this Kad Instance
     */
    @Override
    public final SMSKademliaBucket[] getBuckets() {
        return this.buckets;
    }

    /**
     * Set the Buckets of this routing table, mainly used when retrieving saved state
     *
     * @param buckets The buckets to use.
     */
    public final void setBuckets(SMSKademliaBucket[] buckets) {
        this.buckets = buckets;
    }

    /**
     * Print routing table
     *
     * @return String representing the routing table
     */
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder("\nPrinting Routing Table Started... \n");
        int totalContacts = 0;
        for (KademliaBucket b : this.buckets) {
            if (b.numContacts() > 0) {
                totalContacts += b.numContacts();
                sb.append("# nodes in Bucket with depth ");
                sb.append(b.getDepth());
                sb.append(": ");
                sb.append(b.numContacts());
                sb.append("\n");
                sb.append(b.toString());
                sb.append("\n");
            }
        }

        sb.append("\nTotal Contacts: ");
        sb.append(totalContacts);
        sb.append("\n");

        return sb.toString();
    }
}