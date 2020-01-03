package com.eis0.networklibrary;

import androidx.annotation.NonNull;

import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class used for the creation of {@link Network} objects. Given a list of peer, the pool
 * choose to create a new network or give an existing one.<br>
 * This class improves performance in term of object initializations, saving useless initializations
 * of object already existing. However, in this particular case, the performance boost is reduced a lot
 * because the {@link Network} objects are light and quick to initialize.<br>
 * In a future implementation of distributed networks (e.g. Kademlia), where initialization costs
 * are very high, this class will give a significant performance improvement.<br>
 * This class follows simplified principles of the Object Pool Design Pattern, this because the
 * object release (and consequent deletion) is not actually handled (because actual {@link Network}
 * objects are light and memory efficient).
 *
 * @author Matteo Carnelos
 */
public class NetworksPool {

    // Every device has a LOCALNET already initialized
    private final static Network LOCALNET = new Network(Collections.<SMSPeer>emptyList());

    private static List<Network> networks = new ArrayList<>();

    // ---------------------------- OBJECT CREATION ---------------------------- //

    /**
     * Obtain a one-peer {@link Network} object reference given a single {@link SMSPeer}.
     * The {@link Network} object can be already initialized or it can be created if it doesn't
     * already exist.
     *
     * @param peer The peer in the one-peer network.
     * @return The {@link Network} reference of the requested peer.
     * @author Matteo Carnelos
     */
    public static Network obtainNetwork(@NonNull SMSPeer peer) {
        return obtainNetwork(Collections.singletonList(peer));
    }

    /**
     * Obtain a {@link Network} object reference given a list of {@link SMSPeer}.
     * The object can be already initialized or it can be created if it doesn't already exist. If
     * an empty list is given the returned network will be the LOCALNET.
     *
     * @param peers The {@link List<SMSPeer>} list.
     * @return The {@link Network} reference of the requested peers.
     * @author Matteo Carnelos
     */
    public static Network obtainNetwork(@NonNull List<SMSPeer> peers) {
        if(peers.isEmpty()) return obtainLocalNetwork();
        Network network = new Network(peers);
        int index = networks.indexOf(network);
        if(index == -1) {
            networks.add(network);
            return network;
        }
        network = networks.get(index);
        return network;
    }

    /**
     * Obtain the LOCALNET for this device.
     *
     * @return The {@link #LOCALNET} for this device.
     * @author Matteo Carnelos
     */
    public static Network obtainLocalNetwork() {
        return LOCALNET;
    }
}
