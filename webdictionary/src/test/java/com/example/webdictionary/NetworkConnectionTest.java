package com.example.webdictionary;

import com.eis0.smslibrary.SMSPeer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class NetworkConnectionTest {

    private NetworkConnection instance;
    private final SMSPeer PEER1 = new SMSPeer("5551");
    private final SMSPeer PEER2 = new SMSPeer("5552");
    private final SMSPeer PEER3 = new SMSPeer("5553");
    private final SMSPeer PEER4 = new SMSPeer("5554");
    private final SMSPeer[] PEERS = {PEER1,PEER2,PEER3,PEER4};

    @Before
    public void setup() {
        instance = NetworkConnection.getInstance(PEER1);
        instance.clearNet();
    }

    @Test
    public void validNetworkInstance_noErrors(){
        NetworkConnection.getInstance(PEER1);
    }

    @Test
    public void singletonPattern(){
        NetworkConnection inst1 = NetworkConnection.getInstance(null);
        NetworkConnection inst2 = NetworkConnection.getInstance(null);
        assertEquals(inst1, inst2);
    }

    @Test
    public void networkInstance_nullPeerInConstructor_noErrors(){
        NetworkConnection.getInstance(null);
    }

    @Test(expected = ExceptionInInitializerError.class)
    public void netJoinRequest_noErrors(){
        //the peer passed goes through a lot of classes, ending in SMSCore
        //since I cannot mock the SmsManager call I can at least know that it reached
        //that point thanks to that exception
        NetworkConnection.getInstance(null).askToJoin(PEER1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void netJoinRequest_invalidPeer_errors(){
        NetworkConnection.getInstance(null).askToJoin(new SMSPeer("asdf"));
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void netJoinRequest_nullPeer_errors(){
        NetworkConnection.getInstance(null).askToJoin(null);
        fail();
    }

    //region AddingPeers

    @Test
    public void correctNetworkSize(){
        instance.addToNet(PEERS);
        assertEquals(instance.networkSize(), 4);
    }

    @Test
    public void addManyPeers(){
        instance.addToNet(PEERS);
        SMSPeer[] peersOnline = instance.getOnlinePeers();
        assertArrayEquals(PEERS, peersOnline);
    }

    @Test
    public void addMultipleEqualPeers_correctNetworkSize(){
        SMSPeer[] peers = {PEER1,PEER2,PEER1,PEER2};
        instance.addToNet(peers);
        assertEquals(instance.networkSize(), 2);
    }

    @Test
    public void addMultipleEqualPeers_correctNetworkList() {
        SMSPeer[] totalPeers = {PEER1, PEER2, PEER1, PEER2};
        SMSPeer[] actualPeers = {PEER1, PEER2};
        instance.addToNet(totalPeers);
        assertArrayEquals(instance.getOnlinePeers(), actualPeers);
    }

    @Test
    public void addPeerToNet_onlyOne(){
        instance.addToNet(PEER1);
        assertEquals(instance.networkSize(), 1);
    }

    @Test
    public void addPeerToNet_sameOne(){
        instance.addToNet(PEER1);
        assertEquals(instance.getOnlinePeers()[0],PEER1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPeerToNet_invalidOne(){
        SMSPeer toNet = new SMSPeer("asdf");
        instance.addToNet(toNet);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPeerToNet_emptyOne(){
        SMSPeer toNet = new SMSPeer("");
        instance.addToNet(toNet);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPeerToNet_nullAddress(){
        SMSPeer toNet = new SMSPeer(null);
        instance.addToNet(toNet);
        fail();
    }

    @Test
    public void addStringPeerToNet_sameOne(){
        instance.addToNet("5551");
        assertEquals(instance.getOnlinePeers()[0],PEER1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addStringPeerToNet_invalidOne(){
        instance.addToNet("asdf");
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addStringPeerToNet_emptyOne(){
        instance.addToNet("");
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addStringPeerToNet_nullOne(){
        instance.addToNet((String)null);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addListPeerToNet_nullOne(){
        instance.addToNet((SMSPeer[])null);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addListPeerToNet_emptyOne(){
        SMSPeer toNet = new SMSPeer(null);
        instance.addToNet((SMSPeer[])null);
        fail();
    }

    //endregion

    //region RemovePeers

    @Test
    public void removePeerFromNet(){
        instance.addToNet(PEER1);
        instance.removeFromNet(PEER1);
        assertTrue(instance.isNetEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removePeerFromNet_invalid(){
        SMSPeer peer = new SMSPeer("asdf");
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removePeerFromNet_nullPeer(){
        SMSPeer peer = new SMSPeer(null);
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removePeerFromNet_null(){
        instance.addToNet((SMSPeer)null);
        instance.removeFromNet(PEER1);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removePeerFromNet_empty(){
        SMSPeer peer = new SMSPeer("");
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        fail();
    }

    @Test
    public void removeStringPeerFromNet(){
        String peer = "5554";
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        assertTrue(instance.isNetEmpty());
    }

    @Test
    public void removeStringPeerArrayFromNet(){
        String peer = "5554 5556";
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        assertTrue(instance.isNetEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeStringPeerFromNet_invalid(){
        String peer = "asdf";
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeStringPeerFromNet_nullPeer(){
        String peer = null;
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeStringPeerFromNet_empty(){
        String peer = "";
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        fail();
    }

    @Test
    public void removeArrayPeerFromNet(){
        SMSPeer[] peers = {PEER1};
        instance.addToNet(peers);
        instance.removeFromNet(peers);
        assertTrue(instance.isNetEmpty());
    }

    @Test
    public void removeArrayPeerFromNet_moreThanOne(){
        instance.addToNet(PEERS);
        instance.removeFromNet(PEERS);
        assertTrue(instance.isNetEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeArrayPeerFromNet_invalid(){
        SMSPeer[] peers = {new SMSPeer("asdf"), PEER1};
        instance.addToNet(peers);
        instance.removeFromNet(peers);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeArrayPeerFromNet_nullPeer(){
        SMSPeer[] peer = null;
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeArrayPeerFromNet_empty(){
        SMSPeer[] peers = {new SMSPeer(""), PEER1};
        instance.addToNet(peers);
        instance.removeFromNet(peers);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeArrayPeerFromNet_null(){
        SMSPeer[] peers = {null, PEER1};
        instance.addToNet(peers);
        instance.removeFromNet(peers);
        fail();
    }
    //endregion
}
