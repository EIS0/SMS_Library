package com.eis0.kademlianetwork.routingtablemanager;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;
import com.eis0.kademlianetwork.commands.KadFindId;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;
import com.eis0.netinterfaces.commands.CommandExecutor;

/**
 * Iterator for the execution of the update-table algorithm.
 * I'm using an iterator because in the future where we have more KademliaNetwork to keep
 * track of, this iterator is specific for the single network, so we can have more than once
 * running on different tables at the same time
 *
 * @author Marco Cognolato
 */
class TableUpdateIterator extends Thread {
    private int currentCount;
    private int maxCount;
    private KademliaId netId;
    private SMSKademliaRoutingTable table;
    private SMSPeer netPeer;
    private RequestsHandler requestsHandler;

    /**
     * Constructs an Iterator object which is used to update a given table.
     *
     * @param maxCount How many elements should the table update
     * @param netId    Id of the owner of the table
     * @param table    The RoutingTable to update
     * @param netPeer  The SMSPeer which is asking to update the table (used as part of the algorithm)
     * @throws IllegalArgumentException if maxCount is <= 0 or if any of the input parameters is null
     */
    public TableUpdateIterator(int maxCount,
                               @NonNull KademliaId netId,
                               @NonNull SMSKademliaRoutingTable table,
                               @NonNull SMSPeer netPeer,
                               @NonNull RequestsHandler requestsHandler) {
        if (maxCount <= 0) throw new IllegalArgumentException("maxCount has to be at least 1!");
        this.maxCount = maxCount;
        this.currentCount = 0;
        this.netId = netId;
        this.table = table;
        this.netPeer = netPeer;
        this.requestsHandler = requestsHandler;
    }

    public void run() {
        while (!hasFinished()) {
            //create the fake id
            KademliaId fakeId = netId.generateNodeIdByDistance(currentCount);
            KadFindId findIdCommand = new KadFindId(fakeId, requestsHandler);
            CommandExecutor.execute(findIdCommand);
            if (!findIdCommand.hasSuccessfullyCompleted()) {
                return;
            }
            //If I'm here it means I've found a peer
            SMSPeer peerReceived = findIdCommand.getPeerFound();

            //check if I'm the id received: stop if I am
            SMSKademliaNode receivedNode = new SMSKademliaNode(peerReceived);
            if (receivedNode.getId() == netId) {
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
        }
    }

    /**
     * @return Returns true if the algorithm has finished execution, false otherwise
     */
    private boolean hasFinished() {
        return currentCount >= maxCount;
    }

}