package com.eis0.networklibrary;

import com.eis0.smslibrary.SMSPeer;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.eis0.networklibrary.NetworkTest.VALID_PEER_1;
import static com.eis0.networklibrary.NetworkTest.VALID_PEER_2;
import static com.eis0.networklibrary.NetworkTest.VALID_PEER_3;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

/**
 * Test class for {@link NetworksPool}.
 *
 * @author Matteo Carnelos
 */
public class NetworksPoolTest {

    private static List<SMSPeer> testPeers = new ArrayList<>();

    @BeforeClass
    public static void setup() {
        testPeers.add(VALID_PEER_1);
        testPeers.add(VALID_PEER_2);
        testPeers.add(VALID_PEER_3);
    }

    @Test
    public void validNetworks_areObtained() {
        Network testNetwork1 = NetworksPool.obtainNetwork(testPeers);
        Collections.shuffle(testPeers);
        Network testNetwork2 = NetworksPool.obtainNetwork(testPeers);
        Network testNetwork3 = NetworksPool.obtainNetwork(VALID_PEER_1);
        assertEquals(testNetwork1, testNetwork2);
        assertSame(testNetwork1, testNetwork2);
        assertNotEquals(testNetwork1, testNetwork3);
    }

    @Test
    public void localNetwork_isObtained() {
        Network testNetwork1 = NetworksPool.obtainLocalNetwork();
        Network testNetwork2 = NetworksPool.obtainNetwork(Collections.<SMSPeer>emptyList());
        assertEquals(testNetwork1, testNetwork2);
        assertSame(testNetwork1, testNetwork2);
    }
}
