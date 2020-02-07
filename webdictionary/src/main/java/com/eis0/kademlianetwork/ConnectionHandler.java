package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlianetwork.commands.KadAcceptInvite;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.netinterfaces.commands.CommandExecutor;

/**
 * Class handling the first time connection to a kademlia network.
 * Gives methods to join someone's network or to invite someone to my
 * network
 *
 * @author Marco Cognolato
 */
public class ConnectionHandler {

    /**
     * Accepts the request sent by an SMSPeer (by notifying him)
     *
     * @param peer The peer who sent the request (and to notify back)
     */
    public static void sendAcceptRequest(SMSPeer peer) {
        /*
         * Both if I get invited to another one's network,
         * or someone asked to enter my network,
         * I have to send back an accept message
         * */

        SMSMessage message = new KademliaMessage()
                .setPeer(peer)
                .setRequestType(RequestTypes.AcceptJoin)
                .buildMessage();
        SMSManager.getInstance().sendMessage(message);

        /*
        * Then I have to create/update my routing table, so I call the proper function
        * */
        acceptRequest(peer);
    }

    /**
     * Accepts the request sent by an SMSPeer
     *
     * @param peer The peer who sent the request
     */
    public static void acceptRequest(SMSPeer peer) {
        /*
         * If both the other peer sent me a request to join his network, or
         * a request to invite me to his network, I have to update my routing table
         * from scratch adding him to the table as a contact
         * This serves to more strongly fuse both networks by
         * creating contacts from the other network
         * */

        CommandExecutor.execute(new KadAcceptInvite(new KademliaInvitation(peer)));
    }

}