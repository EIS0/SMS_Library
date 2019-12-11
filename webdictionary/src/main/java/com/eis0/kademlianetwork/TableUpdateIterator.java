package com.eis0.kademlianetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;

/**
 * Iterator for the execution of the update-table algorithm.
 * I'm using an iterator because in the future where we have more KademliaNetwork to keep
 * track of, this iterator is specific for the single network.
 */
public class TableUpdateIterator {
    private int currentCount;
    private int maxCount;
    private KademliaId netId;
    SMSKademliaRoutingTable table;

    public TableUpdateIterator(int maxCount, KademliaId netId, SMSKademliaRoutingTable table){
        this.maxCount = maxCount;
        this.currentCount = 0;
        this.netId = netId;
        this.table = table;

        //step 1
        askForId();
    }

    /**
     * Steps execution of the algorithm once.
     * @param receivedNode The SMSKademliaNode received because of the last step
     * @return Returns true if the algorithm has finished
     * execution, and updated the net, false otherwise
     */
    public void step(SMSKademliaNode receivedNode){
        if(currentCount > maxCount) {
            //finished execution
            return;
        }
        //check if I'm the id received: stop if I am
        KademliaId receivedId = receivedNode.getId();
        if(receivedId == netId) return; //stop or whatever
        //check if the id received is already in the table: stop if it is
        if(table.getAllNodes().contains(receivedNode)) return; //stop or whatever
        //if I'm here I have a new node, add him to the table
        table.insert(receivedNode);
        //re-start algorithm for the next id
        currentCount++;

        askForId();
    }

    private void askForId(){
        //create the fake id
        KademliaId fakeId = netId.generateNodeIdByDistance(currentCount);
        //search in the net for the fakeId and wait
        //TODO: add this ^
    }
}
