package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSHandler;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.Contact;
import com.eis0.kademlia.DefaultConfiguration;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;
import com.eis0.webdictionary.SMSNetVocabulary;
import com.eis0.webdictionary.SMSSerialization;
import com.eis0.webdictionary.SerializableObject;

/**
 * Central class fo the KademliaNetwork. Instead of handling everything itself,
 * calls the proper handler. This is basically the
 * <a href="https://refactoring.guru/design-patterns/chain-of-responsibility">Chain Of Responsibility</a>
 * Design Pattern, where this class instead of handling everything
 * itself (which would make it a really big class), redirects the work on the proper handler.
 *
 * @author Matteo Carnelos
 * @author Edoardo Raimondi
 */
public class KademliaNetwork {

    //User node of the network
    private SMSKademliaNode localNode;
    //Routing table for this user of the network
    private SMSKademliaRoutingTable localRoutingTable;
    private ConnectionHandler connectionHandler = new ConnectionHandler();
    private KademliaListener listener;
    private SMSKademliaListener smsKademliaListener;
    //Dictionary containing the resources stored by the local node
    private SMSNetVocabulary localKademliaDictionary;
    //To know if I had a positive acknowledge respond to a sent command
    private boolean hasRespond;
    //Create a 10 secs responds timer. If a node doesn't respond with an acknowledge message
    //in that time, it is considered broken
    RespondTimer timer = new RespondTimer();

    // Singleton instance
    private static KademliaNetwork instance;
    // Constructor following the Singleton Design Pattern
    private KademliaNetwork() {
        SMSHandler.getInstance().setReceivedListener(smsKademliaListener.getClass());
    }
    
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
        hasRespond = false;
    }

    /**
     * @return true if I had an acknowledge respond to a sent command, false otherwise
     * @author Edoardo Raimondi
     */
    public boolean hasRespond() { return hasRespond; }

    /**
     * Set true if I had an acknowledge respond, false otherwise
     *
     * @param  value of the respond state
     * @author Edoardo Raimondi
     */
    public void setRespond(boolean value){ hasRespond = value; }

    /**
     * Check if I received an acknowledge respond to my request.
     * (so if the node is alive)
     * If death, the {@link SMSKademliaNode} of the target peer is removed from the routing table
     *
     * @param  targetPeer the receiver {@link SMSPeer}
     * @return true if alive, false otherwise
     * @author Edoardo Raimondi
     */
    public boolean isAlive(SMSPeer targetPeer){
        //I wait 10 secs
        timer.run();
        //check if I had an acknowledge respond to my request
        if(hasRespond()){
            //I know my request has ben received successfully. I set false in order to do it again
            setRespond(false);
            return true;
        }
        else { //My target node is broken. I remove it from my routing table
            //create the node by the peer
            SMSKademliaNode toRemove = new SMSKademliaNode(new KademliaId(targetPeer));
            //remove it
            this.localRoutingTable.getBuckets()[0].removeNode(toRemove);
            //the node is not alive
            return false;
        }
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
     * Method used to add a pair <key, resource> to the local Dictionary
     *
     * @param key      The key of the pair <key, resource> the user is trying to add to the dictionary
     * @param resource The resource of the pair <key, resource> the user is trying to add to the dictionary
     * @author Enrico Cestaro
     */
    public void addToLocalDictionary(String key, String resource) {

        localKademliaDictionary.add(new SMSSerialization(key), new SMSSerialization(resource));
    }

    /**
     * Method used to remove a pair <key, resource> from the local Dictionary
     *
     * @param key The key of the pair <key, resource> the user is trying to remove the dictionary
     * @author Enrico Cestaro
     */
    public void removeFromLocalDictionary(String key) {
        localKademliaDictionary.remove(new SMSSerialization(key));
    }

    /**
     * Method used to get a resource specified by its key
     *
     * @param key The key of the pair <key, resource> the user is trying to get from the dictionary
     * @return The SerializableObject with the specified key
     * @author Enrico Cestaro
     */
    public SerializableObject getFromLocalDictionary(String key) {
        return localKademliaDictionary.getResource(new SMSSerialization(key));
    }

    /**
     * Method used to update a resource with the specified key, with a new resource
     *
     * @param key      The key of the pair <key, resource> the user is trying to update in the dictionary
     * @param resource The new resource
     * @author Enrico Cestaro
     */
    public void updateLocalDictionary(String key, String resource) {
        localKademliaDictionary.update(new SMSSerialization(key), new SMSSerialization(resource));
    }
}
