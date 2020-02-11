package com.eis0.kademlianetwork;

import android.content.Context;
import android.util.Log;

import com.eis.communication.Peer;
import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.Contact;
import com.eis0.kademlia.DefaultConfiguration;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;
import com.eis0.kademlianetwork.activitystatus.NodeConnectionInfo;
import com.eis0.kademlianetwork.commands.messages.KadSendInvitation;
import com.eis0.kademlianetwork.commands.networkdictionary.FindResource;
import com.eis0.kademlianetwork.commands.networkdictionary.KadAddResource;
import com.eis0.kademlianetwork.commands.networkdictionary.KadDeleteResource;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;
import com.eis0.kademlianetwork.listener.SMSKademliaListener;
import com.eis0.kademlianetwork.routingtablemanager.RoutingTableRefresh;
import com.eis0.kademlianetwork.routingtablemanager.TableUpdateHandler;
import com.eis0.netinterfaces.NetDictionary;
import com.eis0.netinterfaces.NetworkManager;
import com.eis0.netinterfaces.commands.CommandExecutor;
import com.eis0.netinterfaces.listeners.GetResourceListener;
import com.eis0.netinterfaces.listeners.InviteListener;
import com.eis0.netinterfaces.listeners.RemoveResourceListener;
import com.eis0.netinterfaces.listeners.SetResourceListener;
import com.eis0.webdictionary.SMSNetVocabulary;

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
public class KademliaNetwork implements NetworkManager<String, String, SMSPeer, KademliaFailReason> {


    public final NodeConnectionInfo connectionInfo = new NodeConnectionInfo();
    public RoutingTableRefresh refresh;

    //Routing table for this user of the network
    protected SMSKademliaRoutingTable localRoutingTable;
    //User node of the network
    protected SMSKademliaNode localNode;
    //Dictionary containing the resources stored by the local node
    protected NetDictionary<String, String> localKademliaDictionary = new SMSNetVocabulary();
    protected final SMSKademliaListener smsKademliaListener = new SMSKademliaListener(this);
    protected RequestsHandler requestsHandler = new RequestsHandler();

    private final String LOG_KEY = "KAD_NET";

    /**
     * Initialize the network by setting the current node.
     *
     * @param localNode The SMSKademliaNode to set.
     * @author Matteo Carnelos
     */
    public void init(SMSKademliaNode localNode, Context context) {
        SMSManager.getInstance().setReceivedListener(smsKademliaListener.getClass(), context);
        this.localNode = localNode;
        localRoutingTable = new SMSKademliaRoutingTable(localNode, new DefaultConfiguration());
        refresh = new RoutingTableRefresh(this.localNode, this);
        refresh.start();
    }

    /**
     * Check if I received an acknowledge respond to my request.
     * (so if the node is alive)
     * If death, the {@link SMSKademliaNode} of the target peer is set as unresponsive
     *
     * @param targetPeer the receiver {@link SMSPeer}
     * @return true if alive, false otherwise
     * @author Edoardo Raimondi
     */
    public boolean isAlive(SMSPeer targetPeer) {
        //I wait 10 secs
        connectionInfo.run();
        //check if I had an acknowledge respond to my request
        if (connectionInfo.hasRespond()) {
            //I know my request has ben received successfully. I reset the connectionInfo
            connectionInfo.reset();
            return true;
        }
        connectionInfo.reset();
        setUnresponsive(targetPeer);
        //the node is not alive at the moment
        return false;
    }

