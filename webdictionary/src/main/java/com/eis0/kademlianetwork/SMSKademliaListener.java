package com.eis0.kademlianetwork;

import android.util.Log;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis.smslibrary.listeners.SMSReceivedServiceListener;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;

/**
 * Listener class that sends the appropriate command to the relative appropriate handler
 *
 * @author Marco Cognolato
 * @author Matteo Carnelos
 * @author Edoardo Raimondi
 * @author Enrico Cestaro
 */

public class SMSKademliaListener extends SMSReceivedServiceListener {
    private final static String LOG_TAG = "MSG_LSTNR";
    KademliaNetwork kadNet;
    ResourceExchangeHandler resourceExchangeHandler;
    private boolean hasPong;

    SMSKademliaListener(KademliaNetwork kadNet) {
        this.kadNet = kadNet;
        resourceExchangeHandler = new ResourceExchangeHandler();
        hasPong = false;
    }

    /**
     * This method sends an acknowledge message
     *
     * @param peer The {@link SMSPeer} of the node that contacted me
     * @author Edoardo Raimondi
     */
    public void sendAcknowledge(SMSPeer peer) {
        String message = RequestTypes.AcknowledgeMessage.ordinal() + " ";
        SMSMessage acknowledgeMessage = new SMSMessage(peer, message);
        SMSManager.getInstance().sendMessage(acknowledgeMessage);
    }

    /**
     * This method sends a Pong message
     *
     * @param peer The {@link SMSPeer} of the node that contacted me
     * @author Edoardo Raimondi
     */
    public void sendPong(SMSPeer peer) {
        String pong = RequestTypes.Pong.ordinal() + " ";
        SMSMessage pongMessage = new SMSMessage(peer, pong);
        SMSManager.getInstance().sendMessage(pongMessage);
    }


    /**
     * This method analyzes the incoming messages, extracts the content, and processes it depending
     * upon the {@link RequestTypes} contained at the beginning of the message
     *
     * @param message The message received.
     */
    @Override
    public void onMessageReceived(SMSMessage message) {
        KademliaMessage kadMessage = new KademliaMessage(message);
        SMSPeer peer = kadMessage.peer;
        RequestTypes incomingRequest = kadMessage.requestType;
        KademliaId idToFind = kadMessage.idToFind;
        SMSPeer searcher = kadMessage.searcher;
        String key = kadMessage.key;
        String resource = kadMessage.resource;
        //The two values idToFind and idFound share the same field, depending upon the type of request
        //the SMSKademliaListener knows which one of the two is occupying it
        KademliaId idFound = idToFind;

        //Starts a specific action depending upon the request or the command sent by other users
        switch (incomingRequest) {
            /**Acknowledge messages*/
            case AcknowledgeMessage:
                kadNet.setRespond(true);
                break;

            /**Refreshing operations*/
            case Ping:
                //let others know I'm alive and I'm happy to be
                sendPong(peer);
                break;
            case Pong:
                //I know that someone is alive
                kadNet.setPong(true);
                break;
            case FindIdRefresh:
                //Processes the information brought by the message received
                Log.i(LOG_TAG, "IdFound: " + idFound);
                IdFinderHandler.searchId(idToFind, searcher, ResearchMode.Refresh);
                break;
            case SearchResultReplacement:
                //I found the node, let's insert it in my routing table
                SMSKademliaNode nodeToAdd = new SMSKademliaNode(idFound);
                kadNet.getLocalRoutingTable().insert(nodeToAdd);
                break;


            /**Joining a network */
            case JoinPermission:
                ConnectionHandler.sendAcceptRequest(peer);
                break;
            case AcceptJoin:
                ConnectionHandler.acceptRequest(peer);
                break;
            case FindId:
                sendAcknowledge(peer);
                //1. Processes the information brought by the message received
                Log.i(LOG_TAG, "IdFound: " + idFound);
                IdFinderHandler.searchId(idToFind, searcher, ResearchMode.JoinNetwork);
                break;
            case SearchResult:
                sendAcknowledge(peer);
                //1. Processes the information brought by the message received
                TableUpdateHandler.stepTableUpdate(idFound);
                break;


            /**Adding a resource to the Dictionary */
            case FindIdForAddRequest:
                sendAcknowledge(peer);
                //1. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request from: " + searcher + ".\nTarget: " +
                        idToFind);
                resourceExchangeHandler.processRequest(idToFind, searcher, ResearchMode.AddToDictionary);
                break;
            case ResultAddRequest:
                sendAcknowledge(peer);
                //1. Add the node that answered the research to the local RoutingTable
                KademliaNetwork.getInstance().addNodeToTable(new SMSKademliaNode(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request RESULT: " + idToFind);
                resourceExchangeHandler.completeRequest(idToFind, peer, ResearchMode.AddToDictionary);
                break;
            case AddToDict:
                sendAcknowledge(peer);
                //1. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received AddToDictionary request.\nKey: " + key + ",\nResource:" +
                        resource);
                KademliaNetwork.getInstance().addToLocalDictionary(key, resource);
                break;


            /**Asking for a resource to the Dictionary*/
            case FindIdForGetRequest:
                sendAcknowledge(peer);
                //1. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request from: " + searcher + ".\nTarget: " +
                        idToFind);
                resourceExchangeHandler.processRequest(idToFind, searcher, ResearchMode.FindInDictionary);
                break;
            case ResultGetRequest:
                sendAcknowledge(peer);
                //1. Add the node that answered the research to the local RoutingTable
                KademliaNetwork.getInstance().addNodeToTable(new SMSKademliaNode(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request RESULT: " + idToFind);
                resourceExchangeHandler.completeRequest(idToFind, peer, ResearchMode.FindInDictionary);
                break;
            case GetFromDict:
                sendAcknowledge(peer);
                //1. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received GetFromDictionary request.\nKey: " + key);
                resource = KademliaNetwork.getInstance().getFromLocalDictionary(key).toString();
                //2. Send the <key, resource> pair
                String resourceToAdd = RequestTypes.AddToDict.ordinal() + " " + key + " " + resource;
                message = new SMSMessage(peer, resourceToAdd);
                SMSManager.getInstance().sendMessage(message);
                break;


            /**Remove a resource from the Dictionary */
            case FindIdForDeleteRequest:
                sendAcknowledge(peer);
                //1. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request from: " + searcher + ".\nTarget: " +
                        idToFind);
                resourceExchangeHandler.processRequest(idToFind, searcher, ResearchMode.RemoveFromDictionary);
                break;
            case ResultDeleteRequest:
                sendAcknowledge(peer);
                //1. Add the node that answered the research to the local RoutingTable
                KademliaNetwork.getInstance().addNodeToTable(new SMSKademliaNode(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request RESULT: " + idToFind);
                resourceExchangeHandler.completeRequest(idToFind, peer, ResearchMode.RemoveFromDictionary);
                break;
            case RemoveFromDict:
                sendAcknowledge(peer);
                //1. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received AddToDictionary request.\nKey: " + key);
                KademliaNetwork.getInstance().removeFromLocalDictionary(key);
                break;
        }
    }
}
