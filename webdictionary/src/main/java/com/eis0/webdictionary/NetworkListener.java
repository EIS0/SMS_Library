package com.eis0.webdictionary;

import android.util.Log;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

/**
 * Network listener listening for incoming Network-related Events
 */
class NetworkListener implements ReceivedMessageListener<SMSMessage> {
    private NetworkConnection net;
    private final String LOG_KEY = "NetListener";
    NetworkListener(NetworkConnection net){
        this.net = net;
    }

    @Override
    public void onMessageReceived(SMSMessage message) {
        String text = message.getData();
        SMSPeer peer = message.getPeer();
        //if I'm using simulators I only need to get the last 4 digits of the number
        if(peer.toString().contains("+1555521")){
            peer = new SMSPeer(peer.toString().substring(peer.toString().length() - 4));
        }
        //convert the code number in the message to the related enum
        RequestType incomingRequest = RequestType.values()[Integer.parseInt(text.split(" ")[0])];
        //starts a specific action based on the action received from the other user
        if(incomingRequest == RequestType.JoinPermission){
            Log.d(LOG_KEY, "Received Join Permission: accepting...");
            net.acceptJoin(peer, text.substring(2));
        }
        else if(incomingRequest == RequestType.AcceptJoin){
            Log.d(LOG_KEY, "Received Join Accepted: updating net...");
            net.addToNet(text.substring(2));
        }
        else if(incomingRequest == RequestType.AddPeers){
            Log.d(LOG_KEY, "Received Update Net Request: updating net...");
            net.addToNet(text.substring(2));
        }
        else if(incomingRequest == RequestType.LeavePermission){
            Log.d(LOG_KEY, "Received Leave Permission: ...");
            net.acceptLeave(peer);
        }
        else if(incomingRequest == RequestType.AcceptLeave){
            Log.d(LOG_KEY, "Received Leave Accepted: updating net...");
            net.removeFromNet(text.substring(2));
        }
        else if(incomingRequest == RequestType.Ping){
            Log.d(LOG_KEY, "Received Ping...");
            net.incomingPing(peer);
        }
    }
}