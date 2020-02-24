package com.eis0.webdictionary;


import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis.smslibrary.listeners.SMSReceivedServiceListener;

/**
 * Network listener listening for incoming Network-related Events
 */
class NetworkListener extends SMSReceivedServiceListener {
    private final NetworkConnection net;

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
            net.acceptJoin(text.substring(2));
        }
        else if(incomingRequest == RequestType.AcceptJoin){
            net.addToNet(text.substring(2));
        }
        else if(incomingRequest == RequestType.AddPeers){
            net.addToNet(text.substring(2));
        }
        else if(incomingRequest == RequestType.LeavePermission){
            net.acceptLeave(peer);
        }
        else if(incomingRequest == RequestType.AcceptLeave){
            net.removeFromNet(text.substring(2));
        }
        else if(incomingRequest == RequestType.Ping){
            net.incomingPing(peer);
        }
        else if(incomingRequest == RequestType.AddToDict){
            net.addToDictionaryNoCast(text.substring(2));
        }
        else if(incomingRequest == RequestType.RemoveFromDict){
            //net.removeFromDictionaryNoCast(text.substring(2));
        }
        else if(incomingRequest == RequestType.UpdateDict){
            net.updateDictionaryNoCast(text.substring(2));
        }
    }
}