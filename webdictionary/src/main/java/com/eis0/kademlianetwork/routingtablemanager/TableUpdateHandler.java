package com.eis0.kademlianetwork.routingtablemanager;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaRoutingTable;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;

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
    public static void updateTable(@NonNull SMSKademliaRoutingTable table,
                                   @NonNull KademliaId netId,
                                   @NonNull SMSPeer netPeer,
                                   @NonNull RequestsHandler requestsHandler) {
        iterator = new TableUpdateIterator(KademliaId.ID_LENGTH, netId, table, netPeer, requestsHandler);
        iterator.run();
    }

}
