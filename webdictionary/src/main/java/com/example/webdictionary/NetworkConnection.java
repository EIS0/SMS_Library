package com.example.webdictionary;

import android.content.Context;
import android.util.Log;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;

/**
 * @author Marco Cognolato except where otherwise indicated
 */
public class NetworkConnection {

    //Singleton Design Pattern
    private static NetworkConnection net;
    private NetworkConnection(Context context, SMSPeer myPeer){
        netDict = new SMSNetDictionary();
        if(myPeer != null){
            Log.d(LOG_KEY, "Found myPeer: " + myPeer.getAddress());
            if(myPeer.isValid()){
                Log.d(LOG_KEY, "Added myPeer: " + myPeer.getAddress());
                subscribers.add(myPeer);
            }
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
    private ArrayList<SMSPeer> subscribers = new ArrayList<>();
    private Context context;
    private final String LOG_KEY = "NetCon";
    public enum RequestType{
        JoinPermission,
        AcceptJoin,
        AddPeers,
        RemovePeers,
        UpdatePeers,
        LeavePermission,
        AcceptLeave,
        Ping
    }

    /**
     * Sends to a given valid peer a request to join his network.
     * It also sends the current network state.
     * @param peer The peer to send the message to.
     * @throws IllegalArgumentException If the peer is invalid or null
     */
    public void askToJoin(SMSPeer peer){
        if(peer == null || !peer.isValid()) throw new IllegalArgumentException();
        String textRequest = RequestType.JoinPermission.ordinal() + " " + peersInNetwork();
        SMSMessage message = new SMSMessage(peer, textRequest);

        SMSManager.getInstance(context).sendMessage(message);
    }

    /**
     * Sends a given SMSPeer a request to Join this network,
     * It also sends the current network state
     * @param peer The peer to send the message to.
     * @throws IllegalArgumentException If the peer is invalid or null
     */
    public void inviteToJoin(SMSPeer peer){
        /*
        A net request connection is one of two types:
        1. A asks B to join B's network
        2. A invites B to join A's network

        Since either case is valid they can be supported in a simple way:
        being the same type of request, so an invite to join a network is the same as
        asking to enter the other's network, so I simply call the other function
         */
        askToJoin(peer);
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
     * Sends to a given valid peer a request to leave his network
     * @param peer The peer to send a message to.
     * @author Edoardo Raimondi
     */
    public void askToLeave(SMSPeer peer){
        if(peer == null || !peer.isValid()) throw new IllegalArgumentException();
        SMSMessage message = new SMSMessage(peer, RequestType.LeavePermission.ordinal()+"");
        SMSManager.getInstance(context).sendMessage(message);
    }

    /**
     * Sends an exit notification to a given peer that will remove it
     * @param linkPeer The Peer asking to join this network, working as a link between 2 nets now joined
     * @param text The message received with the sender's network state
     * @author Edoardo Raimondi
     */
    private void acceptLeave(SMSPeer linkPeer, String text){
        //remove the peer
        removeFromNet(text);
        String newPeersInNet = peersInNetwork();
        //notify my old peers about the exit
        for(String oldPeerAddress : newPeersInNet.split(" ")){
            Log.d(LOG_KEY, "Old Peer: " + oldPeerAddress);
            SMSPeer oldPeer = new SMSPeer(oldPeerAddress);
            SMSManager.getInstance(context).sendMessage(new SMSMessage(oldPeer, RequestType.RemovePeers.ordinal() + " " + text));
        }
    }

    /**
     * Returns a space separated String with all the Peers in my Network
     */
    private String peersInNetwork(){
        String netPeers = "";
        for(SMSPeer netPeer : subscribers){
            netPeers = netPeers.concat(netPeer.getAddress()).concat(" ");
        }
        return netPeers;
    }

    /**
     * Sends to a valid peer a ping to notify that the user is still online
     * @param peer The peer to send the ping to
     * @throws IllegalArgumentException If the peer is null or not valid
     */
    public void sendPing(SMSPeer peer){
        if(peer == null || !peer.isValid()) throw new IllegalArgumentException();
        SMSMessage message = new SMSMessage(peer, RequestType.Ping.ordinal()+"");
        SMSManager.getInstance(context).sendMessage(message);
    }

    /**
     * Function called when a Ping is received from an SMSPeer,
     * Resets the Ping timer before considering that peer offline
     * @param peer The peer who sent the ping
     */
    private void incomingPing(SMSPeer peer){
        //TODO: implement this (using timers?)
    }

    /**
     * Adds a given String list of peers (separated by a space) to the current network
     * @param peersToAdd The peers to add to the net
     * @throws IllegalArgumentException If at least one of the peers is invalid
     * or if the string is empty or null
     */
    public void addToNet(String peersToAdd){
        if(peersToAdd == null || peersToAdd.equals("")) throw new IllegalArgumentException();
        String[] peers = peersToAdd.split(" ");
        for(String peer : peers){
            addToNet(new SMSPeer(peer));
        }
    }

    /**
     * Adds a given valid peer to the network, if the SMSPeer is already
     * present, it's not added again
     * @param peer The SMSPeer to add to the net
     * @throws IllegalArgumentException If the peer is null or invalid
     */
    public void addToNet(SMSPeer peer){
        if(peer == null || !peer.isValid()) throw new IllegalArgumentException();
        if(!subscribers.contains(peer)) subscribers.add(peer);
    }

    /**
     * Adds a given array of valid peers to the network
     * @param peers the SMSPeer array to add to the net
     * @throws IllegalArgumentException If at least one peer is null or invalid,
     * or if the array is null
     */
    public void addToNet(SMSPeer[] peers){
        if(peers == null) throw new IllegalArgumentException();
        for(SMSPeer peer: peers) addToNet(peer);
    }

    /**
     * Removes a given String list of peers from the current network
     */
    public void removeFromNet(String peersInNet){
        Log.d(LOG_KEY, "Removing these Peers: " + peersInNet);
        String[] peers = peersInNet.split(" ");
        for(String peer : peers){
            subscribers.remove(new SMSPeer(peer));
        }
    }

    /**
     * Removes every online peer from the net
     * N.B. it doesn't notify anyone about the net, just clears it
     */
    public void clearNet(){
        subscribers.clear();
    }

    /**
     * Returns true if there is no online Peer in the net
     */
    public boolean isNetEmpty(){
        return subscribers.isEmpty();
    }

    /**
     * Returns how many SMSPeers are currently online
     */
    public int networkSize(){
        return subscribers.size();
    }

    /**
     * Updates the current Network State given a peer in the network to update and it's resources
     */
    public void updateNet(String peer, SMSResource[] resources){
        Log.d(LOG_KEY, "Updating this Peer: " + peer);
        SMSPeer peerToUpdate = new SMSPeer(peer);
        subscribers.remove(peerToUpdate);
        subscribers.add(peerToUpdate);
    }

    /**
     * Returns currently online SMSPeers
     */
    public SMSPeer[] getOnlinePeers(){
        return subscribers.toArray(new SMSPeer[0]);
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
                net.acceptLeave(peer, text.substring(2));
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
}
