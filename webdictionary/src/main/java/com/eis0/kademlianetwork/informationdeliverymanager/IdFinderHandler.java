package com.eis0.kademlianetwork.informationdeliverymanager;

import androidx.annotation.NonNull;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;
import com.eis0.kademlianetwork.KademliaJoinableNetwork;

import java.util.List;

/**
 * This class is used to scan the RoutingTable, searching for the {@link SMSKademliaNode} with the
 * {@link KademliaId} closest to the idToFind (the Resource Id specified in the searchId() call); if
 * the local node is the closest one it will answer to the searching {@link SMSPeer}, if the closest
 * node is another node inside of the RoutingTable the local node will pass the task to him (it sends
 * a SMS to the node, and waits to know if it is still alive inside of the network).
 * <p>
 * The searchId() is a wrapper of the searchIdList() method.
 * <p>
 * It creates a list of nodes (number of nodes = ATTEMPTS), starting from the closest one; if a node
 * doesn't respond in 10s the method removes it from the list of closest nodes and restarts the
 * process with the new list, asking to the following one, and so on, as long as the list ends
 * or the closest node is the local node. The list length corresponds to the ATTEMPTS value (or to
 * ATTEMPTS+1, if the list doesn't contain the local node; in this scenario, the local node is added
 * at the end of the list, as last possible node that will answer to the research).
 * The nodes are removed only from the list created by the searchIdList, not from the RoutingTable,
 * to avoid the direct erasure.
 * <p>
 * When a node doesn't respond its staleCount get incremented, in the Refresh phase it will be
 * removed if still inactive.
 *
 * @author Marco Cognolato (created the class)
 * @author Enrico Cestaro (edited and restored the code)
 * @author Edoardo Raimondi (added the check cicle to verify if the target node is 'alive')
 */
public class IdFinderHandler {
    private static int maxAttempts = 10;
    private static final String ID_TO_FIND_NULL = "The idToFind cannot be null";
    private static final String SEARCHER_NULL = "The searcher cannot be null";
    private static final String NODES_LIST_NULL = "The nodeList cannot be null";
    private static final String NODES_LIST_EMPTY = "The nodeList cannot be empty";
    private static final String INSUFFICIENT_ATTEMPTS = "The attemptsNumber must be higher then zero";

    /**
     * Searches for a specific Id, which needs to be closer to a given resource Id.
     * The list of possible nodes is formed by the closest nodes, and it always contains the local
     * node (if it doesn't, the node is automatically added)
     *
     * @param idToFind The ID to find in the network
     * @param searcher The Peer which is searching for the ID
     * @throws NullPointerException If the idToFind, the searcher or the researchMode are null
     */
    public static void searchId(@NonNull KademliaId idToFind, @NonNull SMSPeer searcher) {
        if (idToFind == null) throw new NullPointerException(ID_TO_FIND_NULL);
        if (searcher == null) throw new NullPointerException(SEARCHER_NULL);
        //Obtain the local RoutingTable
        SMSKademliaRoutingTable table = KademliaJoinableNetwork.getInstance().getLocalRoutingTable();
        //Create a list containing the closest nodes (five is more than enough)
        List<SMSKademliaNode> nodeList = table.findClosest(idToFind, maxAttempts);
        //Local node, inserted in the list if not already present
        SMSKademliaNode local = KademliaJoinableNetwork.getInstance().getLocalNode();
        if (!nodeList.contains(local)) {
            //inserted in the last position, must be the last node to be checked
            nodeList.add(local);
        }
        searchIdList(idToFind, searcher, nodeList);
    }

    /**
     * Searches inside of the given list for the active closest node; when a node doesn't respond it
     * gets removed from the list, and the process is repeated
     *
     * @param idToFind The ID to find in the network
     * @param searcher The peer which is searching for the ID
     * @param nodeList
     */
    public static void searchIdList(@NonNull KademliaId idToFind, @NonNull SMSPeer searcher,
                                    @NonNull List<SMSKademliaNode> nodeList) {
        if (nodeList.isEmpty()) throw new IllegalArgumentException(NODES_LIST_EMPTY);
        if (nodeList == null) throw new NullPointerException(NODES_LIST_NULL);
        //Get the node with the node ID closest to the idToFind
        //The first node in the nodeList is the one with the node Id closest to the idToFind
        SMSKademliaNode closestNode = nodeList.get(0);

        // Checks if I'm closest node to the one to find
        if (closestNode.equals(KademliaJoinableNetwork.getInstance().getLocalNode())) {
            sendResult(idToFind, searcher);
            return;
        }
        //else, I ask to the closest node inside my Routing Table to continue the research
        SMSPeer closer = closestNode.getPeer();
        keepLooking(idToFind, searcher, closer);
        retryIfDead(idToFind, searcher, closer, nodeList);
    }

    /**
     * This method sends to the specified target {@link SMSPeer} the result of the previously
     * carried out research
     *
     * @param idToFind   The ID whose research originated the request;
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
            KademliaJoinableNetwork.getInstance().getRequestsHandler().
                    completeFindIdRequest(idToFind, targetPeer);
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
    private static void retryIfDead(KademliaId idToFind, SMSPeer searcherNode, SMSPeer nodeToCheck,
                                    List<SMSKademliaNode> nodeList) {
        if (!KademliaJoinableNetwork.getInstance().isAlive(nodeToCheck)) {
            //The first node is the one tested, if I'm here it failed.
            //I remove it and restart the process
            nodeList.remove(0);
            //Recursive call
            searchIdList(idToFind, searcherNode, nodeList);
        }
    }

    /**
     * The number of maximum attempts for the research of the node in the RoutingTable is by default
     * set to 10; the reason for the existence of this value is to assure that, if the node can't
     * reach a large number of nodes, it can still insert the resource inside of the Network; the
     * resource will be then relocated during the refresh, IF the unresponsive node returns to live.
     * If it doesn't, then the node chosen as closest one remains the closest one.
     * It must also be considered the time that each attempts takes (10s)
     * Warning: when the application is closed the number of attempts is reset
     *
     * @param attemptsNumber The maximum number of attempts before saving the resource in the local
     *                       dictionary
     */
    public static void setAttemptsNumber(int attemptsNumber) {
        if (attemptsNumber < 1) throw new IllegalArgumentException(INSUFFICIENT_ATTEMPTS);
        maxAttempts = attemptsNumber;
    }
}
