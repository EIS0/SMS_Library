package com.eis0.kademlianetwork;

import android.util.Log;

import com.eis0.kademlia.KademliaId;
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
 */

public class SMSKademliaListener implements ReceivedMessageListener<SMSMessage> {


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
                String[] splitted = text.split(" ");
                Log.e("CONN_LOG", "IdFound: " + splitted[1]);
                KademliaId idToFind = new KademliaId(splitted[1]);
                SMSPeer searcher = new SMSPeer(splitted[2]);
                IdFinderHandler.searchId(idToFind, searcher);
                break;
            case SearchResult:
                KademliaId idFound = new KademliaId(text.split(" ")[1]);
                TableUpdateHandler.stepTableUpdate(idFound);
                break;
            case AddToDictRequest:
                //I suppose to have a predefine message space reserved to the key and to the content
                String key = text.substring(2, 52);
                String content = text.substring(53);
                CommunicationHandler.receiveAddToDictionaryRequest(key, content);
        }
    }
}
