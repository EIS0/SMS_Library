package com.eis0.networklibrary;

import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represent a network object. A network is basically a list of peer on which is possible
 * to execute some message related commands.
 *
 * @author Matteo Carnelos
 */
public class Network {

    private List<SMSPeer> peers;
    private SMSManager smsManager = SMSManager.getInstance();

    Network(List<SMSPeer> peers) {
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
        Network subNet = new Network(getPeers());
        subNet.getPeers().remove(peer);
        return subNet;
    }

    public void unicastMessage(SMSPeer peer, String message) {
        smsManager.sendMessage(new SMSMessage(peer, message));
    }

    public void broadcastMessage(String message) {
        for(SMSPeer peer : peers)
            smsManager.sendMessage(new SMSMessage(peer, message));
    }

    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Network that = (Network)obj;
        return this.peers.equals(that.getPeers());
    }
}
