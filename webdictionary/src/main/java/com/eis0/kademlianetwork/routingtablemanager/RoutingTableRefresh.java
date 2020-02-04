package com.eis0.kademlianetwork.routingtablemanager;


import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.Contact;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaBucket;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.activitystatus.RespondTimer;
import com.eis0.kademlianetwork.informationdeliverymanager.IdFinderHandler;
import com.eis0.kademlianetwork.KademliaNetwork;
import com.eis0.kademlianetwork.informationdeliverymanager.ResearchMode;

import java.util.List;


/**
 * Perform a local routing table refresh.
 * All nodes over the max stale count define in the configuration are removed.
 *
 * @author Edoardo Raimondi
 */

public class RoutingTableRefresh{
    //create a timer to verify if I had a pong in at least 10 secs
    private final RespondTimer timer = new RespondTimer();
    //node doing this refresh
    private final SMSKademliaNode localNode;
    //kademlia network of the local node
    private final KademliaNetwork net;

    public RoutingTableRefresh(SMSKademliaNode node, KademliaNetwork net){
        localNode = node;
        this.net = net;
    }

    /**
     * Method that performs a refresh
     */
    public void start() {
        //create the list of my routing table nodes. I need to check all that nodes.
        List<SMSKademliaNode> allRoutingTableNodes = net.getLocalRoutingTable().getAllNodes();
        for (int i = 0; i < allRoutingTableNodes.size(); i++) {
            SMSKademliaNode currentNode = allRoutingTableNodes.get(i);
            if(removeIfUnresponsive(currentNode)){
                //I removed that node. Search for another one
                askForId(currentNode.getId());
            }
        }
    }

    /**
     * Setting a contact as unresponsive
     *
     * @param node Contact node
     * @return true if the node has been correctly removed
     */
    private boolean removeIfUnresponsive(SMSKademliaNode node){
        KademliaId currentId = node.getId();
        //I check the bucket Id that contains that node.
        int b = net.getLocalRoutingTable().getBucketId(currentId);
        //I extract the bucket
        SMSKademliaBucket currentBucket = net.getLocalRoutingTable().getBuckets()[b];
        //I extract the contact
        Contact currentContact = currentBucket.getFromContacts(node);
        //If the contact has been stale more than two times, i remove it
        if(currentContact.staleCount()> net.getLocalRoutingTable().getConfig().stale()){
            currentBucket.removeContact(currentContact);
            return true;
        }
        else { //let's update I've seen it. I need to remove and re-add the node to get the sort order
            currentBucket.removeContact(currentContact);
            currentContact.setSeenNow();
            currentContact.resetStaleCount();
            currentBucket.insert(currentContact);
            return false;
        }
    }

    /**
     * Generates an id to find and searches for it in the net sending a request
     *
     * @param id the id to replace
     */
    private void askForId(KademliaId id) {
        //take the node peer
        SMSPeer peer = localNode.getPeer();
        //create the fake id. I want a node in the same bucket so I search for a same distance one
        KademliaId fakeId = id.generateNodeIdByDistance(0);

        //search in the net for the fakeId and wait
        IdFinderHandler.searchId(fakeId, peer, ResearchMode.Refresh);
    }
}