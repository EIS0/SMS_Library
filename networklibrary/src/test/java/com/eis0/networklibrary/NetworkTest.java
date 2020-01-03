package com.eis0.networklibrary;

import com.eis0.smslibrary.SMSPeer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link Network}.
 *
 * @author Matteo Carnelos
 */
public class NetworkTest {

    private static final String VALID_MESSAGE = "Test";
    static final SMSPeer VALID_PEER_1 = new SMSPeer("3401234567");
    static final SMSPeer VALID_PEER_2 = new SMSPeer("3401234568");
    static final SMSPeer VALID_PEER_3 = new SMSPeer("3401234569");

    private static List<SMSPeer> testPeers = new ArrayList<>();
    private static List<String> testAddresses = new ArrayList<>();

    @Before
    public void setup() {
        testPeers.clear();
        testPeers.add(VALID_PEER_1);
        testPeers.add(VALID_PEER_2);
        testPeers.add(VALID_PEER_3);
        testAddresses.add(VALID_PEER_1.getAddress());
        testAddresses.add(VALID_PEER_2.getAddress());
        testAddresses.add(VALID_PEER_3.getAddress());
    }

    @Test
    public void validNetwork_isCreated() {
        Network testNetwork = NetworksPool.obtainNetwork(testPeers);
        assertEquals(testPeers, testNetwork.getPeers());
        assertEquals(testAddresses, testNetwork.getAddresses());
        assertEquals(3, testNetwork.size());
        assertFalse(testNetwork.isLocalNetwork());
    }

    @Test
    public void localNetwork_isCreated() {
        Network testNetwork = NetworksPool.obtainLocalNetwork();
        assertTrue(testNetwork.isLocalNetwork());
        assertTrue(testNetwork.getPeers().isEmpty());
        assertTrue(testNetwork.getAddresses().get(0).isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void validMessageToExternalPeer_isNotUnicasted() {
        testPeers.remove(VALID_PEER_3);
        Network testNetwork = NetworksPool.obtainNetwork(testPeers);
        testNetwork.unicastMessage(VALID_PEER_3, VALID_MESSAGE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validMessageToExternalPeers_isNotMulticasted() {
        testPeers.remove(VALID_PEER_3);
        Network testNetwork = NetworksPool.obtainNetwork(testPeers);
        List<SMSPeer> testPeers = new ArrayList<>();
        testPeers.add(VALID_PEER_1);
        testPeers.add(VALID_PEER_3);
        testNetwork.multicastMessage(testPeers, VALID_MESSAGE);
    }

    @Test(expected = NullPointerException.class)
    public void validMessage_isBroadcasted() {
        Network testNetwork = NetworksPool.obtainNetwork(testPeers);
        testNetwork.broadcastMessage(VALID_MESSAGE);
    }
}
