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
     * @param  peer The {@link SMSPeer} of the node that contacted me
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
     * @param  peer The {@link SMSPeer} of the node that contacted me
     * @author Edoardo Raimondi
     */
    public void sendPong(SMSPeer peer){
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
        String text = message.getData();
        SMSPeer peer = message.getPeer();
        //Converts the code number in the message to the related enum
        RequestTypes incomingRequest = RequestTypes
                .values()[Integer.parseInt(text.split(" ")[0])];
        //Array of strings containing the message fields
        String[] splitted = text.split(" ");

        //Starts a specific action depending upon the request or the command sent by other users
        KademliaId idToFind;
        SMSPeer searcher;
        KademliaId idFound;

        String key;
        String resource;

        switch (incomingRequest) {
            /**Acknowledge messages*/
            case AcknowledgeMessage:
                //that means the sent request has been taken by the node
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
                Log.i("CONN_LOG", "IdFound: " + splitted[1]);
                idToFind = new KademliaId(splitted[1]);
                searcher = new SMSPeer(splitted[2]);
                IdFinderHandler.searchId(idToFind, searcher, ResearchMode.Refresh);
                break;
            case SearchResultReplacement:
                //I found the node, let's insert it in my routing table
                idFound = new KademliaId(splitted[1]);
                SMSKademliaNode nodeToAdd = new SMSKademliaNode(idFound);
                kadNet.getLocalRoutingTable().insert(nodeToAdd);
                break;
                
        }
    }
}
