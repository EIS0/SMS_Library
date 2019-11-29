package com.eis0.kademlia;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
/**
 * Implementation of a Kademlia routing table.
 * Every Node has his own table. It contains his well known contacts.
 * A routing table is composed by different Buckets (see SMSKademliaBucket)
 * Every routing table has a configuration that, at the creation, will be a DefaultConfiguration
 */
public class SMSKademliaRoutingTable implements KademliaRoutingTable {

    private final SMSKademliaNode localNode;  // The current node
    private transient SMSKademliaBucket[] buckets;
    private transient KadConfiguration config;

    public SMSKademliaRoutingTable(SMSKademliaNode localNode, KadConfiguration config) {
        this.localNode = localNode;
        this.config = config;

        /* Initialize all of the buckets to a specific depth */
        this.initialize();

        /* Insert the local node */
        this.insert(localNode);
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
     * Set configuration as default
     * @param config
     */
    public void setConfiguration(KadConfiguration config)
    {
        this.config = config;
    }

    /**
     * Adds a contact to the routing table based on how far it is from the LocalNode.
     *
     * @param c The contact to add
     */
    @Override
    public synchronized final void insert(Contact c) {
        this.buckets[this.getBucketId(c.getNode().getNodeId())].insert(c);
    }

    /**
     * Adds a node to the routing table based on how far it is from the LocalNode.
     *
     * @param n The node to add
     */

    public synchronized final void insert(SMSKademliaNode n) {
        this.buckets[this.getBucketId(n.getNodeId())].insert(n);
    }

    /**
     * Compute the bucket ID in which a given node should be placed.
     * The bucketId is computed based on how far the node is away from the Local Node.
     *
     * @param nid The NodeId for which we want to find which bucket it belong to
     *
     * @return Integer The bucket ID in which the given node should be placed.
     */
    @Override
    public final int getBucketId(KademliaId nid) {
        int bId = this.localNode.getNodeId().getDistance(nid) - 1;

        /* If we are trying to insert a node into it's own routing table, then the bucket ID will be -1, so let's just keep it in bucket 0 */
        return bId < 0 ? 0 : bId;
    }

    /**
     * Find the closest set of contacts to a given NodeId
     *
     * @param target           The NodeId to find contacts close to
     * @param numNodesRequired The number of contacts to find
     *
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
    public synchronized final List<SMSKademliaNode> getAllNodes() {
        List<SMSKademliaNode> nodes = new ArrayList<>();

        for (KademliaBucket b : this.buckets) {
            for (Contact c : b.getContacts()) {
                nodes.add(c.getNode());
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
     * @param buckets
     */
    public final void setBuckets(SMSKademliaBucket[] buckets) {
        this.buckets = buckets;
    }

    /**
     * Print routing table
     * @return String representing the routing table
     */
    @Override
    public synchronized final String toString() {
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