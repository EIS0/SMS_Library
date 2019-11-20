package com.example.webdictionary;

import android.content.Context;
import android.util.Log;

import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;

/**
 * @author Marco Cognolato except where otherwise indicated
 */
public class NetworkConnection {

    //Singleton Design Pattern https://refactoring.guru/design-patterns/singleton
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

    //region JoinMethods
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
    void acceptJoin(SMSPeer linkPeer, String text){
        String[] newPeersOnNet = text.split(" ");
        SMSPeer[] oldPeersInNet = subscribers.toArray(new SMSPeer[0]);
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
        for(SMSPeer oldPeer : oldPeersInNet){
            Log.d(LOG_KEY, "Old Peer: " + oldPeer.getAddress());
            SMSManager.getInstance(context).sendMessage(new SMSMessage(oldPeer, RequestType.AddPeers.ordinal() + " " + text));
        }
    }

    //endregion

    //region LeaveMethods
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
     * Sends to the network a notification that a given valid SMSPeer exited the network
     * @param peer The Peer who just left this network
     */
    void acceptLeave(SMSPeer peer){
        //remove the peer
        removeFromNet(peer.getAddress());
        //notify my old peers about the exit
        for(SMSPeer oldPeer : subscribers){
            Log.d(LOG_KEY, "Old Peer: " + oldPeer.getAddress());
            SMSManager.getInstance(context).sendMessage(new SMSMessage(oldPeer, RequestType.RemovePeers.ordinal() + " " + peer.getAddress()));
        }
    }

    //endregion

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
    void incomingPing(SMSPeer peer){
        //TODO: implement this (using timers?)
    }

    //region addToNet
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
    //endregion

    //region removeFromNet
    /**
     * Removes a given String list of peers from the current network
     * @param peersInNet a space separated string of valid SMSPeers
     * @throws IllegalArgumentException if peersInNet is null or if at least
     * one SMSPeer is null or invalid
     */
    public void removeFromNet(String peersInNet){
        if(peersInNet == null) throw new IllegalArgumentException();
        Log.d(LOG_KEY, "Removing these Peers: " + peersInNet);
        String[] peers = peersInNet.split(" ");
        for(String peer : peers){
            removeFromNet(new SMSPeer(peer));
        }
    }

    /**
     * Removes a given SMSPeer from the current network
     * @param peer The SMSPeer to remove from the net
     * @throws IllegalArgumentException if peer is null or invalid
     */
    public void removeFromNet(SMSPeer peer){
        if(peer == null || !peer.isValid()) throw new IllegalArgumentException();
        Log.d(LOG_KEY, "Removing this Peer: " + peer);
        subscribers.remove(peer);
    }

    /**
     * Removes an array of valid SMSPeers from the current network
     * @param peers The list of Peers to remove
     * @throws IllegalArgumentException if peers is null, or if at least one SMSPeer
     * is null or invalid
     */
    public void removeFromNet(SMSPeer[] peers){
        if(peers == null) throw new IllegalArgumentException();
        for (SMSPeer peer : peers) removeFromNet(peer);
    }
    //endregion

    //region helperMethods
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
     * Returns an array of currently online SMSPeers
     */
    public SMSPeer[] getOnlinePeers(){
        return subscribers.toArray(new SMSPeer[0]);
    }

    //endregion

    /**
     * Updates the current Network State of a Peer
     * N.B. Since the lesson with the professor this method should not work this way
     * @param peer The peer to update his resources
     * @param resources The resources of the Peer
     */
    public void updateNet(String peer, SMSResource[] resources){
        Log.d(LOG_KEY, "Updating this Peer: " + peer);
        SMSPeer peerToUpdate = new SMSPeer(peer);
        subscribers.remove(peerToUpdate);
        subscribers.add(peerToUpdate);
    }
}
