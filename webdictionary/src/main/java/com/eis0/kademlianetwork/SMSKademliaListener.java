package com.eis0.kademlianetwork;

import android.util.Log;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.ReceivedMessageListener;
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

    ResourceExchangeHandler resourceExchangeHandler = new ResourceExchangeHandler();
    KademliaNetwork kadNet;

    SMSKademliaListener(KademliaNetwork kadNet){
        this.kadNet = kadNet;
    }
    //TODO: remember to make it work (register the listener, somehow. How does it work again?)
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
                break;
            case JoinPermission:
                ConnectionHandler.sendAcceptRequest(peer);
                break;
            case AcceptJoin:
                ConnectionHandler.acceptRequest(peer);
                break;
            case FindId:
                Log.e("CONN_LOG", "IdFound: " + splitted[1]);
                KademliaId idToFind = new KademliaId(splitted[1]);
                SMSPeer searcher = new SMSPeer(splitted[2]);
                IdFinderHandler.searchId(idToFind, searcher);
                break;
            case SearchResult:
                KademliaId idFound = new KademliaId(splitted[1]);
                TableUpdateHandler.stepTableUpdate(idFound);
                break;

            case FindIdForAddRequest:
                idToFind = new KademliaId(splitted[1]);
                searcher = new SMSPeer(splitted[2]);
                resourceExchangeHandler.processAddRequest(idToFind, searcher);
                break;
            case AddRequestResult:
                idToFind = new KademliaId(splitted[1]);
                resourceExchangeHandler.completeAddRequest(idToFind, peer);
                break;

            case AddToDict:
                String key = splitted[1];
                String resource = splitted[2];
                KademliaNetwork.getInstance().addToLocalDictionary(key, resource);
                break;
        }
    }
}
