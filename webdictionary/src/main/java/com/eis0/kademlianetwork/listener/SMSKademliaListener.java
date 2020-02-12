package com.eis0.kademlianetwork.listener;


import android.util.Log;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis.smslibrary.listeners.SMSReceivedServiceListener;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.KademliaInvitation;
import com.eis0.kademlianetwork.KademliaJoinableNetwork;
import com.eis0.kademlianetwork.commands.KadAddPeer;
import com.eis0.kademlianetwork.commands.localdictionary.KadAddLocalResource;
import com.eis0.kademlianetwork.commands.localdictionary.KadRemoveLocalResource;
import com.eis0.kademlianetwork.commands.messages.KadPong;
import com.eis0.kademlianetwork.commands.messages.KadSendAcknowledge;
import com.eis0.kademlianetwork.informationdeliverymanager.IdFinderHandler;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessageAnalyzer;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;
import com.eis0.netinterfaces.commands.CommandExecutor;

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

    /**
     * This method analyzes the incoming messages, extracts the content, and processes it depending
     * upon the {@link RequestTypes} contained at the beginning of the message
     *
     * @param message The message received.
     */
    @Override
    public void onMessageReceived(SMSMessage message) {
        KademliaMessageAnalyzer kadMessage = new KademliaMessageAnalyzer(message);
        //the peer who sent the message
        SMSPeer peer = kadMessage.getPeer();
        //the request sent inside the message
        RequestTypes incomingRequest = kadMessage.getCommand();
        //the peer who's searching (if present)
        SMSPeer searcher = kadMessage.getSearcher();
        //the id to find (if present)
        KademliaId idToFind = kadMessage.getIdToFind();
        //the key of a resource (if present)
        String key = kadMessage.getKey();
        //the resource (if present)
        String resource = kadMessage.getResource();
        //the node object of who sent the message
        SMSKademliaNode node = new SMSKademliaNode(peer);

        RequestsHandler requestsHandler = KademliaJoinableNetwork.getInstance().getRequestsHandler();

        //Starts a specific action depending upon the request or the command sent by other users
        switch (incomingRequest) {
            /*Acknowledge messages*/
            case AcknowledgeMessage:
                KademliaJoinableNetwork.getInstance().connectionInfo.setRespond(true);
                break;

            /*Refreshing operations*/
            case Ping:
                //Lets others know I'm alive and I'm happy to be
                CommandExecutor.execute(new KadPong(peer));
                break;
            case Pong:
                //I know that someone is alive
                KademliaJoinableNetwork.getInstance().connectionInfo.setPong(true);
                break;

            /*Joining a network */
            case JoinPermission:
                KademliaInvitation invitation = new KademliaInvitation(peer);
                KademliaJoinableNetwork.getInstance().checkInvitation(invitation);
                break;
            case AcceptJoin:
                CommandExecutor.execute(new KadAddPeer(peer, KademliaJoinableNetwork.getInstance()));
                break;

            /*Searching for an Id*/
            case FindId:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Id To Find: " + idToFind);
                IdFinderHandler.searchId(idToFind, searcher);
                break;
            case FindIdSearchResult:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                //2. Processes the information brought by the message received
                requestsHandler.completeFindIdRequest(idToFind, peer);
                break;


            /*Adding a resource to the Dictionary */
            case AddToDict:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received AddToDictionary request.\nKey: " + key + ",\nResource:" +
                        resource);
                CommandExecutor.execute(new KadAddLocalResource(key, resource, KademliaJoinableNetwork.getInstance().getLocalDictionary()));
                break;


            /*Asking for a resource to the Dictionary*/
            case GetFromDict:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received GetFromDictionary request.\nKey: " + key);
                resource = KademliaJoinableNetwork.getInstance()
                        .getLocalDictionary()
                        .getResource(key);
                //3. Send the <key, resource> pair
                message = new KademliaMessage()
                        .setPeer(peer)
                        .setRequestType(RequestTypes.ResultGetRequest)
                        .setKey(key)
                        .setResource(resource)
                        .buildMessage();
                SMSManager.getInstance().sendMessage(message);
                break;
            case ResultGetRequest:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                KademliaJoinableNetwork.getInstance().addNodeToTable(new SMSKademliaNode(peer));
                //2. Processes the information brought by the message received
                requestsHandler.completeFindResourceRequest(key, resource);
                break;


            /*Remove a resource from the Dictionary */
            case RemoveFromDict:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received AddToDictionary request.\nKey: " + key);
                CommandExecutor.execute(new KadRemoveLocalResource(key, KademliaJoinableNetwork.getInstance().getLocalDictionary()));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + incomingRequest);
        }
    }
}
