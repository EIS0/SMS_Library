package com.example.webdictionary;

import android.content.Context;
import android.util.Log;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

/**
 * @author Marco Cognolato
 */
public class NetworkConnection {

    //Singleton Design Pattern
    private static NetworkConnection net;
    private NetworkConnection(Context context, SMSPeer myPeer){
        netDict = new SMSNetDictionary();
        Log.d(LOG_KEY, "Found myPeer: " + myPeer.getAddress());
        if(myPeer != null && myPeer.isValid()){
            Log.d(LOG_KEY, "Added myPeer: " + myPeer.getAddress());
            netDict.add(myPeer, null);
        }
        NetworkListener listener = new NetworkListener(this);
        SMSManager.getInstance(context).addReceiveListener(listener);
    }
    public static NetworkConnection getInstance(Context context, SMSPeer myPeer){
        if(net == null){
            net = new NetworkConnection(context, myPeer);
        }
        net.context = context;
        return net;
    }

    private SMSNetDictionary netDict;
    private Context context;
    private final String LOG_KEY = "NetCon";
    public enum RequestType{
        JoinPermission,
        AcceptJoin,
        LeavePermission,
        Ping
    }
    private enum UpdateType{
        Add,
        Remove,
        Change
    }

    /**
     * Sends a given valid peer a request to join his network
     */
    public void askToJoin(SMSPeer peer){
        SMSManager.getInstance(context).sendMessage(new SMSMessage(peer, RequestType.JoinPermission.ordinal()+""));
    }

    /**
     * Accepts a request to Join this network, then sends the peer an update on the net
     * @param peer The Peer asking to join this network
     */
    private void acceptJoin(SMSPeer peer){
        //Create a String with all the Peers in my network
        String netPeers = "";
        for(SMSPeer netPeer : netDict.getAvailablePeers()){
            netPeers += netPeer + " ";
        }
        //Then I add the new peer to the net, and I update him on the net state
        //N.B. I don't send him that he's on the net because he already has it's own net with him inside
        netDict.add(peer, null);
        SMSManager.getInstance(context).sendMessage(new SMSMessage(peer, RequestType.AcceptJoin.ordinal() + " " + netPeers));
    }

    /**
     * Sends a given SMSPeer a request to Join this network
     */
    private void inviteToJoin(SMSPeer peer){

    }

    /**
     * Sends a ping to notify that the user is still online
     */
    private void sendPing(){

    }

    /**
     * Updates the current Network State given a String of peers in the network to update
     */
    private void updateNet(String peersInNet){
        String[] peers = peersInNet.split(" ");
        //I start from one, not checking the first code, because I know it's not a Peer
        for(int i = 1; i < peers.length; i++){
            SMSPeer peer = new SMSPeer(peers[i]);
            netDict.add(peer, null);
        }
    }

    /**
     * Returns currently online SMSPeers
     */
    public SMSPeer[] getOnlinePeers(){
        return netDict.getAvailablePeers();
    }

    /**
     * Network listener listening for incoming Network-related Events
     */
    private class NetworkListener implements ReceivedMessageListener<SMSMessage>{
        NetworkConnection net;
        public NetworkListener(NetworkConnection net){
            this.net = net;
        }

        @Override
        public void onMessageReceived(SMSMessage message) {
            String text = message.getData();
            SMSPeer peer = message.getPeer();
            if(peer.toString().contains("+1555521")){
                peer = new SMSPeer(peer.toString().substring(peer.toString().length() - 4));
            }
            RequestType incomingRequest = RequestType.values()[Integer.parseInt(text.split(" ")[0])];

            if(incomingRequest == RequestType.JoinPermission){
                Log.d(LOG_KEY, "Received Join Permission: accepting...");
                net.acceptJoin(peer);
            }
            else if(incomingRequest == RequestType.AcceptJoin){
                Log.d(LOG_KEY, "Received Join Accepted: updating net...");
                net.updateNet(text);
            }
        }
    }
}
