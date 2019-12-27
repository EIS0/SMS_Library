package com.eis0.networklibrary;

import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;

/**
 * Class that represent a network object. A network is basically a list of peer on which is possible
 * to execute some message related commands.
 *
 * @author Matteo Carnelos
 */
public class Network {

    private ArrayList<SMSPeer> peers;
    private SMSManager smsManager = SMSManager.getInstance();

    Network(ArrayList<SMSPeer> peers) {
        this.peers = peers;
    }

    public ArrayList<SMSPeer> getPeers() {
        return peers;
    }

    public void create() {
        String[] addresses = new String[peers.size()];
        for(int i = 0; i < peers.size(); i++)
            addresses[i] = peers.get(i).getAddress();
        NetworkCommandLine commandLine = new NetworkCommandLine(NetworkCommandLine.CREATE_NETWORK_CMD, addresses);
        linearBroadcast(commandLine);
    }

    public void linearBroadcast(NetworkCommandLine commandLine) {
        for(SMSPeer peer : peers)
            smsManager.sendMessage(new SMSMessage(peer, commandLine.toMessageData()));
    }

    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Network that = (Network)obj;
        return this.peers.equals(that.getPeers());
    }
}
