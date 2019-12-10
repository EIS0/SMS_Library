package com.eis0.kademlianetwork;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

public class KademliaListener implements ReceivedMessageListener<SMSMessage> {

        private KademliaNetwork kadNet;
        private final String LOG_KEY = "KadListener";

        KademliaListener(KademliaNetwork kadNet){
            this.kadNet = kadNet;
        }



        @Override
        public void onMessageReceived(SMSMessage message) {
            String text = message.getData();
            SMSPeer peer = message.getPeer();
            //if I'm using simulators I only need to get the last 4 digits of the number
            if (peer.toString().contains("+1555521")) {
                peer = new SMSPeer(peer.toString().substring(peer.toString().length() - 4));
            }
            //convert the code number in the message to the related enum
            KademliaNetwork.RequestType incomingRequest = KademliaNetwork.RequestType.values()[Integer.parseInt(text.split(" ")[0])];
            //starts a specific action based on the action received from the other user
            switch (incomingRequest) {
                case AcknowledgeMessage:
                    break;
                case JoinPermission:
                    break;
                case AddPeers:
                    break;
                case AddToDict:
                    break;
                case RemoveFromDict:
                    break;
                case UpdateDict:
                    break;
            }
        }
}
