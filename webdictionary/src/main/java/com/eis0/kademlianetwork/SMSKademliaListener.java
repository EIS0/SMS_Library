package com.eis0.kademlianetwork;


import android.util.Log;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis.smslibrary.listeners.SMSReceivedServiceListener;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.activitystatus.SystemMessages;
import com.eis0.kademlianetwork.informationdeliverymanager.IdFinderHandler;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.kademlianetwork.informationdeliverymanager.ResearchMode;
import com.eis0.kademlianetwork.informationdeliverymanager.ResourceExchangeHandler;
import com.eis0.kademlianetwork.routingtablemanager.TableUpdateHandler;

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
    private final KademliaNetwork kadNet;
    private final ResourceExchangeHandler resourceExchangeHandler;


    public SMSKademliaListener(KademliaNetwork kadNet) {
        this.kadNet = kadNet;
        resourceExchangeHandler = new ResourceExchangeHandler();
    }



    /**
     * This method analyzes the incoming messages, extracts the content, and processes it depending
     * upon the {@link RequestTypes} contained at the beginning of the message
     *
     * @param message The message received.
     */
    @Override
    public void onMessageReceived(SMSMessage message) {
        SMSPeer peer = message.getPeer();
        KademliaMessage kadMessage = new KademliaMessage(message.getData());
        RequestTypes incomingRequest = kadMessage.requestType;
        KademliaId idToFind = kadMessage.idToFind;
        SMSPeer searcher = kadMessage.searcher;
        String key = kadMessage.key;
        String resource = kadMessage.resource;
        //The two values, idToFind and idFound, share the same field, depending upon the type of request
        //the SMSKademliaListener knows which one of the two is occupying it, and process it consequently
        KademliaId idFound = idToFind;

        //Starts a specific action depending upon the request or the command sent by other users
        switch (incomingRequest) {
            /*Acknowledge messages*/
            case AcknowledgeMessage:
                kadNet.connectionInfo.setRespond(true);
                break;

            /*Refreshing operations*/
            case Ping:
                //Lets others know I'm alive and I'm happy to be
                SystemMessages.sendPong(peer);
                break;
            case Pong:
                //I know that someone is alive
                kadNet.connectionInfo.setPong(true);
                break;
            case FindIdRefresh:
                //Processes the information brought by the message received
                Log.i(LOG_TAG, "IdFound: " + idFound);
                IdFinderHandler.searchId(idToFind, searcher, ResearchMode.Refresh);
                break;
            case SearchResultReplacement:
                //I found the node, let's insert it in my routing table
                SMSKademliaNode nodeToAdd = new SMSKademliaNode(idFound);
                kadNet.addNodeToTable(nodeToAdd);
                break;


            /*Joining a network */
            case JoinPermission:
                ConnectionHandler.sendAcceptRequest(peer);
                break;
            case AcceptJoin:
                ConnectionHandler.acceptRequest(peer);
                break;
            case FindId:
                //1. I inform that I'm alive and happy to be
                SystemMessages.sendAcknowledge(peer);
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "IdFound: " + idFound);
                IdFinderHandler.searchId(idToFind, searcher, ResearchMode.JoinNetwork);
                break;
            case SearchResult:
                //1. I inform that I'm alive and happy to be
                SystemMessages.sendAcknowledge(peer);
                //2. Processes the information brought by the message received
                TableUpdateHandler.stepTableUpdate(idFound);
                break;


            /*Adding a resource to the Dictionary */
            case FindIdForAddRequest:
                //1. I inform that I'm alive and happy to be
                SystemMessages.sendAcknowledge(peer);
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request from: " + searcher + ".\nTarget: " +
                        idToFind);
                resourceExchangeHandler.processRequest(idToFind, searcher, ResearchMode.AddToDictionary);
                break;
            case ResultAddRequest:
                //1. I inform that I'm alive and happy to be
                SystemMessages.sendAcknowledge(peer);
                KademliaNetwork.getInstance().addNodeToTable(new SMSKademliaNode(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request RESULT: " + idToFind);
                resourceExchangeHandler.completeRequest(idToFind, peer, ResearchMode.AddToDictionary);
                break;
            case AddToDict:
                //1. I inform that I'm alive and happy to be
                SystemMessages.sendAcknowledge(peer);
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received AddToDictionary request.\nKey: " + key + ",\nResource:" +
                        resource);
                KademliaNetwork.getInstance().addToLocalDictionary(key, resource);
                break;


            /*Asking for a resource to the Dictionary*/
            case FindIdForGetRequest:
                //1. I inform that I'm alive and happy to be
                SystemMessages.sendAcknowledge(peer);
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request from: " + searcher + ".\nTarget: " +
                        idToFind);
                resourceExchangeHandler.processRequest(idToFind, searcher, ResearchMode.FindInDictionary);
                break;
            case ResultGetRequest:
                //1. I inform that I'm alive and happy to be
                SystemMessages.sendAcknowledge(peer);
                KademliaNetwork.getInstance().addNodeToTable(new SMSKademliaNode(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request RESULT: " + idToFind);
                resourceExchangeHandler.completeRequest(idToFind, peer, ResearchMode.FindInDictionary);
                break;
            case GetFromDict:
                //1. I inform that I'm alive and happy to be
                SystemMessages.sendAcknowledge(peer);
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received GetFromDictionary request.\nKey: " + key);
                resource = KademliaNetwork.getInstance().getFromLocalDictionary(key).toString();
                //2. Send the <key, resource> pair
                KademliaMessage kademliaMessage = new KademliaMessage(RequestTypes.AddToDict, null, null, key, resource);
                message = new SMSMessage(peer, kademliaMessage.toString());
                SMSManager.getInstance().sendMessage(message);
                break;


            /*Remove a resource from the Dictionary */
            case FindIdForDeleteRequest:
                //1. I inform that I'm alive and happy to be
                SystemMessages.sendAcknowledge(peer);
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request from: " + searcher + ".\nTarget: " +
                        idToFind);
                resourceExchangeHandler.processRequest(idToFind, searcher, ResearchMode.RemoveFromDictionary);
                break;
            case ResultDeleteRequest:
                //1. I inform that I'm alive and happy to be
                SystemMessages.sendAcknowledge(peer);
                KademliaNetwork.getInstance().addNodeToTable(new SMSKademliaNode(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request RESULT: " + idToFind);
                resourceExchangeHandler.completeRequest(idToFind, peer, ResearchMode.RemoveFromDictionary);
                break;
            case RemoveFromDict:
                //1. I inform that I'm alive and happy to be
                SystemMessages.sendAcknowledge(peer);
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received AddToDictionary request.\nKey: " + key);
                KademliaNetwork.getInstance().removeFromLocalDictionary(key);
                break;
        }
    }
}
