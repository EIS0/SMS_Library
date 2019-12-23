package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;

/**
 * Iterator for the execution of the update-table algorithm.
 * I'm using an iterator because in the future where we have more KademliaNetwork to keep
 * track of, this iterator is specific for the single network, so we can have more than once
 * running on different tables at the same time
 *
 * @author Marco Cognolato
 */
public class TableUpdateIterator {
    private int currentCount;
    private int maxCount;
    private KademliaId netId;
    private SMSKademliaRoutingTable table;
    private SMSPeer netPeer;

    public TableUpdateIterator(int maxCount, KademliaId netId, SMSKademliaRoutingTable table, SMSPeer netPeer) {
        this.maxCount = maxCount;
        this.currentCount = 0;
        this.netId = netId;
        this.table = table;
        this.netPeer = netPeer;

        //step 1
        askForId();
    }

    /**
     * Steps execution of the algorithm once.
     *
     * @param receivedId The KademliaId received because of the last step
     */
    public void step(KademliaId receivedId) {
        if (hasFinished()) {
            //finished execution
            return;
        }
        //check if I'm the id received: stop if I am
        SMSKademliaNode receivedNode = new SMSKademliaNode(receivedId);
        if (receivedId == netId) {
            currentCount = maxCount + 1;
            return;
        }
        //check if the id received is already in the table: stop if it is
        if (table.getAllNodes().contains(receivedNode)) {
            currentCount = maxCount + 1;
            return;
        }
        //if I'm here I have a new node, add him to the table
        table.insert(receivedNode);
        //re-start algorithm for the next id
        currentCount++;

        askForId();
    }

    /**
     * @return Returns true if the algorithm has finished execution, false otherwise
     */
    public boolean hasFinished() {
        return currentCount >= maxCount;
    }

    /**
     * Generates an id to find and searches for it in the net sending a request
     */
    private void askForId() {
        //create the fake id
        KademliaId fakeId = netId.generateNodeIdByDistance(currentCount);
        //search in the net for the fakeId and wait
        IdFinderHandler.searchId(fakeId, netPeer, ResearchMode.JoinNetwork);
    }
}