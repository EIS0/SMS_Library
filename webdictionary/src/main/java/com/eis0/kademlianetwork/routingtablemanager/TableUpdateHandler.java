package com.eis0.kademlianetwork.routingtablemanager;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaRoutingTable;

/**
 * Class responsible for updating a routing table
 *
 * @author Marco Cognolato
 */
public class TableUpdateHandler {

    private static TableUpdateIterator iterator;

    /**
     * Starts the update process using the routing table
     *
     * @param table The Routing Table of the net to update
     * @param netId The id of the owner of this network
     */
    public static void updateTable(SMSKademliaRoutingTable table, KademliaId netId, SMSPeer netPeer) {
        iterator = new TableUpdateIterator(KademliaId.ID_LENGTH, netId, table, netPeer);
    }

    /**
     * Function called to single step execution of the update table algorithm, instead
     * of sending ID_LENGTH messages, sends one, than waits for the user,
     * then sends another, then waits etc. This way if the algorithms instantly
     * stops, I haven't yet to send all the messages.
     *
     * @param peerFound The SMSPeer received as part of the algorithm.
     */
    public static void stepTableUpdate(SMSPeer peerFound) {
        iterator.step(peerFound);
    }
}
