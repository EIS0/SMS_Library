package com.eis0.kademlianetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;
import com.eis0.smslibrary.SMSPeer;

public class TableUpdateHandler {

    private static TableUpdateIterator iterator;
    /**
     * Starts the update process using the routing table
     *
     * @param table The Routing Table of the net to update
     * @param netId The id of the owner of this network
     */
    public static void updateTable(SMSKademliaRoutingTable table, KademliaId netId){
        iterator = new TableUpdateIterator(KademliaId.ID_LENGTH, netId, table);
    }

    /**
     * Function called to single step execution of the update table algorithm, instead
     * of sending ID_LENGTH messages, sends one, than waits for the user,
     * then sends another, then waits etc. This way if the algorithms instantly
     * stops, I haven't yet to send all the messages.
     *
     * @param peer The SMSPeer received as part of the algorithm.
     */
    public static void stepTableUpdate(SMSPeer peer){
        SMSKademliaNode nodeReceived = new SMSKademliaNode(peer);
        iterator.step(nodeReceived);
    }
}
