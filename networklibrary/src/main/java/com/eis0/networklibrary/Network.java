package com.eis0.networklibrary;

import androidx.annotation.NonNull;

import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that represent a network object. A network is basically a list of peer on which is possible
 * to execute some message related commands.
 *
 * @author Matteo Carnelos
 */
public class Network {

    private List<SMSPeer> peers;

    Network(@NonNull List<SMSPeer> peers) {
        this.peers = peers;
    }

    public List<SMSPeer> getPeers() {
        return peers;
    }

    public List<String> getAddresses() {
        List<String> addresses = new ArrayList<>();
        for(SMSPeer peer : getPeers()) addresses.add(peer.getAddress());
        return addresses;
    }

    public Network getSubNetForPeer(SMSPeer peer) {
        ArrayList<SMSPeer> subPeers = new ArrayList<>(getPeers());
        subPeers.remove(peer);
        return NetworksPool.obtainNetwork(subPeers);
    }

    public void unicastMessage(SMSPeer peer, String message) {
        multicastMessage(Collections.singletonList(peer), message);
    }

    public void multicastMessage(List<SMSPeer> peers, String message) {
        if(!getPeers().containsAll(peers))
            throw new IllegalArgumentException("One or more of the given peers are not part of the network");
        for(SMSPeer peer : peers)
            SMSManager.getInstance().sendMessage(new SMSMessage(peer, message));
    }

    public void broadcastMessage(String message) {
        multicastMessage(peers, message);
    }

    public int size() {
        return peers.size();
    }

    public boolean isLocalNetwork() {
        return size() == 0;
    }

    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Network that = (Network)obj;
        return this.size() == that.size() && this.peers.containsAll(that.getPeers());
    }
}
