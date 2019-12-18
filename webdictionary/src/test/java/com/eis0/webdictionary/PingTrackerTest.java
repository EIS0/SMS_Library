package com.eis0.webdictionary;

import com.eis.smslibrary.SMSPeer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PingTrackerTest {

    private NetworkConnection net;
    private final SMSPeer VALID_PEER = new SMSPeer("+393423541601");
    private final SMSPeer VALID_PEER2 = new SMSPeer("+393423541602");
    private PingTracker tracker1;
    private PingTracker tracker2;
    private final int MAX_PING_MISSES = 4;

    @Before
    public void setupTests(){
        net = NetworkConnection.getInstance(null);
        net.clearNet();
        net.addToNet(VALID_PEER);
        net.addToNet(VALID_PEER2);
        tracker1 = new PingTracker(net, VALID_PEER, MAX_PING_MISSES);
        tracker2 = new PingTracker(net, VALID_PEER2, MAX_PING_MISSES);
    }

    @Test
    public void isTrackingThePeer_shouldBeTracking(){
        assertTrue(tracker1.isTracking(VALID_PEER));
    }

    @Test
    public void isNotTrackingTheWrongPeer_shouldTrackOne(){
        assertFalse(tracker1.isTracking(VALID_PEER2));
    }

    @Test(expected = NullPointerException.class)
    public void isTrackingNullPeer_throws(){
        assertFalse(tracker1.isTracking(null));
    }

    @Test
    public void correctlyTicksOnce_noExceptions(){
        tracker1.tick();
        assertEquals(tracker1.pingsMissing(), MAX_PING_MISSES-1);
    }

    @Test
    public void ticksManyTimes_removesPeer(){
        for(int i = 0; i < MAX_PING_MISSES; i++){
            tracker1.tick();
        }
        SMSPeer[] expected = {VALID_PEER2};
        assertArrayEquals(net.getOnlinePeers(), expected);
    }

    @Test
    public void reset_StopsDeletion(){
        for(int i = 0; i < MAX_PING_MISSES*2; i++){
            tracker1.tick();
            tracker1.pingReceived();
        }
        SMSPeer[] expected = {VALID_PEER,VALID_PEER2};
        assertArrayEquals(net.getOnlinePeers(), expected);
    }

    @Test
    public void disable_StopsExecution(){
        tracker1.disable();
        assertFalse(tracker1.isEnabled());
    }

    @Test
    public void disable_StopsDeletion(){
        tracker1.disable();
        for(int i = 0; i < MAX_PING_MISSES*2; i++){
            tracker1.tick();
        }
        SMSPeer[] expected = {VALID_PEER,VALID_PEER2};
        assertArrayEquals(net.getOnlinePeers(), expected);
    }

    @Test
    public void multipleTrackers_WorkTogether(){
        for(int i = 0; i < MAX_PING_MISSES; i++){
            tracker1.tick();
            tracker2.tick();
        }
        SMSPeer[] expected = {};
        assertArrayEquals(net.getOnlinePeers(), expected);
    }

    @Test
    public void disableThenReenabled_conbinedShouldWork(){
        tracker1.disable();
        for(int i = 0; i < MAX_PING_MISSES*2; i++){
            tracker1.tick();
        }
        tracker1.enable();
        for(int i = 0; i < MAX_PING_MISSES*2; i++){
            tracker1.tick();
        }
        SMSPeer[] expected = {VALID_PEER2};
        assertArrayEquals(net.getOnlinePeers(), expected);
    }
}
