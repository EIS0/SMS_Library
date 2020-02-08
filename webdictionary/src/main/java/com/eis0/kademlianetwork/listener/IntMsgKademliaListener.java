package com.eis0.kademlianetwork.listener;

import android.util.Log;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.KademliaInvitation;
import com.eis0.kademlianetwork.KademliaJoinableNetwork;
import com.eis0.kademlianetwork.KademliaNetwork;
import com.eis0.kademlianetwork.commands.KadAddPeer;
import com.eis0.kademlianetwork.commands.KadPong;
import com.eis0.kademlianetwork.commands.KadSendAcknowledge;
import com.eis0.kademlianetwork.informationdeliverymanager.IdFinderHandler;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessageAnalyzer;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.kademlianetwork.informationdeliverymanager.ResearchMode;
import com.eis0.kademlianetwork.informationdeliverymanager.ResourceExchangeHandler;
import com.eis0.kademlianetwork.routingtablemanager.TableUpdateHandler;
import com.eis0.netinterfaces.commands.CommandExecutor;

/**
 * Listener class which sends the appropriate command to the relative appropriate handler
 *
 * @author Marco Cognolato
 * @author Matteo Carnelos
 * @author Edoardo Raimondi
 * @author Enrico Cestaro
 */
public class IntMsgKademliaListener {
    private final static String LOG_TAG = "MSG_LSTNR";
    private final KademliaNetwork kadNet;
    private final ResourceExchangeHandler resourceExchangeHandler;
    private static IntMsgKademliaListener instance;

    private IntMsgKademliaListener(KademliaNetwork kadNet) {
        this.kadNet = kadNet;
        resourceExchangeHandler = new ResourceExchangeHandler();
    }
    //@TODO change it in an ObjectPoll
    public static IntMsgKademliaListener getInstance(KademliaNetwork kadNet) {
            if(instance == null) instance = new IntMsgKademliaListener(kadNet);
            return instance;
        }

    /**
     * This method analyzes the incoming messages, extracts the content, and processes it depending
     * upon the {@link RequestTypes} contained at the beginning of the message
     *
     * @param message The message received.
     */
    public void processMessage(SMSMessage message) {
        KademliaMessageAnalyzer kadMessage = new KademliaMessageAnalyzer(message);
        SMSPeer peer = kadMessage.getPeer();
        RequestTypes incomingRequest = kadMessage.getCommand();
        SMSPeer searcher = kadMessage.getSearcher();
        KademliaId idToFind = kadMessage.getIdToFind();
        String key = kadMessage.getKey();
        String resource = kadMessage.getResource();
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
                CommandExecutor.execute(new KadPong(peer));
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
                KademliaInvitation invitation = new KademliaInvitation(peer);
                KademliaJoinableNetwork.getInstance().checkInvitation(invitation);
                break;
            case AcceptJoin:
                CommandExecutor.execute(new KadAddPeer(peer, KademliaJoinableNetwork.getInstance()));
                break;
            case FindId:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "IdFound: " + idFound);
                IdFinderHandler.searchId(idToFind, searcher, ResearchMode.JoinNetwork);
                break;
            case SearchResult:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                //2. Processes the information brought by the message received
                TableUpdateHandler.stepTableUpdate(idFound);
                break;


            /*Adding a resource to the Dictionary */
            case FindIdForAddRequest:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request from: " + searcher + ".\nTarget: " +
                        idToFind);
                IdFinderHandler.searchId(idToFind, searcher, ResearchMode.AddToDictionary);
                break;
            case ResultAddRequest:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                KademliaJoinableNetwork.getInstance().addNodeToTable(new SMSKademliaNode(peer));
                //2. Processes the information brought by the message received
                //@TODO: il Log genera errore nei test, come risolvere
                //Log.i(LOG_TAG, "Received ID research request RESULT: " + idToFind);
                resourceExchangeHandler.completeRequest(idToFind, peer, ResearchMode.AddToDictionary);
                break;
            case AddToDict:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received AddToDictionary request.\nKey: " + key + ",\nResource:" +
                        resource);
                KademliaJoinableNetwork.getInstance().addToLocalDictionary(key, resource);
                break;


            /*Asking for a resource to the Dictionary*/
            case FindIdForGetRequest:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request from: " + searcher + ".\nTarget: " +
                        idToFind);
                IdFinderHandler.searchId(idToFind, searcher, ResearchMode.FindInDictionary);
                break;
            case ResultGetRequest:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                KademliaJoinableNetwork.getInstance().addNodeToTable(new SMSKademliaNode(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request RESULT: " + idToFind);
                resourceExchangeHandler.completeRequest(idToFind, peer, ResearchMode.FindInDictionary);
                break;
            case GetFromDict:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received GetFromDictionary request.\nKey: " + key);
                resource = KademliaJoinableNetwork.getInstance().getFromLocalDictionary(key).toString();
                //2. Send the <key, resource> pair
                message = new KademliaMessage()
                        .setPeer(peer)
                        .setRequestType(RequestTypes.AddToDict)
                        .setKey(key)
                        .setResource(resource)
                        .buildMessage();
                SMSManager.getInstance().sendMessage(message);
                break;


            /*Remove a resource from the Dictionary */
            case FindIdForDeleteRequest:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request from: " + searcher + ".\nTarget: " +
                        idToFind);
                IdFinderHandler.searchId(idToFind, searcher, ResearchMode.RemoveFromDictionary);
                break;
            case ResultDeleteRequest:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                KademliaJoinableNetwork.getInstance().addNodeToTable(new SMSKademliaNode(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received ID research request RESULT: " + idToFind);
                resourceExchangeHandler.completeRequest(idToFind, peer, ResearchMode.RemoveFromDictionary);
                break;
            case RemoveFromDict:
                //1. I inform that I'm alive and happy to be
                CommandExecutor.execute(new KadSendAcknowledge(peer));
                //2. Processes the information brought by the message received
                Log.i(LOG_TAG, "Received AddToDictionary request.\nKey: " + key);
                KademliaJoinableNetwork.getInstance().removeFromLocalDictionary(key);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + incomingRequest);
        }
    }
}
