package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;

import java.math.BigInteger;

/**
 * This Class is used to find the closest node ID to a specific resource ID inside the network
 *
 * @author Marco Cognolato (wrote the class)
 * @author Enrico Cestaro (edited and restored the code, to implement multiple research modes)
 * @author Edoardo Raimondi (added the check cicle to verify if the target node is 'alive')
 */
public class IdFinderHandler {
    /**
     * Searches for a specific Id, which needs to be closer to a given resource Id.
     * If it's found it's sent to a given SMSPeer who's searching, else sends a request to find it
     * to a closer person than the actual node. By a chain process, if the node is not found
     * it's sent to searcher the closest id found on the net
     *
     * @param idToFind     The ID to find in the network
     * @param searcher     The Peer which is searching the ID
     * @param researchMode The research mode, represents the final purpose of the research
     * @throws IllegalArgumentException If the idToFind, the searcher or the researchMode are null
     */
    public static void searchId(KademliaId idToFind, SMSPeer searcher, ResearchMode researchMode) {
        if (searcher == null || idToFind == null || researchMode == null)
            throw new IllegalArgumentException();
        /*Declaration of the two messages used to:
        1. entrust the research to a closer node
        2. communicate the result of the research to the originating node
         */
        RequestTypes findId = null;
        RequestTypes taskResult = null;
        /*Selection of the research mode: the ID research in the network can be originated for
        different tasks, the researchMode specifies the target that the node which originated the
        research was trying to accomplish */
        switch (researchMode) {
            case JoinNetwork:
                findId = RequestTypes.FindId;
                taskResult = RequestTypes.SearchResult;
            case AddToDictionary:
                findId = RequestTypes.FindIdForAddRequest;
                taskResult = RequestTypes.ResultAddRequest;
                break;
            case FindInDictionary:
                findId = RequestTypes.FindIdForGetRequest;
                taskResult = RequestTypes.ResultGetRequest;
                break;
            case RemoveFromDictionary:
                findId = RequestTypes.FindIdForDeleteRequest;
                taskResult = RequestTypes.ResultDeleteRequest;
            case Refresh:
                findId = RequestTypes.FindId;
                taskResult = RequestTypes.SearchResultReplacement;
        }

        //Obtain the KademliaId belonging to the local node (myself)
        KademliaId netId = KademliaNetwork.getInstance().getLocalNode().getId();
        //Obtain the local RoutingTable
        SMSKademliaRoutingTable table = KademliaNetwork.getInstance().getLocalRoutingTable();
        //Get the node with the node ID closest to the idToFind
        SMSKademliaNode closestNode = table.findClosest(idToFind, 1).get(0);
        SMSKademliaNode nodeToFind = new SMSKademliaNode(idToFind);

        BigInteger idToFindDistanceFromNetId = idToFind.getXorDistance(netId);
        BigInteger idToFindDistanceFromClosest = idToFind.getXorDistance(closestNode.getId());

        //1. Checking if I'm the searched id (This state should be impossible, it's a fail safe)
        //2. Checking if inside my RoutingTable there is a node with the ID to find
        //3. I got further away from what I'm looking for, so I'm the closest one: I return this ID
        if (netId == idToFind ||
                KademliaNetwork.getInstance().isNodeInNetwork(nodeToFind) ||
                idToFindDistanceFromClosest.compareTo(idToFindDistanceFromNetId) > 0) {
            sendResult(taskResult, idToFind, searcher);
            retryIfDead(idToFind, searcher, researchMode, searcher);
            return;
        }
        //else
        //I ask to the closest node inside my Routing Table to continue the research
        SMSPeer closer = closestNode.getPeer();
        keepLooking(findId, idToFind, searcher, closer);
        retryIfDead(idToFind, searcher, researchMode, closer);
    }


    /**
     * This method sends to the specified target {@link SMSPeer} the result of the previously
     * carried out research
     *
     * @param requestType The type of the result, sent to allow the receiver of the
     *                    result to define which request the result itself belongs to
     *                    (There may be multiple pending requests, each of a different nature)
     * @param idToFind    The ID whose research originated the request
     *                    It's sent back as response to the research
     * @param targetPeer  The Peer representing the target that will receive the result
     */
    private static void sendResult(RequestTypes requestType, KademliaId idToFind, SMSPeer targetPeer) {
        KademliaMessage kadMessage = new KademliaMessage(requestType, idToFind, null, null, null);
        SMSMessage searchResult = new SMSMessage(targetPeer, kadMessage.toString());
        SMSManager.getInstance().sendMessage(searchResult);
    }

    /**
     * This method is called if the local node couldn't find the searched ID, it sends a request to
     * the node inside its RoutingTable with the node ID closest to the resource ID, and ask it to
     * continue the research; if the closerNode find the searched ID, it sends it directly to the
     * node which originated the research, without retracing back all the node that took part to the
     * research
     *
     * @param requestType  The type of research, there are multiple processes
     *                     that need to search for a specific ID, this value allows the receiver of
     *                     the message to define which process asked for the ID, and to answer
     *                     appropriately
     * @param idToFind     The ID whose research originated the request
     *                     It's sent back as response to the research
     * @param searcherNode The Peer which started the research
     * @param closerNode   The node with the {@link KademliaId} closer to the idToFind
     */
    private static void keepLooking(RequestTypes requestType, KademliaId idToFind, SMSPeer searcherNode, SMSPeer closerNode) {
        KademliaMessage kadMessage = new KademliaMessage(requestType, idToFind, searcherNode, null, null);
        SMSMessage requestMessage = new SMSMessage(closerNode, kadMessage.toString());
        SMSManager.getInstance().sendMessage(requestMessage);
    }

    /**
     * This method verify if the nodeToCheck is still valid, if the node returns any sign of life
     * the process ends, otherwise the research for the ID is restarted
     *
     * @param idToFind     The ID to find inside the network
     * @param searcherNode The Peer which is searching the ID
     * @param researchMode The research mode, represents the final purpose of the research
     * @param nodeToCheck  The Peer whose validity must be checked; if it's not active
     *                     anymore, it is removed from the RoutingTable, and the research is started
     *                     again
     */
    private static void retryIfDead(KademliaId idToFind, SMSPeer searcherNode, ResearchMode researchMode, SMSPeer nodeToCheck) {
        if (!KademliaNetwork.getInstance().isAlive(nodeToCheck)) {
            //The target node is not alive. It is no more in my routing table
            //I try with another one, starting another ID research, with the same values, but now
            //that I executed the .isAlive(nodeToCheck) method, the invalid node it's been removed
            //from my routing table
            searchId(idToFind, searcherNode, researchMode);
        }
    }


}
