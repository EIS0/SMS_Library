package com.example.webdictionary;

import com.eis0.smslibrary.SMSPeer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PingTrackerTest {

    private NetworkConnection net;
    private final SMSPeer VALID_PEER = new SMSPeer("3423541601");
    private final SMSPeer VALID_PEER2 = new SMSPeer("3423541602");
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
        Assert.assertTrue(tracker1.isTracking(VALID_PEER));
    }

    @Test
    public void isNotTrackingTheWrongPeer_shouldTrackOne(){
        Assert.assertFalse(tracker1.isTracking(VALID_PEER2));
    }

    @Test
    public void correctlyTicksOnce_noExceptions(){
        tracker1.tick();
        Assert.assertEquals(tracker1.pingsMissing(), MAX_PING_MISSES-1);
    }

    @Test
    public void ticksManyTimes_removesPeer(){
        for(int i = 0; i < MAX_PING_MISSES; i++){
            tracker1.tick();
        }
        SMSPeer[] expected = {VALID_PEER2};
        Assert.assertArrayEquals(net.getOnlinePeers(), expected);
    }

    @Test
    public void reset_StopsDeletion(){
        for(int i = 0; i < MAX_PING_MISSES*2; i++){
            tracker1.tick();
            tracker1.pingReceived();
        }
        SMSPeer[] expected = {VALID_PEER,VALID_PEER2};
        Assert.assertArrayEquals(net.getOnlinePeers(), expected);
    }

    @Test
    public void disable_StopsExecution(){
        tracker1.disable();
        Assert.assertFalse(tracker1.isEnabled());
    }

    @Test
    public void disable_StopsDeletion(){
        tracker1.disable();
        for(int i = 0; i < MAX_PING_MISSES*2; i++){
            tracker1.tick();
        }
        SMSPeer[] expected = {VALID_PEER,VALID_PEER2};
        Assert.assertArrayEquals(net.getOnlinePeers(), expected);
    }

    @Test
    public void multipleTrackers_WorkTogether(){
        for(int i = 0; i < MAX_PING_MISSES; i++){
            tracker1.tick();
            tracker2.tick();
        }
        SMSPeer[] expected = {};
        Assert.assertArrayEquals(net.getOnlinePeers(), expected);
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
        Assert.assertArrayEquals(net.getOnlinePeers(), expected);
    }
}
