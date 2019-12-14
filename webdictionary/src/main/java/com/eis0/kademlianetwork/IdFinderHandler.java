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
 */
public class IdFinderHandler {

    /**
     * Searches for a specific Id, if it's found it's sent to a given
     * SMSPeer who's searching, else sends a request to find it to a
     * closer person than me. By a process chain, if the node is not found
     * it's sent to searcher the closest id found on the net
     * @param idToFind The id to find
     * @param searcher The SMSPeer who's searching the id
     */
    public static void searchId(KademliaId idToFind, SMSPeer searcher){
        KademliaId netId = KademliaNetwork.getInstance().getLocalNode().getId();
        //If I'm the id return it
        //N.B. this state should be impossible, so it's a fail safe
        if(netId == idToFind){
            String message = RequestTypes.SearchResult.ordinal() + " " + idToFind;
            SMSMessage searchResult = new SMSMessage(searcher, message);
            SMSManager.getInstance().sendMessage(searchResult);
            return;
        }

        SMSKademliaNode nodeToFind = new SMSKademliaNode(idToFind);
        if(KademliaNetwork.getInstance().isNodeInNetwork(nodeToFind)){
            String message = RequestTypes.SearchResult.ordinal() + " " + idToFind;
            SMSMessage searchResult = new SMSMessage(searcher, message);
            SMSManager.getInstance().sendMessage(searchResult);
            return;
        }

        SMSKademliaRoutingTable table = KademliaNetwork.getInstance().getLocalRoutingTable();
        SMSKademliaNode closestNode = table.findClosest(idToFind,1).get(0);
        BigInteger idToFindDistanceFromNetId = idToFind.getXorDistance(netId);
        BigInteger idToFindDistanceFromClosest = idToFind.getXorDistance(closestNode.getId());
        if(idToFindDistanceFromClosest.compareTo(idToFindDistanceFromNetId) > 0){
            //I got further away from what I'm looking for, so I'm the closest,
            //so I return this id
            String message = RequestTypes.SearchResult.ordinal() + " " + netId;
            SMSMessage searchResult = new SMSMessage(searcher, message);
            SMSManager.getInstance().sendMessage(searchResult);
        }
        else{
            //I got closer to what I'm looking for, so I ask that id to find it.
            SMSPeer closer = closestNode.getPeer();
            String message = RequestTypes.FindId.ordinal() + " " + idToFind + " " + searcher;
            SMSMessage messageToSend = new SMSMessage(closer, message);
            SMSManager.getInstance().sendMessage(messageToSend);
        }
    }

    /**
     * Searches for a specific Id, which needs to be closer to a given resource Id.
     * If it's found it's sent to a given SMSPeer who's searching, else sends a request to find it
     * to a closer person than me. By a process chain, if the node is not found
     * it's sent to searcher the closest id found on the net
     * @param idToFind The id to find
     * @param searcher The SMSPeer who's searching the id
     */
    public static void searchIdForAddRequest(KademliaId idToFind, SMSPeer searcher){
        KademliaId netId = KademliaNetwork.getInstance().getLocalNode().getId();
        //If I'm the id return it
        //N.B. this state should be impossible, so it's a fail safe
        if(netId == idToFind){
            String message = RequestTypes.AddRequestResult.ordinal() + " " + idToFind;
            SMSMessage searchResult = new SMSMessage(searcher, message);
            SMSManager.getInstance().sendMessage(searchResult);
            return;
        }

        SMSKademliaNode nodeToFind = new SMSKademliaNode(idToFind);
        if(KademliaNetwork.getInstance().isNodeInNetwork(nodeToFind)){
            String message = RequestTypes.AddRequestResult.ordinal() + " " + idToFind;
            SMSMessage searchResult = new SMSMessage(searcher, message);
            SMSManager.getInstance().sendMessage(searchResult);
            return;
        }

        SMSKademliaRoutingTable table = KademliaNetwork.getInstance().getLocalRoutingTable();
        SMSKademliaNode closestNode = table.findClosest(idToFind,1).get(0);
        BigInteger idToFindDistanceFromNetId = idToFind.getXorDistance(netId);
        BigInteger idToFindDistanceFromClosest = idToFind.getXorDistance(closestNode.getId());
        if(idToFindDistanceFromClosest.compareTo(idToFindDistanceFromNetId) > 0){
            //I got further away from what I'm looking for, so I'm the closest,
            //so I return this id
            String message = RequestTypes.AddRequestResult.ordinal() + " " + idToFind;
            SMSMessage searchResult = new SMSMessage(searcher, message);
            SMSManager.getInstance().sendMessage(searchResult);
        }
        else{
            //I got closer to what I'm looking for, so I ask that id to find it.
            SMSPeer closer = closestNode.getPeer();
            String message = RequestTypes.FindIdForAddRequest.ordinal() + " " + idToFind + " " + searcher;
            SMSMessage addRequestMessage = new SMSMessage(closer, message);
            SMSManager.getInstance().sendMessage(addRequestMessage);
        }
    }
}
