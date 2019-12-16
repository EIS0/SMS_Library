package com.eis0.kademlianetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.math.BigInteger;

/**
 * Class used to find the closest ID to the network to a specific Id
 *
 * @author Marco Cognolato (wrote the class)
 * @author Enrico Cestaro (edited the code, to implement multiple research modes)
 */
public class IdFinderHandler {
    //TODO: can be converted in an Object Pool?
    /**
     * Searches for a specific Id, which needs to be closer to a given resource Id.
     * If it's found it's sent to a given SMSPeer who's searching, else sends a request to find it
     * to a closer person than the actual node. By a chain process, if the node is not found
     * it's sent to searcher the closest id found on the net
     *
     * @param idToFind     The {@link KademliaId} to find
     * @param searcher     The {@link SMSPeer} who's searching the id
     * @param researchMode The {@link ResearchMode} enum, represents the final purpose of the
     *                     research
     * @throws IllegalArgumentException If the idToFind, the searcher or the researchMode are null
     */
    public static void searchId(KademliaId idToFind, SMSPeer searcher, ResearchMode researchMode) {
        if (searcher == null || idToFind == null || researchMode == null)
            throw new IllegalArgumentException();
        /*Declaration of the two messages used to:
        1. entrust the research to a close node
        2. communicate the result of the research, or to
         */
        RequestTypes findId = null;
        RequestTypes taskResult = null;
        //Selection of the research mode
        switch (researchMode) {
            case JoinNetwork:
                findId = RequestTypes.FindId;
                taskResult = RequestTypes.SearchResult;
            case AddToDictionary:
                findId = RequestTypes.FindIdForAddRequest;
                taskResult = RequestTypes.AddRequestResult;
                break;
            case FindInDictionary:
                findId = null;
                taskResult = null;
                break;
        }
        KademliaId netId = KademliaNetwork.getInstance().getLocalNode().getId();
        //If I'm the id return it
        //N.B. this state should be impossible, so it's a fail safe
        if (netId == idToFind) {
            String message = taskResult.ordinal() + " " + idToFind;
            SMSMessage searchResult = new SMSMessage(searcher, message);
            SMSManager.getInstance().sendMessage(searchResult);
            KademliaNetwork.getInstance().checkIfAlive(searcher);
            return;
        }

        SMSKademliaNode nodeToFind = new SMSKademliaNode(idToFind);
        if (KademliaNetwork.getInstance().isNodeInNetwork(nodeToFind)) {
            String message = taskResult.ordinal() + " " + idToFind;
            SMSMessage searchResult = new SMSMessage(searcher, message);
            SMSManager.getInstance().sendMessage(searchResult);
            KademliaNetwork.getInstance().checkIfAlive(searcher);
            return;
        }

        SMSKademliaRoutingTable table = KademliaNetwork.getInstance().getLocalRoutingTable();
        SMSKademliaNode closestNode = table.findClosest(idToFind, 1).get(0);
        BigInteger idToFindDistanceFromNetId = idToFind.getXorDistance(netId);
        BigInteger idToFindDistanceFromClosest = idToFind.getXorDistance(closestNode.getId());
        if (idToFindDistanceFromClosest.compareTo(idToFindDistanceFromNetId) > 0) {
            //I got further away from what I'm looking for, so I'm the closest,
            //so I return this id
            String message = taskResult.ordinal() + " " + idToFind;
            SMSMessage searchResult = new SMSMessage(searcher, message);
            SMSManager.getInstance().sendMessage(searchResult);
            KademliaNetwork.getInstance().checkIfAlive(searcher);
        } else {
            //I got closer to what I'm looking for, so I ask that id to find it.
            SMSPeer closer = closestNode.getPeer();
            String message = findId.ordinal() + " " + idToFind + " " + searcher;
            SMSMessage addRequestMessage = new SMSMessage(closer, message);
            SMSManager.getInstance().sendMessage(addRequestMessage);
            KademliaNetwork.getInstance().checkIfAlive(closer);
        }
    }
}
