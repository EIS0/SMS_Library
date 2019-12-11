package com.eis0.kademlianetwork;

import com.eis0.kademlia.Contact;
import com.eis0.kademlia.DefaultConfiguration;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;
import com.eis0.webdictionary.SMSNetVocabulary;
import com.eis0.webdictionary.SerializableObject;

/**
 * Central class fo the KademliaNetwork. Instead of handling everything itself,
 * calls the proper handler. This is basically the
 * <a href="https://refactoring.guru/design-patterns/chain-of-responsibility">Chain Of Responsibility</a>
 * Design Pattern, where this class instead of handling everything
 * itself (which would make it a really big class), redirects the work on the proper handler.
 *
 * @author Matteo Carnelos
 */
public class KademliaNetwork {

    //User node of the network
    private SMSKademliaNode node;
    //Routing table for this user of the network
    private SMSKademliaRoutingTable localRoutingTable;
    //Dictionary containing the resources stored by the local node
    private SMSNetVocabulary localKademliaDictionary;

    // Request types for the Kademlia Network
    public enum RequestType {
        JoinPermission,
        AcceptJoin,
        AddPeers,
        RemovePeers,
        UpdatePeers,
        LeavePermission,
        AcceptLeave,
        AcknowledgeMessage,
        AddToDict,
        RemoveFromDict,
        UpdateDict,
        NodeLookup
    }

    // Singleton instance
    private static KademliaNetwork instance;

    // Constructor following the Singleton Design Pattern
    private KademliaNetwork() {
    }

    /**
     * Return an instance of KademliaNetwork.
     *
     * @return Returns a single KademliaNetwork instance as per the
     * <a href="https://refactoring.guru/design-patterns/singleton">Singleton Design Pattern</a>.
     * @author Matteo Carnelos
     */
    public static KademliaNetwork getInstance() {
        if (instance == null) instance = new KademliaNetwork();
        return instance;
    }

    /**
     * Initialize the network by setting the current node.
     *
     * @param localNode The SMSKademliaNode to set.
     * @author Matteo Carnelos
     */
    public void init(SMSKademliaNode localNode) {
        this.node = localNode;
        localRoutingTable = new SMSKademliaRoutingTable(localNode, new DefaultConfiguration());
    }

    /**
     * Get the local node.
     *
     * @return The SMSKademliaNode representing the local node.
     * @author Matteo Carnelos
     */
    public SMSKademliaNode getLocalNode() {
        return node;
    }

    /**
     * Get the local routing table.
     *
     * @return The SMSKademliaRoutingTable representing the local routing table.
     * @author Matteo Carnelos
     */
    public SMSKademliaRoutingTable getLocalRoutingTable() {
        return localRoutingTable;
    }

    /**
     * Returns if a given valid {@link SMSKademliaNode} is inside this
     * network's routing table
     *
     * @param node The node to find in the routing table
     * @return True if node is in the routing table, false otherwise
     */
    public boolean isNodeInNetwork(SMSKademliaNode node) {
        Contact nodeContact = new Contact(node);
        return localRoutingTable.getAllContacts().contains(nodeContact);
    }

    /**
     * Adds a given valid SMSKademliaNode to the routing table of this network
     *
     * @param node The valid node to add to the net
     */
    public void addNodeToTable(SMSKademliaNode node) {
        Contact nodeContact = new Contact(node);
        localRoutingTable.insert(nodeContact);
    }

    /**
     * Updates the routing table
     */
    public void updateTable() {
        //calls the proper handler to update the routing table
        TableUpdateHandler.updateTable(localRoutingTable);
    }








    
    //Methods to manipulate the Dictionary
    /**
     * Method used to get the local Dictionary
     *
     * @return The SMSNetVocabulary representing the local Dictionary
     * @author Enrico Cestaro
     */
    public SMSNetVocabulary getLocalDictionary() {
        return localKademliaDictionary;
    }

    /**
     * Method used to add a pair <key, resource> to the local Dictionary
     *
     * @param key      The key of the pair <key, resource> the user is trying to add to the dictionary
     * @param resource The resource of the pair <key, resource> the user is trying to add to the dictionary
     * @author Enrico Cestaro
     */
    public void addToLocalDictionary(SerializableObject key, SerializableObject resource) {
        localKademliaDictionary.add(key, resource);
    }

    /**
     * Method used to remove a pair <key, resource> from the local Dictionary
     *
     * @param
     * @return
     * @author Enrico Cestaro
     */
    public void removeFromLocalDictionary(SerializableObject key) {
        localKademliaDictionary.remove(key);
    }

    /**
     * Method used to get a resource specified by its key
     *
     * @param
     * @return
     * @author Enrico Cestaro
     */
    public SerializableObject getFromLocalDictionary(SerializableObject key) {
        return localKademliaDictionary.getResource(key);
    }

    /**
     * Method used to update a resource with the specified key, with a new resource
     *
     * @param key
     * @param resource
     * @author Enrico Cestaro
     */
    public void updateLocalDictionary(SerializableObject key, SerializableObject resource) {
        localKademliaDictionary.update(key, resource);
    }
}
