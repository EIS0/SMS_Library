package com.example.webdictionary;

import com.eis0.smslibrary.SMSPeer;

import java.util.TimerTask;

public class PingTracker extends TimerTask {
    private int maxPingMisses;
    private int pingsMissed = 0;
    private boolean shouldRun = true;
    private SMSPeer toTrack;
    private NetworkConnection net;

    /**
     * Constructor for the timer object
     * @param net The net where the timer is operating
     * @param toTrack The SMSPeer to keep track of
     * @param maxPingMisses The maximum number of misses you can do before being considered disconnected
     */
    PingTracker(NetworkConnection net, SMSPeer toTrack, int maxPingMisses){
        this.net = net;
        this.toTrack = toTrack;
        this.maxPingMisses = maxPingMisses;
    }

    /**
     * Function called by the timer class, checking if the ping is missed too many times
     */
    @Override
    public void run() {
        if(shouldRun){
            pingsMissed++;
            if(pingsMissed >= maxPingMisses){
                //the user disconnected
                net.removeFromNet(toTrack);
            }
        }
    }

    /**
     * Resets the counter for the ping misses
     */
    void pingReceived(){
        if(shouldRun) pingsMissed = 0;
    }

    /**
     * Disables the timer checks
     */
    void disable(){
        shouldRun = false;
    }

    /**
     * Re-enables the timer checks
     */
    void enable(){
        shouldRun = true;
    }

    /**
     * Checks if a given valid peer is being tracked
     * @param peer The valid peer to check
     * @return Returns true if this PingTracker is tracking the given peer, false otherwise
     * @throws IllegalArgumentException if the peer is null or invalid
     */
    boolean isTracking(SMSPeer peer){
        if(peer == null || !peer.isValid()) throw new IllegalArgumentException();
        return toTrack == peer;
    }
}