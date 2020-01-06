package com.eis0.networklibrary;

import androidx.annotation.NonNull;

import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that represent a replicated network object. A replicated network is basically a list of
 * peers on which is possible to execute some message related commands like unicasting,
 * multicasting and broadcasting. Every node of the network has an exact copy of all the resources
 * and nodes in the network and so the resource searches are made locally.<br>
 * A network without peers is said to be a LOCALNET, a network that includes only the local
 * host.<br>
 * More formally, every network is composed by the local host and a list of peers, if the latter is
 * empty, the network became a LOCALNET.<br>
 * The creation of {@link Network} objects is managed by the {@link NetworksPool}.
 *
 * @author Matteo Carnelos
 */
public class Network {

    // Every network is addressed by the combinations of all peers, but for the LOCALNET, where the
    // are no peers, is given a special address
    public final static String LOCALNET_ADDR = "localnet";

    private List<SMSPeer> peers;

    // ---------------------------- CONSTRUCTORS ---------------------------- //

    /**
     * Create a {@link Network} object given a list of {@link SMSPeer}.
     *
     * @param peers The {@link List<SMSPeer>} list.
     * @author Matteo Carnelos
     */
    Network(@NonNull List<SMSPeer> peers) {
        this.peers = peers;
    }

    // ---------------------------- GETTERS ---------------------------- //

    /**
     * Get the peers in the network. For the LOCALNET an empty list is returned.
     *
     * @return The {@link List<SMSPeer>} containing all the peers in the network.
     * @author Matteo Carnelos
     */
    public List<SMSPeer> getPeers() {
        return peers;
    }

    /**
     * Get a list of all the addresses of the peers in the network. If the network is a LOCALNET,
     * meaning that there are no peers in the network, an empty address is returned.
     *
     * @return The {@link List<String>} containing all the peer's addresses in the network.
     * @author Matteo Carnelos
     */
    public List<String> getAddresses() {
        List<String> addresses = new ArrayList<>();
        if(isLocalNetwork()) addresses.add("");
        for(SMSPeer peer : getPeers()) addresses.add(peer.getAddress());
        return addresses;
    }

    // ---------------------------- COMMUNICATION / RESOURCES UPDATING ---------------------------- //

    /**
     * Unicast (one to one) a message to a peer of the network.
     *
     * @param peer The peer in the {@link Network}.
     * @param message The message to send.
     * @throws IllegalArgumentException If the given peer is not part of the network.
     * @author Matteo Carnelos
     */
    public void unicastMessage(@NonNull SMSPeer peer, @NonNull String message) {
        multicastMessage(Collections.singletonList(peer), message);
    }

    /**
     * Multicast (one to many) a message to some peers of the network.
     *
     * @param peers The {@link List<SMSPeer>} in the {@link Network}.
     * @param message The message to send.
     * @throws IllegalArgumentException If at least one of the given peers is not part of the network.
     * @author Matteo Carnelos
     */
    public void multicastMessage(@NonNull List<SMSPeer> peers, @NonNull String message) {
        if(!getPeers().containsAll(peers))
            throw new IllegalArgumentException("The network does not contains the given peers.");
        for(SMSPeer peer : peers)
            SMSManager.getInstance().sendMessage(new SMSMessage(peer, message));
    }

    /**
     * Broadcast (one to all) a message to all the peers of the network.
     *
     * @param message The message to send.
     * @author Matteo Carnelos
     */
    public void broadcastMessage(@NonNull String message) {
        multicastMessage(peers, message);
    }

    // ---------------------------- INSPECTIONS ---------------------------- //

    /**
     * Get the size of the network, that is the number of peers into it. For the LOCALNET, the size
     * is equals to zero.
     *
     * @return The size as an integer value.
     * @author Matteo Carnelos
     */
    public int size() {
        return peers.size();
    }

    /**
     * Tell if the network is the LOCALNET, menaing that there are no peers.<br>
     * More formally, a {@link Network} is said to be a local network if the size is equal to zero.
     *
     * @return True if the network object is a LOCALNET, false otherwise.
     * @author Matteo Carnelos
     */
    public boolean isLocalNetwork() {
        return size() == 0;
    }

    // ---------------------------- OVERRIDDEN METHODS ---------------------------- //

    /**
     * Compare two {@link Network} objects and tell if they are equals. Two {@link Network} are said
     * to be equals if they have the same list of peers, ignoring the order.
     *
     * @param obj The object to compare.
     * @return True if the two objects are equal, false otherwise.
     * @author Matteo Carnelos
     */
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Network that = (Network)obj;
        return this.size() == that.size() && this.peers.containsAll(that.getPeers());
    }
}
