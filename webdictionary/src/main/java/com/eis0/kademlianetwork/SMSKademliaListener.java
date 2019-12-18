package com.eis0.kademlianetwork;

import android.util.Log;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis.smslibrary.listeners.SMSReceivedServiceListener;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;

/**
 * Listener class that send the appropriate command to
 * the relative appropriate handler
 *
 * @author Marco Cognolato
 * @author Matteo Carnelos
 * @author Edoardo Raimondi
 * @author Enrico Cestaro
 */

public class SMSKademliaListener extends SMSReceivedServiceListener {
    private final static String LOG_TAG = "MSG_LSTNR";
    KademliaNetwork kadNet;
    ResourceExchangeHandler resourceExchangeHandler = new ResourceExchangeHandler();

    SMSKademliaListener(KademliaNetwork kadNet) {
        this.kadNet = kadNet;
    }

    /**
     * Send an acknowledge message
     *
     * @param peer The {@link SMSPeer} of the node that contacted me
     * @author Edoardo Raimondi
     */
    public void sendAcknowledge(SMSPeer peer) {
        String message = RequestTypes.AcknowledgeMessage.ordinal() + " ";
        SMSMessage acknowoledgeMessage = new SMSMessage(peer, message);
        SMSManager.getInstance().sendMessage(acknowoledgeMessage);
    }


    /**
     * This method analyzes the incoming messages, and extracts the content and the CODE
     *
     * @param message The message received.
     */
    @Override
    public void onMessageReceived(SMSMessage message) {
        String text = message.getData();
        SMSPeer peer = message.getPeer();
        //Converts the code number in the message to the related enum
        RequestTypes incomingRequest = RequestTypes
                .values()[Integer.parseInt(text.split(" ")[0])];
        //Array of strings containing the message fields
        String[] splitted = text.split(" ");
        //Starts a specific action depending upon the request or the command sent by other users
        switch (incomingRequest) {
            case AcknowledgeMessage:
                //that means the sent request has been taken by the node
                kadNet.setRespond(true);
                break;

            /**Joining a network */
            case JoinPermission:
                ConnectionHandler.sendAcceptRequest(peer);
                break;
            case AcceptJoin:
                ConnectionHandler.acceptRequest(peer);
                break;
            case FindId:
                //1. Create the response addressed to the sender, to inform him of my activity status
                sendAcknowledge(peer);
                //2. Processes the information brought by the message received
                Log.e("CONN_LOG", "IdFound: " + splitted[1]);
                KademliaId idToFind = new KademliaId(splitted[1]);
                SMSPeer searcher = new SMSPeer(splitted[2]);
                IdFinderHandler.searchId(idToFind, searcher, ResearchMode.JoinNetwork);
                break;
            case SearchResult:
                //1. Create the response addressed to the sender, to inform him of my activity status
                sendAcknowledge(peer);
                //2. Processes the information brought by the message received
                KademliaId idFound = new KademliaId(splitted[1]);
                TableUpdateHandler.stepTableUpdate(idFound);
                break;

            /**Adding a resource to the Dictionary */
            case FindIdForAddRequest:
                //1. Create the response addressed to the sender, to inform him of my activity status
                sendAcknowledge(peer);
                //2. Processes the information brought by the message received
                idToFind = new KademliaId(splitted[1]);
                searcher = new SMSPeer(splitted[2]);
                Log.i(LOG_TAG, "Received ID research request from: " + searcher + ".\nTarget: " +
                        idToFind);
                resourceExchangeHandler.processRequest(idToFind, searcher, ResearchMode.AddToDictionary);
                break;
            case AddRequestResult:
                //1. Create the response addressed to the sender, to inform him of my activity status
                sendAcknowledge(peer);
                //2. Add the node that answered the research to the local RoutingTable
                KademliaNetwork.getInstance().addNodeToTable(new SMSKademliaNode(peer));
                //3. Processes the information brought by the message received
                idToFind = new KademliaId(splitted[1]);
                Log.i(LOG_TAG, "Received ID research request RESULT: " + idToFind);
                resourceExchangeHandler.completeAddRequest(idToFind, peer);
                break;
            case AddToDict:
                //1. Create the response addressed to the sender, to inform him of my activity status
                sendAcknowledge(peer);
                //2. Processes the information brought by the message received
                String key = splitted[1];
                String resource = splitted[2];
                Log.i(LOG_TAG, "Received AddToDictionary request.\nKey: " + key + ",\nResource:" +
                        resource);
                KademliaNetwork.getInstance().addToLocalDictionary(key, resource);
                break;

            /**Asking for a resource to the Dictionary*/
            case FindIdForGetRequest:
                //1. Create the response addressed to the sender, to inform him of my activity status
                sendAcknowledge(peer);
                //2. Processes the information brought by the message received
                idToFind = new KademliaId(splitted[1]);
                searcher = new SMSPeer(splitted[2]);
                Log.i(LOG_TAG, "Received ID research request from: " + searcher + ".\nTarget: " +
                        idToFind);
                resourceExchangeHandler.processRequest(idToFind, searcher, ResearchMode.FindInDictionary);
                break;
            case GetRequestResult:
                //1. Create the response addressed to the sender, to inform him of my activity status
                sendAcknowledge(peer);
                //2. Add the node that answered the research to the local RoutingTable
                KademliaNetwork.getInstance().addNodeToTable(new SMSKademliaNode(peer));
                //3. Processes the information brought by the message received
                idToFind = new KademliaId(splitted[1]);
                Log.i(LOG_TAG, "Received ID research request RESULT: " + idToFind);
                resourceExchangeHandler.completeGetRequest(idToFind, peer);
                break;
            case GetFromDict:
                //1. Create the response addressed to the sender, to inform him of my activity status
                sendAcknowledge(peer);
                //2. Processes the information brought by the message received
                key = splitted[1];
                Log.i(LOG_TAG, "Received GetFromDictionary request.\nKey: " + key);
                resource = KademliaNetwork.getInstance().getFromLocalDictionary(key).toString();
                //3. Send the <key, resource> pair
                String resourceToAdd = RequestTypes.AddToDict.ordinal() + " " + key + " " + resource;
                message = new SMSMessage(peer, resourceToAdd);
                SMSManager.getInstance().sendMessage(message);
                break;
        }
    }
}
