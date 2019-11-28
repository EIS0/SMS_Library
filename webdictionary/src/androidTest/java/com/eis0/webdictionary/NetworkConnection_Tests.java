package com.eis0.webdictionary;

import android.Manifest;
import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import com.eis0.smslibrary.SMSPeer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class NetworkConnection_Tests {

    @Rule public GrantPermissionRule sendSMSRule = GrantPermissionRule.grant(Manifest.permission.SEND_SMS);
    @Rule public GrantPermissionRule receiveSMSRule = GrantPermissionRule.grant(Manifest.permission.RECEIVE_SMS);
    @Rule public GrantPermissionRule readPhoneStateRule = GrantPermissionRule.grant(Manifest.permission.READ_PHONE_STATE);
    @Rule public GrantPermissionRule readSMSRule = GrantPermissionRule.grant(Manifest.permission.READ_SMS);

    private Context appContext;
    private NetworkConnection instance;

    @Before
    public void useAppContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        instance = NetworkConnection.getInstance(appContext, new SMSPeer("5554"));
        instance.clearNet();
    }

    @Test
    public void checkContext(){
        assertEquals("com.eis0.webdictionary.test", appContext.getPackageName());
    }

    @Test
    public void validNetworkInstance_noErrors(){
        NetworkConnection.getInstance(appContext, new SMSPeer("5554"));
    }

    @Test
    public void singletonPattern(){
        NetworkConnection inst1 = NetworkConnection.getInstance(appContext, null);
        NetworkConnection inst2 = NetworkConnection.getInstance(appContext, null);
        assertEquals(inst1, inst2);
    }

    @Test
    public void networkInstance_nullPeerInConstructor_noErrors(){
        NetworkConnection.getInstance(appContext, null);
    }

    @Test
    public void netJoinRequest_noErrors(){
        NetworkConnection.getInstance(appContext, null).askToJoin(new SMSPeer("5556"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void netJoinRequest_invalidPeer_errors(){
        NetworkConnection.getInstance(appContext, null).askToJoin(new SMSPeer("asdf"));
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void netJoinRequest_nullPeer_errors(){
        NetworkConnection.getInstance(appContext, null).askToJoin(null);
        fail();
    }

    @Test
    public void correctNetworkSize(){
        instance.clearNet();
        SMSPeer peer1 = new SMSPeer("1");
        SMSPeer peer2 = new SMSPeer("2");
        SMSPeer peer3 = new SMSPeer("3");
        SMSPeer peer4 = new SMSPeer("4");
        SMSPeer[] peers = {peer1,peer2,peer3,peer4};
        instance.addToNet(peers);
        assertEquals(instance.networkSize(), 4);
    }

    @Test
    public void addManyPeers(){
        instance.clearNet();
        SMSPeer peer1 = new SMSPeer("1");
        SMSPeer peer2 = new SMSPeer("2");
        SMSPeer peer3 = new SMSPeer("3");
        SMSPeer peer4 = new SMSPeer("4");
        SMSPeer[] peers = {peer1,peer2,peer3,peer4};
        instance.addToNet(peers);
        SMSPeer[] peersOnline = instance.getOnlinePeers();
        Assert.assertArrayEquals(peers, peersOnline);
    }

    @Test
    public void correctNetworkSize_withMultipleEqualPeers(){
        instance.clearNet();
        SMSPeer peer1 = new SMSPeer("1");
        SMSPeer peer2 = new SMSPeer("2");
        SMSPeer peer3 = new SMSPeer("1");
        SMSPeer peer4 = new SMSPeer("2");
        SMSPeer[] peers = {peer1,peer2,peer3,peer4};
        instance.addToNet(peers);
        assertEquals(instance.networkSize(), 2);
    }

    @Test
    public void addPeerToNet_onlyOne(){
        SMSPeer toNet = new SMSPeer("5556");
        instance.addToNet(toNet);
        assertEquals(instance.networkSize(), 1);
        instance.clearNet();
    }

    @Test
    public void addPeerToNet_sameOne(){
        SMSPeer toNet = new SMSPeer("5556");
        instance.addToNet(toNet);
        assertEquals(instance.getOnlinePeers()[0],toNet);
        instance.clearNet();
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
        SMSPeer toNet = new SMSPeer("5556");
        instance.addToNet("5556");
        assertEquals(instance.getOnlinePeers()[0],toNet);
        instance.clearNet();
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



    /*
    Remove from net checks
     */
    @Test
    public void removePeerFromNet(){
        instance.clearNet();
        SMSPeer peer = new SMSPeer("5554");
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        assertTrue(instance.isNetEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removePeerFromNet_invalid(){
        instance.clearNet();
        SMSPeer peer = new SMSPeer("asdf");
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removePeerFromNet_nullPeer(){
        instance.clearNet();
        SMSPeer peer = new SMSPeer(null);
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removePeerFromNet_null(){
        instance.clearNet();
        SMSPeer peer = new SMSPeer("5554");
        instance.addToNet((SMSPeer)null);
        instance.removeFromNet(peer);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removePeerFromNet_empty(){
        instance.clearNet();
        SMSPeer peer = new SMSPeer("");
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        fail();
    }

    /*
    Remove String Peer from net
     */
    @Test
    public void removeStringPeerFromNet(){
        instance.clearNet();
        String peer = "5554";
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        assertTrue(instance.isNetEmpty());
    }

    @Test
    public void removeStringPeerArrayFromNet(){
        instance.clearNet();
        String peer = "5554 5556";
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        assertTrue(instance.isNetEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeStringPeerFromNet_invalid(){
        instance.clearNet();
        String peer = "asdf";
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeStringPeerFromNet_nullPeer(){
        instance.clearNet();
        String peer = null;
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeStringPeerFromNet_empty(){
        instance.clearNet();
        String peer = "";
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        fail();
    }

    /*
    Remove Array Peer from net
     */
    @Test
    public void removeArrayPeerFromNet(){
        instance.clearNet();
        SMSPeer[] peers = {new SMSPeer("5554")};
        instance.addToNet(peers);
        instance.removeFromNet(peers);
        assertTrue(instance.isNetEmpty());
    }

    @Test
    public void removeArrayPeerFromNet_moreThanOne(){
        instance.clearNet();
        SMSPeer[] peers = {new SMSPeer("5554"), new SMSPeer("5556")};
        instance.addToNet(peers);
        instance.removeFromNet(peers);
        assertTrue(instance.isNetEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeArrayPeerFromNet_invalid(){
        instance.clearNet();
        SMSPeer[] peers = {new SMSPeer("asdf"), new SMSPeer("5556")};
        instance.addToNet(peers);
        instance.removeFromNet(peers);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeArrayPeerFromNet_nullPeer(){
        instance.clearNet();
        SMSPeer[] peer = null;
        instance.addToNet(peer);
        instance.removeFromNet(peer);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeArrayPeerFromNet_empty(){
        instance.clearNet();
        SMSPeer[] peers = {new SMSPeer(""), new SMSPeer("5556")};
        instance.addToNet(peers);
        instance.removeFromNet(peers);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeArrayPeerFromNet_null(){
        instance.clearNet();
        SMSPeer[] peers = {null, new SMSPeer("5556")};
        instance.addToNet(peers);
        instance.removeFromNet(peers);
        fail();
    }
}
