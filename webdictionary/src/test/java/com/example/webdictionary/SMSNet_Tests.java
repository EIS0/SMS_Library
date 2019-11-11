package com.example.webdictionary;

import com.eis0.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 *
 * @author Edoardo Raimondi
 */
public class SMSNet_Tests {
    @Test
    public void Instantiation_noErrors() {
        SMSNetDictionary net = new SMSNetDictionary();
    }

    @Test
    public void addPeer_CheckIfAdded() {
        SMSNetDictionary net = new SMSNetDictionary();
        SMSPeer peer = new SMSPeer("3423541601");
        SMSResource resource = new SMSResource("photo.png");
        SMSResource[] resources = {resource};
        net.add(peer, resources);
        assertEquals(net.getAvailablePeers()[0], peer);
    }

    @Test
    public void addPeer_NoResource_CheckIfAdded() {
        SMSNetDictionary net = new SMSNetDictionary();
        SMSPeer peer = new SMSPeer("3423541601");
        net.add(peer, null);
        assertEquals(net.getAvailablePeers()[0], peer);
    }

    @Test
    public void addResource_CheckIfAdded() {
        SMSNetDictionary net = new SMSNetDictionary();
        SMSPeer peer = new SMSPeer("3423541601");
        SMSResource resource = new SMSResource("photo.png");
        SMSResource[] resources = {resource};
        net.add(peer, resources);
        assertEquals(net.getAvailableResources()[0], resource);
    }

    @Test
    public void removePeer_CheckNoPeer() {
        SMSNetDictionary net = new SMSNetDictionary();
        SMSPeer peer = new SMSPeer("3423541601");
        SMSResource resource = new SMSResource("photo.png");
        SMSResource[] resources = {resource};
        net.add(peer, resources);
        net.remove(peer);
        assertNull(net.getAvailablePeers());
    }

    @Test
    public void removePeer_CheckNoResources() {
        SMSNetDictionary net = new SMSNetDictionary();
        SMSPeer peer = new SMSPeer("3423541601");
        SMSResource resource = new SMSResource("photo.png");
        SMSResource[] resources = {resource};
        net.add(peer, resources);
        net.remove(peer);
        assertNull(net.getAvailableResources());
    }

    @Test
    public void addResources_CheckPeerResources() {
        SMSNetDictionary net = new SMSNetDictionary();
        SMSPeer peer = new SMSPeer("3423541601");
        SMSResource resource1 = new SMSResource("photo.png");
        SMSResource resource2 = new SMSResource("home.jpg");
        SMSResource resource3 = new SMSResource("test.jpg");
        SMSResource[] resources = {resource1, resource2, resource3};
        net.add(peer, resources);
        for(int i = 0; i < resources.length; i++){
            assertEquals(net.findPeerResources(peer)[i], resources[i]);
        }
    }

    @Test
    public void findPeerWithResource() {
        SMSNetDictionary net = new SMSNetDictionary();
        SMSPeer peer1 = new SMSPeer("3423541601");
        SMSPeer peer2 = new SMSPeer("5554");
        SMSResource resource1 = new SMSResource("photo.png");
        SMSResource resource2 = new SMSResource("home.jpg");
        SMSResource resource3 = new SMSResource("test.jpg");
        SMSResource[] resources1 = {resource1, resource2};
        SMSResource[] resources2 = {resource3};
        net.add(peer1, resources1);
        net.add(peer2, resources2);
        assertEquals(net.findPeerWithResource(resource2), peer1);
    }

    @Test
    public void addReosurcesToAnExistingPeer(){
        SMSNetDictionary net = new SMSNetDictionary();
        SMSPeer peer = new SMSPeer("12345");
        SMSResource resource1 = new SMSResource("photo.png");
        SMSResource resource2 = new SMSResource("home.jpg");
        SMSResource[] resources1 = {resource1};
        SMSResource[] resources2 = {resource2};
        net.add(peer, resources1);
        net.add(peer, resources2);
        SMSPeer[] peers = net.getAvailablePeers();
        assertEquals(peers.length, 1);
    }

}