    /**
     * Set a node has unresponsive
     *
     * @param peer that seems to be death
     * @author Edoardo Raimondi
     */
    private void setUnresponsive(SMSPeer peer) {
        //create the node by the peer
        KademliaId id = new KademliaId(peer);
        SMSKademliaNode unresponsive = new SMSKademliaNode(peer);
        //increment its stale count, it will be considered unresponsive
        this.localRoutingTable.getBuckets()[this.localRoutingTable.getBucketId(id)].getFromContacts(unresponsive).incrementStaleCount();
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
     *
     * @param node The node to find in the routing table
     * @return True if node is in the routing table, false otherwise
     * @author Marco Cognolato
     */
    public boolean isNodeInNetwork(SMSKademliaNode node) {
        Contact nodeContact = new Contact(node);
        return localRoutingTable.getAllContacts().contains(nodeContact);
    }

    /**
     * Adds a given valid SMSKademliaNode to the routing table of this network
     *
     * @param node The valid node to add to the net
     * @author Marco Cognolato
     */
    public void addNodeToTable(SMSKademliaNode node) {
        Contact nodeContact = new Contact(node);
        localRoutingTable.insert(nodeContact);
    }

    /**
     * Updates the routing table
     * @author Edoardo Raimondi
     */
    public void updateTable() {
        //calls the proper handler to update the routing table
        SMSPeer netPeer = localNode.getPeer();
        TableUpdateHandler.updateTable(localRoutingTable, localNode.getId(), netPeer, requestsHandler);
    }

    /**
     * @return Returns the local dictionary used by the network
     */
    public NetDictionary<String, String> getLocalDictionary(){
        return localKademliaDictionary;
    }

    /**
     * Saves a resource value in the network for the specified key. If the save is successful
     * {@link SetResourceListener#onResourceSet(Object, Object)} is be called.
     *
     * @param key                 The key identifier for the resource.
     * @param value               The identified value of the resource.
     * @param setResourceListener Listener called on resource successfully saved or on fail.
     */
    public void setResource(String key, String value, SetResourceListener<String, String, KademliaFailReason> setResourceListener) {
        try{
            CommandExecutor.execute(new KadAddResource(key, value, requestsHandler));
        }
        catch (Exception e){
            Log.e(LOG_KEY, e.toString());
            setResourceListener.onResourceSetFail(key, value, KademliaFailReason.GENERIC_FAIL);
            return;
        }
        setResourceListener.onResourceSet(key, value);
    }

    /**
     * Retrieves a resource value from the network for the specified key. The value is returned inside
     * {@link GetResourceListener#onGetResource(Object, Object)}.
     *
     * @param key                 The key identifier for the resource.
     * @param getResourceListener Listener called on resource successfully retrieved or on fail.
     */
    public void getResource(String key, GetResourceListener<String, String, KademliaFailReason> getResourceListener) {
        FindResource resourceCommand;
        try {
            resourceCommand = new FindResource(key, requestsHandler);
            CommandExecutor.execute(resourceCommand);
        }
        catch (Exception e){
            Log.e(LOG_KEY, e.toString());
            getResourceListener.onGetResourceFailed(key, KademliaFailReason.GENERIC_FAIL);
            return;
        }

        if(!resourceCommand.hasSuccessfullyCompleted()){
            getResourceListener.onGetResourceFailed(key, resourceCommand.getFailReason());
            return;
        }
        getResourceListener.onGetResource(key, resourceCommand.getResource());
    }

    /**
     * Removes a resource value from the network for the specified key. If the removal is successful
     * {@link RemoveResourceListener#onResourceRemoved(Object)} is called
     *
     * @param key                    The key identifier for the resource.
     * @param removeResourceListener Listener called on resource successfully removed or on fail.
     */
    public void removeResource(String key, RemoveResourceListener<String, KademliaFailReason> removeResourceListener) {
        try{
            CommandExecutor.execute(new KadDeleteResource(key, requestsHandler));
        }
        catch (Exception e){
            Log.e(LOG_KEY, e.toString());
            removeResourceListener.onResourceRemoveFail(key, KademliaFailReason.GENERIC_FAIL);
            return;
        }
        removeResourceListener.onResourceRemoved(key);
    }

    /**
     * Invites another user to join the network. If the invitation is sent correctly
     * {@link InviteListener#onInvitationSent(Peer)} will be called
     *
     * @param peer           The address of the user to invite to join the network.
     * @param inviteListener Listener called on user invited or on fail.
     * @author Marco Cognolato
     */
    public void invite(SMSPeer peer, InviteListener<SMSPeer, KademliaFailReason> inviteListener) {
        //don't invite a peer if it's already in the net
        if (isNodeInNetwork(new SMSKademliaNode(peer))){
            inviteListener.onInvitationNotSent(peer, KademliaFailReason.PEER_IN_NET);
            return;
        }

        try {
            KademliaInvitation invitation = new KademliaInvitation(peer);
            CommandExecutor.execute(new KadSendInvitation(invitation));
        } catch (Exception e) {
            Log.e(LOG_KEY, e.toString());
            inviteListener.onInvitationNotSent(peer, KademliaFailReason.GENERIC_FAIL);
            return;
        }
        inviteListener.onInvitationSent(peer);
    }

    /**
     * Returns the network valid instance of the RequestsHandler
     */
    public RequestsHandler getRequestsHandler(){
        return requestsHandler;
    }
}
