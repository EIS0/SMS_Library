package com.eis0.kademlianetwork;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

/**
 * Listener of the network, receives the incoming messages and calls the methods of the dedicated
 * classes to handle different scenarios
 *
 * @author Marco Cognolato
 * @author Enrico Cestaro
 */
class KademliaListener implements ReceivedMessageListener<SMSMessage> {

        private KademliaNetwork kadNet;
        private final String LOG_KEY = "KadListener";

        KademliaListener(KademliaNetwork kadNet){
            this.kadNet = kadNet;
        }

    /**
     * This method analyze the incoming messages, and extracts the content and the CODE
     * @param message The message received.
     */
    @Override
        public void onMessageReceived(SMSMessage message) {
            String text = message.getData();
            SMSPeer peer = message.getPeer();
            //if/else for handling simulators, which only need to get the last 4 digits of the number
            if (peer.toString().contains("+1555521")) {
                peer = new SMSPeer(peer.toString().substring(peer.toString().length() - 4));
            }
            //Converts the code number in the message to the related enum
            KademliaNetwork.RequestType incomingRequest = KademliaNetwork
                    .RequestType
                    .values()[Integer.parseInt(text.split(" ")[0])];
            //Starts a specific action depending upon the request or the command sent by other users
            switch (incomingRequest) {
                case AcknowledgeMessage:
                    break;
                case JoinPermission:
                    ConnectionHandler.acceptRequest(peer);
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
