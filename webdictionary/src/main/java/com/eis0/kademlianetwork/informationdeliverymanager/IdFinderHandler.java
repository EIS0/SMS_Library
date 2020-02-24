package com.eis0.kademlianetwork.informationdeliverymanager;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;
import com.eis0.kademlianetwork.KademliaJoinableNetwork;

/**
 * This Class is used to find the closest node ID to a specific resource ID inside the network
 *
 * @author Marco Cognolato (wrote the class)
 * @author Enrico Cestaro (edited and restored the code, to implement multiple research modes)
 * @author Edoardo Raimondi (added the check cicle to verify if the target node is 'alive')
 * @author Giovanni Velludo (removed an useless variable)
 */
public class IdFinderHandler {
    private static final String ID_TO_FIND_NULL = "The idToFind parameter is null";
    private static final String SEARCHER_NULL = "The searcher parameter is null";

    /**
     * Searches for a specific Id, which needs to be closer to a given resource Id.
     * If found, it's sent the searching Peer, else sends a request to find it
     * to a closer person than the actual node. So on by a chain process
     *
     * @param idToFind The ID to find in the network
     * @param searcher The Peer which is searching the ID
     * @throws IllegalArgumentException If the idToFind, the searcher or the researchMode are null
     */
    public static void searchId(@NonNull KademliaId idToFind, @NonNull SMSPeer searcher) {
        if (idToFind == null) throw new NullPointerException("The idToFind cannot be Null");
        if (searcher == null) throw new NullPointerException("The searcher cannot be Null");
        /*
        Declaration of the two messages used to:
        1. entrust the research to a closer node
        2. communicate the result of the research to the originating node
        */

        //Obtain the local RoutingTable
        SMSKademliaRoutingTable table = KademliaJoinableNetwork.getInstance().getLocalRoutingTable();
        //Get the node with the node ID closest to the idToFind
        SMSKademliaNode closestNode = table.findClosest(idToFind, 1).get(0);

        // Checks if I'm closest node to the one to find
        if (closestNode.equals(KademliaJoinableNetwork.getInstance().getLocalNode())) {
            sendResult(idToFind, searcher);
            return;
        }
        //else, I ask to the closest node inside my Routing Table to continue the research
        SMSPeer closer = closestNode.getPeer();
        keepLooking(idToFind, searcher, closer);
        retryIfDead(idToFind, searcher, closer);
    }

    /**
     * This method sends to the specified target {@link SMSPeer} the result of the previously
     * carried out research
     *
     * @param idToFind   The ID whose research originated the request
     *                   It's sent back as response to the research
     * @param targetPeer The Peer representing the target that will receive the result
     */
    private static void sendResult(KademliaId idToFind, SMSPeer targetPeer) {
        SMSMessage searchResult = new KademliaMessage()
                .setPeer(targetPeer)
                .setRequestType(RequestTypes.FindIdSearchResult)
                .setIdToFind(idToFind)
                .buildMessage();
        if (targetPeer.equals(KademliaJoinableNetwork.getInstance().getLocalNode().getPeer())) {
            //If I'm searching the id for myself, then I have a pending request I can directly fulfill
            KademliaJoinableNetwork.getInstance().getRequestsHandler().completeFindIdRequest(idToFind, targetPeer);
            return;
        }

        SMSManager.getInstance().sendMessage(searchResult);
    }

    /**
     * This method is called if the local node couldn't find the searched ID. It sends a request to
     * the node inside its RoutingTable with the node ID closest to the resource ID, and ask it to
     * continue the research; if the closerNode find the searched ID, it sends it directly to the
     * node which originated the research, without retracing back all the node that took part to the
     * research
     *
     * @param idToFind     The ID whose research originated the request
     *                     It's sent back as response to the research
     * @param searcherNode The Peer which started the research
     * @param closerNode   The node with the {@link KademliaId} closer to the idToFind
     */
    private static void keepLooking(KademliaId idToFind, SMSPeer searcherNode, SMSPeer closerNode) {
        SMSMessage requestMessage = new KademliaMessage()
                .setPeer(closerNode)
                .setRequestType(RequestTypes.FindId)
                .setIdToFind(idToFind)
                .setSearcher(searcherNode)
                .buildMessage();
        SMSManager.getInstance().sendMessage(requestMessage);
    }

    /**
     * This method verify if the nodeToCheck is still valid. If the node returns any sign of life
     * the process ends, otherwise the research for the ID restarted
     *
     * @param idToFind     The ID to find inside the network
     * @param searcherNode The Peer which is searching the ID
     * @param nodeToCheck  The Peer whose validity must be checked; if it's not active
     *                     anymore, it is removed from the RoutingTable, and the research is started
     *                     again
     */
    private static void retryIfDead(KademliaId idToFind, SMSPeer searcherNode, SMSPeer nodeToCheck) {
        if (!KademliaJoinableNetwork.getInstance().isAlive(nodeToCheck)) {

            searchId(idToFind, searcherNode);
        }
    }
}
