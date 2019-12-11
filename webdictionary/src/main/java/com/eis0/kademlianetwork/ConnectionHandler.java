package com.eis0.kademlianetwork;

import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

/**
 * Class handling the first time connection to a kademlia network.
 * Gives methods to join someone's network or to invite someone to my
 * network
 *
 * @author Marco Cognolato
 */
public class ConnectionHandler implements ReceivedMessageListener<SMSMessage> {

    /**
     * Invites a given valid SMSPeer to join my kademlia network
     * @param peer The peer to invite to my network
     */
    public void inviteToJoin(SMSPeer peer){
        String messageRequest = RequestTypes.JoinPermission.ordinal() + "";
        SMSMessage message = new SMSMessage(peer, messageRequest);
        SMSManager.getInstance().sendMessage(message);
    }

    /**
     * Asks a given valid peer if I can join HIS Kademlia Network
     * @param peer The peer to ask to
     */
    public void askToJoin(SMSPeer peer){
        //asking to join has the same functionality as inviting someone to join
        inviteToJoin(peer);
    }

    /**
     * Accepts the request sent by an SMSPeer
     * @param peer The peer who sent the request
     */
    public void acceptRequest(SMSPeer peer){
        /*
        * If both the other peer sent me a request to join his network, or
        * a request to invite me to his network, I have to update my routing table
        * from scratch adding him to the table as a contact. This serves to more strongly
        * fuse both networks by creating contacts from the other network
        * */
        SMSKademliaNode node = new SMSKademliaNode(peer);
        KademliaNetwork.getInstance().addNodeToTable(node);
        KademliaNetwork.getInstance().updateTable();
    }

    /**
     * This method analyze the incoming messages, and extracts the content and the CODE
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
                acceptRequest(peer);
                break;
            case AddPeers:
                break;
            case AddToDict:
                break;
            case RemoveFromDict:
                break;
            case UpdateDict:
                break;
            case NodeLookup:
                break;
        }
    }
}
