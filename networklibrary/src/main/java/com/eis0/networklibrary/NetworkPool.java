package com.eis0.networklibrary;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;

/**
 * Object pool used for the creation of {@link Network} objects. Given a list of peer, the pool
 * choose to create a new network or give an existing one.
 *
 * @author Matteo Carnelos
 */
public class NetworkPool implements ReceivedMessageListener<SMSMessage> {

    private static NetworkPool instance = null;
    private ArrayList<Network> networks = new ArrayList<>();

    private NetworkPool() {
        SMSManager.getInstance().addReceiveListener(this);
    }

    public static NetworkPool getInstance() {
        if (instance == null) instance = new NetworkPool();
        return instance;
    }

    public Network obtainNetwork(ArrayList<SMSPeer> peers) {
        Network network = new Network(peers);
        int index = networks.indexOf(network);
        if(index == -1) {
            networks.add(network);
            network.create();
            return network;
        } else return networks.get(index);
    }

    public void onMessageReceived(SMSMessage message) {
        String data = message.getData();
        NetworkCommandLine networkCommandLine = NetworkCommandLine.parseCommandLine(data);
        String cmd = networkCommandLine.getCommand();
        String[] args = networkCommandLine.getArguments();
        switch(cmd) {
            case NetworkCommandLine.CREATE_NETWORK_CMD:
                ArrayList<SMSPeer> peers = new ArrayList<>();
                for(String address : args)
                    peers.add(new SMSPeer(address));
                Network network = new Network(peers);
                if(!networks.contains(network)) networks.add(network);
                break;
        }
    }
}
