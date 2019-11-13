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
        if(myPeer.isValid()){
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
        AddPeers,
        RemovePeers,
        UpdatePeers,
        LeavePermission,
        Ping
    }

    /**
     * Sends a given valid peer a request to join his network,
     * It also sends the current network state
     * @param peer The peer to send the message to.
     */
    public void askToJoin(SMSPeer peer){
        SMSManager.getInstance(context).sendMessage(new SMSMessage(peer, RequestType.JoinPermission.ordinal()+" " + peersInNetwork()));
    }

    /**
     * Accepts a request to Join this network, then sends the peer an update on the net
     * @param linkPeer The Peer asking to join this network, working as a link between 2 nets now joined
     * @param text The message received with the sender's network state
     */
    private void acceptJoin(SMSPeer linkPeer, String text){
        String[] newPeersOnNet = text.split(" ");
        String oldPeersInNet = peersInNetwork();
        //add new peers to my net
        addToNet(text);
        addToNet(oldPeersInNet);
        //notify new peers of my old peers
        for(String newPeerAddress: newPeersOnNet){
            Log.d(LOG_KEY, "New Peer: " + newPeerAddress);
            SMSPeer newPeer = new SMSPeer(newPeerAddress);
            SMSManager.getInstance(context).sendMessage(new SMSMessage(newPeer, RequestType.AddPeers.ordinal() + " " + oldPeersInNet));
        }
        //notify my old peers about the new ones
        for(String oldPeerAddress : oldPeersInNet.split(" ")){
            Log.d(LOG_KEY, "Old Peer: " + oldPeerAddress);
            SMSPeer oldPeer = new SMSPeer(oldPeerAddress);
            SMSManager.getInstance(context).sendMessage(new SMSMessage(oldPeer, RequestType.AddPeers.ordinal() + " " + text));
        }
    }

    /**
     * Returns a space separated String with all the Peers in my Network
     */
    private String peersInNetwork(){
        String netPeers = "";
        for(SMSPeer netPeer : netDict.getAvailablePeers()){
            netPeers = netPeers.concat(netPeer.getAddress()).concat(" ");
        }
        return netPeers;
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
     * Adds a given String list of peers to the current network
     */
    private void addToNet(String peersInNet){
        Log.d(LOG_KEY, "Adding these new Peers: " + peersInNet);
        String[] peers = peersInNet.split(" ");
        for(String peer : peers){
            netDict.add(new SMSPeer(peer), null);
        }
    }

    /**
     * Removes a given String list of peers from the current network
     */
    private void removeFromNet(String peersInNet){
        Log.d(LOG_KEY, "Removing these Peers: " + peersInNet);
        String[] peers = peersInNet.split(" ");
        for(String peer : peers){
            netDict.remove(new SMSPeer(peer));
        }
    }

    /**
     * Updates the current Network State given a peer in the network to update and it's resources
     */
    private void updateNet(String peer, SMSResource[] resources){
        Log.d(LOG_KEY, "Updating this Peer: " + peer);
        SMSPeer peerToUpdate = new SMSPeer(peer);
        netDict.remove(peerToUpdate);
        netDict.add(peerToUpdate, resources);
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
        private NetworkListener(NetworkConnection net){
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
        }
    }
}
