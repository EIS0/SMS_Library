package com.eis0.kademlianetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
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
public class ConnectionHandler {

    /**
     * Invites a given valid SMSPeer to join my kademlia network
     * @param peer The peer to invite to my network
     */
    public static void inviteToJoin(SMSPeer peer){
        String messageRequest = KademliaNetwork.RequestType.JoinPermission.ordinal() + "";
        SMSMessage message = new SMSMessage(peer, messageRequest);
        SMSManager.getInstance().sendMessage(message);
    }

    /**
     * Asks a given valid peer if I can join HIS Kademlia Network
     * @param peer The peer to ask to
     */
    public static void askToJoin(SMSPeer peer){
        //asking to join has the same functionality as inviting someone to join
        inviteToJoin(peer);
    }

    /**
     * Accepts the request sent by an SMSPeer (by notifying him)
     * @param peer The peer who sent the request (and to notify back)
     */
    public static void sendAcceptRequest(SMSPeer peer){
        /*
        * If both the other peer sent me a request to join his network, or
        * a request to invite me to his network, I have to update my routing table
        * from scratch adding him to the table as a contact. I also have to send
        * an acceptJoin event, so he can do the same thing.
        *  This serves to more strongly fuse both networks by
        * creating contacts from the other network
        * */

        String messageRequest = KademliaNetwork.RequestType.AcceptJoin.ordinal() + "";
        SMSMessage message = new SMSMessage(peer, messageRequest);
        SMSManager.getInstance().sendMessage(message);

        acceptRequest(peer);
    }

    /**
     * Accepts the request sent by an SMSPeer
     * @param peer The peer who sent the request
     */
    public static void acceptRequest(SMSPeer peer){
        /*
         * If both the other peer sent me a request to join his network, or
         * a request to invite me to his network, I have to update my routing table
         * from scratch adding him to the table as a contact, then I have to send
         * an acceptRequest event, so he can do the same thing.
         *  This serves to more strongly fuse both networks by
         * creating contacts from the other network
         * */

        KademliaId nodeId = new KademliaId(peer);
        SMSKademliaNode node = new SMSKademliaNode(nodeId, peer, null);
        KademliaNetwork.getInstance().addNodeToTable(node);
        KademliaNetwork.getInstance().updateTable();
    }
}
