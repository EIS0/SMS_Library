package com.eis0.networklibrary;

import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Object pool used for the creation of {@link Network} objects. Given a list of peer, the pool
 * choose to create a new network or give an existing one.
 *
 * @author Matteo Carnelos
 */
public class NetworksPool {

    private final static Network LOCALNET = new Network(Collections.<SMSPeer>emptyList());
    public final static String LOCALNET_ADDR = "localnet";

    private static List<Network> networks = new ArrayList<>();

    public static Network obtainNetwork(SMSPeer peer) {
        return obtainNetwork(Collections.singletonList(peer));
    }

    public static Network obtainNetwork(List<SMSPeer> peers) {
        if(peers.isEmpty()) return obtainLocalNetwork();
        Network network = new Network(peers);
        int index = networks.indexOf(network);
        if(index == -1) {
            networks.add(network);
            return network;
        }
        return networks.get(index);
    }

    public static Network obtainLocalNetwork() {
        return LOCALNET;
    }
}
