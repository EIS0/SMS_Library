package com.eis0.kademlianetwork;

import androidx.annotation.NonNull;

import com.eis0.kademlia.Contact;
import com.eis0.kademlia.DefaultConfiguration;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSPeer;
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
    private SMSKademliaNode localNode;
    //Routing table for this user of the network
    private SMSKademliaRoutingTable localRoutingTable;
    private ConnectionHandler connectionHandler = new ConnectionHandler();
    private KademliaListener listener;
    //Dictionary containing the resources stored by the local node
    private SMSNetVocabulary kademliaDictionary;

    // Singleton instance
    private static KademliaNetwork instance;

    /**
     * Return an instance of KademliaNetwork.
     *
     * @return Returns a single KademliaNetwork instance as per the
     * <a href="https://refactoring.guru/design-patterns/singleton">Singleton Design Pattern</a>.
     * @author Matteo Carnelos
     */
    public static KademliaNetwork getInstance() {
        if(instance == null) instance = new KademliaNetwork();
        return instance;
    }

    /**
     * Initialize the network by setting the current node.
     *
     * @param localNode The SMSKademliaNode to set.
     * @author Matteo Carnelos
     */
    public void init(SMSKademliaNode localNode, KademliaListener listener) {
        this.localNode = localNode;
        this.listener = listener;
        localRoutingTable = new SMSKademliaRoutingTable(localNode, new DefaultConfiguration());
    }

    /**
     * Add a peer to the kademlia network.
     *
     * @param peer The SMSPeer to add.
     * @author Matteo Carnelos
     */
    public void addToNetwork(SMSPeer peer) {
        if(!isNodeInNetwork(new SMSKademliaNode(peer)))
            connectionHandler.inviteToJoin(peer);
    }

    /**
     * Get the local node.
     *
     * @return The SMSKademliaNode representing the local node.
     * @author Matteo Carnelos
     */
    public SMSKademliaNode getLocalNode() {
        return localNode;
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
     * @param node The node to find in the routing table
     * @return True if node is in the routing table, false otherwise
     * @author Marco Cognolato
     */
    public boolean isNodeInNetwork(SMSKademliaNode node){
        Contact nodeContact = new Contact(node);
        return localRoutingTable.getAllContacts().contains(nodeContact);
    }

    /**
     * Adds a given valid SMSKademliaNode to the routing table of this network
     * @param node The valid node to add to the net
     * @author Marco Cognolato
     */
    public void addNodeToTable(SMSKademliaNode node){
        Contact nodeContact = new Contact(node);
        localRoutingTable.insert(nodeContact);
        listener.onNewContact(nodeContact);
    }

    /**
     * Updates the routing table
     */
    public void updateTable(){
        //calls the proper handler to update the routing table
        SMSPeer netPeer = localNode.getPeer();
        TableUpdateHandler.updateTable(localRoutingTable, localNode.getId(), netPeer);
    }


    /**
     * Add a key-content to this local node dictionary
     * They have to be serialize in order to be add
     * @param key     to add
     * @param content to add
     */
    public void addToLocalDictionary(String key, String content) {
    }


    /**
     *
     */
    public void removeFromLocalDictionary() {

    }


    /**
     *
     */
    public void getFromLocalDictionary() {

    }


    /**
     *
     */
    public void updateLocalDictionary() {

    }
}
