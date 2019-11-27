package com.example.kademlia;

import java.util.List;

/**
 * Specification for Kademlia's Routing Table
 */
public interface KademliaRoutingTable {

    /**
     * Initialize the RoutingTable to it's default state
     */
    void initialize();

    /**
     * Adds a contact to the routing table based on how far it is from the LocalNode.
     *
     * @param c The contact to add
     */
    void insert(Contact c);

    /**
     * Adds a node to the routing table based on how far it is from the LocalNode.
     *
     * @param n The node to add
     */
     void insert(SMSKademliaNode n);

    /**
     * Compute the bucket ID in which a given node should be placed; the bucketId is computed based on how far the node is away from the Local Node.
     *
     * @param nid The NodeId for which we want to find which bucket it belong to
     *
     * @return Integer The bucket ID in which the given node should be placed.
     */
     int getBucketId(KademliaId nid);

    /**
     * Find the closest set of contacts to a given NodeId
     *
     * @param target           The NodeId to find contacts close to
     * @param numNodesRequired The number of contacts to find
     *
     * @return List A List of contacts closest to target
     */
     List<SMSKademliaNode> findClosest(KademliaId target, int numNodesRequired);

    /**
     * @return List A List of all Nodes in this RoutingTable
     */
     List getAllNodes();

    /**
     * @return List A List of all Nodes in this RoutingTable
     */
     List getAllContacts();

    /**
     * @return Bucket[] The buckets in this Kad Instance
     */
     KademliaBucket[] getBuckets();
}
