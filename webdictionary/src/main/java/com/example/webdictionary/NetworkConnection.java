package com.example.webdictionary;

import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.Timer;

/**
 * @author Marco Cognolato except where otherwise indicated
 */
public class NetworkConnection {

    //Singleton Design Pattern https://refactoring.guru/design-patterns/singleton
    private static NetworkConnection net;

    private NetworkConnection(SMSPeer myPeer){
        if(myPeer != null){
            if(myPeer.isValid()){
                subscribers.add(myPeer);
            }
        }
        NetworkListener listener = new NetworkListener(this);
        SMSManager.getInstance().addReceiveListener(listener);
    }

    public static NetworkConnection getInstance(SMSPeer myPeer){
        if(net == null){
            net = new NetworkConnection(myPeer);
        }
        return net;
    }

    private ArrayList<SMSPeer> subscribers = new ArrayList<>();
    private ArrayList<PingTracker> tracking = new ArrayList<>();
    private final int TRACKING_TIME = 1000 * 60 * 10; //1000 ms (1s) * 60s (1min) * 10 (10mins)
    private SMSNetVocabulary vocabulary = new SMSNetVocabulary();

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

        SMSManager.getInstance().sendMessage(message);
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
        String[] newPeersOnNet_asString = text.split(" ");
        SMSPeer[] newPeersOnNet = new SMSPeer[newPeersOnNet_asString.length];
        for(int i = 0; i < newPeersOnNet_asString.length; i++)
            newPeersOnNet[i] = new SMSPeer(newPeersOnNet_asString[i]);

        SMSPeer[] oldPeersInNet = subscribers.toArray(new SMSPeer[0]);
        //add new peers to my net
        addToNet(text);
        addToNet(oldPeersInNet);
        //notify new peers of my old peers
        partCast(newPeersOnNet, RequestType.AddPeers.ordinal() + " " + oldPeersInNet);
        //notify my old peers about the new ones
        partCast(oldPeersInNet, RequestType.AddPeers.ordinal() + " " + text);
    }

    //endregion

    //region BroadcastMethods

    /**
     * Broadcasts a valid message to the whole network
     * @param message The message to broadcast
     */
    public void broadcast(String message){
        partCast(subscribers.toArray(new SMSPeer[0]), message);
    }

    /**
     * Sends a message to a small part of users
     * @param peers The list of peers to send the message to
     * @param message The message to send
     */
    public void partCast(SMSPeer[] peers, String message){
        for(SMSPeer peer : peers){
            SMSMessage wholeMessage = new SMSMessage(peer, message);
            SMSManager.getInstance().sendMessage(wholeMessage);
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
        SMSManager.getInstance().sendMessage(message);
    }

    /**
     * Sends to the network a notification that a given valid SMSPeer exited the network
     * @param peer The Peer who just left this network
     */
    void acceptLeave(SMSPeer peer){
        //remove the peer
        removeFromNet(peer.getAddress());
        //notify my old peers about the exit
        broadcast(RequestType.RemovePeers.ordinal() + " " + peer.getAddress());
    }

    //endregion

    //region PingMethods

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
        SMSManager.getInstance().sendMessage(message);
    }

    /**
     * Function called when a Ping is received from an SMSPeer,
     * Resets the Ping timer before considering that peer offline
     * @param peer The peer who sent the ping
     */
    void incomingPing(SMSPeer peer){
        //search through enabled trackers if one is tracking this peer
        for(PingTracker tracker: tracking){
            if(tracker.isTracking(peer)){
                //if it is, then ping received, so skip the others
                tracker.pingReceived();
                return;
            }
        }

        //if I'm here it means there's no tracker for this peer, create a new one
        PingTracker tracker = new PingTracker(this, peer, 4);
        Timer timer = new Timer();
        timer.schedule(tracker, 0,TRACKING_TIME);
        tracking.add(tracker);
    }

    //endregion

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

    //region addToDict

    /**
     * Adds to the dictionary a key-value pair, then broadcasts the operation to every subscriber
     * @param key The key to add to the dictionary (and broadcast to all subscribers)
     * @param value The value to add to the dictionary with the key (and broadcast)
     */
    public void addToDictionary(SerializableObject key, SerializableObject value){
        addToDictionaryNoCast(key, value);
        broadcast(RequestType.AddToDict.ordinal() + " " + key.serialize() + " " + value.serialize());
    }

    /**
     * Adds a key-value couple to the vocabulary without broadcasting to the whole net
     */
    void addToDictionaryNoCast(SerializableObject key, SerializableObject value){
        vocabulary.add(key, value);
    }

    /**
     * Adds a key-value couple to the vocabulary without broadcasting to the whole net starting from a String
     */
    void addToDictionaryNoCast(String text){
        String[] objects = text.split(" ");
        SMSSerialization key = new SMSSerialization(objects[0]);
        SMSSerialization value = new SMSSerialization(objects[1]);
        addToDictionaryNoCast(key, value);
    }
    //endregion

    //region removeFromDict
    /**
     * Removes from the dictionary a key, then broadcasts the operation to every subscriber
     * @param key The key to remove from the dictionary (and broadcast to all subscribers)
     */
    public void removeFromDictionary(SerializableObject key){
        removeFromDictionaryNoCast(key);
        broadcast(RequestType.RemoveFromDict.ordinal() + " " + key.serialize());
    }

    /**
     * Removes a key from the vocabulary without broadcasting to the whole net
     */
    void removeFromDictionaryNoCast(SerializableObject key){
        vocabulary.remove(key);
    }

    /**
     * Removes a key from the vocabulary without broadcasting to the whole net starting from a String
     */
    void removeFromDictionaryNoCast(String text){
        String[] objects = text.split(" ");
        SMSSerialization key = new SMSSerialization(objects[0]);
        removeFromDictionaryNoCast(key);
    }
    //endregion

    //region updateDict
    /**
     * Removes from the dictionary a key, then broadcasts the operation to every subscriber
     * @param key The key to remove from the dictionary (and broadcast to all subscribers)
     */
    public void updateDictionary(SerializableObject key, SerializableObject value){
        updateDictionaryNoCast(key, value);
        broadcast(RequestType.RemoveFromDict.ordinal() + " " + key.serialize() + " " + value.serialize());
    }

    /**
     * Removes a key from the vocabulary without broadcasting to the whole net
     */
    void updateDictionaryNoCast(SerializableObject key, SerializableObject value){
        vocabulary.update(key, value);
    }

    /**
     * Removes a key from the vocabulary without broadcasting to the whole net starting from a String
     */
    void updateDictionaryNoCast(String text){
        String[] objects = text.split(" ");
        SMSSerialization key = new SMSSerialization(objects[0]);
        SMSSerialization value = new SMSSerialization(objects[1]);
        updateDictionaryNoCast(key, value);
    }
    //endregion

    //region getFromDict

    /**
     * Returns the Resource associated with the given valid key
     */
    public SerializableObject getResource(SerializableObject key){
        return vocabulary.getResource(key);
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
}