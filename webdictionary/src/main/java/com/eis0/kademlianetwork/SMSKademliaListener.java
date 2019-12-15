package com.eis0.kademlianetwork;

import android.util.Log;

import com.eis0.kademlia.KademliaId;
import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

/**
 * Listener class that send the appropriate command to
 * the relative appropriate handler
 *
 * @author Marco Cognolato
 * @author Matteo Carnelos
 * @author Edoardo Raimondi
 * @author Enrico Cestaro
 */

public class SMSKademliaListener implements ReceivedMessageListener<SMSMessage> {

    /*Create a 10 second timer. If a don't receive an acknowledge message in that time, I quit*/
    RespondTimer timer = new RespondTimer();
    KademliaNetwork KadNet;
    ResourceExchangeHandler resourceExchangeHandler = new ResourceExchangeHandler();
    //TODO: remember to make it work (register the listener, somehow. How does it work again?)

    SMSKademliaListener(KademliaNetwork kadNet) {
        this.KadNet = kadNet;
    }

    /**
     * Send an acknowledge message
     *
     * @param peer \The {@link SMSPeer} of the node that contacted me
     * @author Edoardo Raimondi
     */
    public void sendAcknowledge(SMSPeer peer) {
        String message = RequestTypes.AcknowledgeMessage.ordinal() + " ";
        SMSMessage acknowoledgeMessage = new SMSMessage(peer, message);
        SMSManager.getInstance().sendMessage(acknowoledgeMessage);
    }


    /**
     * This method analyze the incoming messages, and extracts the content and the CODE
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

        /*I suppose to have a predefine message space reserved to the key and to the content
        String key = text.substring(2, 10);
        String content = text.substring(11);
        TODO: verify
         */

        //Array of strings containing the message fields
        String[] splitted = text.split(" ");
        //Starts a specific action depending upon the request or the command sent by other users
        switch (incomingRequest) {
            case AcknowledgeMessage:
                //that means the sent request has been taken by the node
                break;

            /**Joining a network */
            case JoinPermission:
                ConnectionHandler.sendAcceptRequest(peer);
                break;
            case AcceptJoin:
                ConnectionHandler.acceptRequest(peer);
                break;
            case FindId:
                /*First at all, I make know that I can work*/
                sendAcknowledge(peer);
                Log.e("CONN_LOG", "IdFound: " + splitted[1]);
                KademliaId idToFind = new KademliaId(splitted[1]);
                SMSPeer searcher = new SMSPeer(splitted[2]);
                IdFinderHandler.searchId(idToFind, searcher, ResearchMode.JoinNetwork);
                break;
            case SearchResult:
                /*First at all, I make know that I can work*/
                sendAcknowledge(peer);
                KademliaId idFound = new KademliaId(splitted[1]);
                TableUpdateHandler.stepTableUpdate(idFound);
                break;

            /**Adding a resource to the Dictionary */
            case FindIdForAddRequest:
                /*First at all, I make know that I can work*/
                sendAcknowledge(peer);
                idToFind = new KademliaId(splitted[1]);
                searcher = new SMSPeer(splitted[2]);
                resourceExchangeHandler.processAddRequest(idToFind, searcher);
                break;
            case AddRequestResult:
                /*First at all, I make know that I can work*/
                sendAcknowledge(peer);
                idToFind = new KademliaId(splitted[1]);
                resourceExchangeHandler.completeAddRequest(idToFind, peer);
                break;
            case AddToDict:
                /*First at all, I make know that I can work*/
                sendAcknowledge(peer);
                String key = splitted[1];
                String resource = splitted[2];
                KademliaNetwork.getInstance().addToLocalDictionary(key, resource);
                break;
        }
    }
}
