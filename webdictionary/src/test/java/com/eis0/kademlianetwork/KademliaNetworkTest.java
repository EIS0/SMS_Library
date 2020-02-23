package com.eis0.kademlianetwork;


import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSPeer;
import com.eis0.UtilityMocks;
import com.eis0.kademlia.Contact;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.commands.messages.KadSendInvitation;
import com.eis0.kademlianetwork.commands.networkdictionary.FindResource;
import com.eis0.kademlianetwork.commands.networkdictionary.KadAddResource;
import com.eis0.kademlianetwork.commands.networkdictionary.KadDeleteResource;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;
import com.eis0.netinterfaces.commands.CommandExecutor;
import com.eis0.netinterfaces.listeners.GetResourceListener;
import com.eis0.netinterfaces.listeners.InviteListener;
import com.eis0.netinterfaces.listeners.RemoveResourceListener;
import com.eis0.netinterfaces.listeners.SetResourceListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CommandExecutor.class, SMSManager.class})

public class KademliaNetworkTest {
    private SMSPeer peer1;
    private SMSPeer peer2;
    private SMSPeer peer3;
    private SMSKademliaNode NODE1; //local node
    private SMSKademliaNode NODE2;
    private SMSKademliaNode NODE3;

    private final String KEY = "key";
    private final String RESOURCE = "resource";

    private KademliaNetwork NET1;
    private RequestsHandler REQUEST_HANDLER;
    
    private SMSManager smsManagerMock;
    private final SetResourceListener dummySetResourceListener = mock(SetResourceListener.class);
    private final GetResourceListener dummyGetResourceListener = mock(GetResourceListener.class);
    private final RemoveResourceListener dummyRemoveResourceListener = mock(RemoveResourceListener.class);
    private final InviteListener dummyInviteListener = mock(InviteListener.class);


    @Before
    public void setUp() throws Exception {
        peer1 = new SMSPeer("+393335552121");
        peer2 = new SMSPeer("+5555");
        peer3 = new SMSPeer("+5556");
        NODE1 = new SMSKademliaNode(peer1); //local node
        NODE2 = new SMSKademliaNode(peer2);
        NODE3 = new SMSKademliaNode(peer3);
        NET1 = new KademliaNetwork();
        REQUEST_HANDLER = new RequestsHandler();

        NET1.init(NODE1, UtilityMocks.setupMocks());
        NET1.getLocalRoutingTable().insert(new SMSKademliaNode(peer2));

        PowerMockito.mockStatic(CommandExecutor.class);

        smsManagerMock = mock(SMSManager.class);
        PowerMockito.mockStatic(SMSManager.class);
        when(SMSManager.getInstance()).thenReturn(smsManagerMock);

        /*These exceptions will be all catch*/
        doThrow(new RuntimeException()).when(CommandExecutor.class, "execute", new KadAddResource(KEY, RESOURCE, REQUEST_HANDLER));
        when(CommandExecutor.class, "execute", new KadDeleteResource(KEY, REQUEST_HANDLER)).thenThrow(new RuntimeException());
        when(CommandExecutor.class, "execute", new FindResource(KEY, REQUEST_HANDLER)).thenThrow(new RuntimeException());

        doNothing().when(CommandExecutor.class, "execute", new KadSendInvitation(new KademliaInvitation(peer3))); //so onInvitationSent() can be called

        /*NullPointerException will be use for failing calls. IllegalArgumentException otherwise*/
        Mockito.doThrow(NullPointerException.class).when(dummySetResourceListener).onResourceSetFail(Mockito.anyString(), Mockito.anyString(), Mockito.any(KademliaFailReason.class));
        Mockito.doThrow(NullPointerException.class).when(dummyGetResourceListener).onGetResourceFailed(Mockito.anyString(), Mockito.any(KademliaFailReason.class));
        Mockito.doThrow(NullPointerException.class).when(dummyRemoveResourceListener).onResourceRemoveFail(Mockito.anyString(), Mockito.any(KademliaFailReason.class));
        Mockito.doThrow(NullPointerException.class).when(dummyInviteListener).onInvitationNotSent(Mockito.any(SMSPeer.class), Mockito.any(KademliaFailReason.class));

        Mockito.doThrow(IllegalArgumentException.class).when(dummyInviteListener).onInvitationSent(Mockito.any(SMSPeer.class));
    }

    @Test
    public void addNodeToTable_TwoNodes() {
        NET1.addNodeToTable(NODE2);
        NET1.addNodeToTable(NODE3);
        assertTrue(NET1.isNodeInNetwork(NODE1));
        assertTrue(NET1.isNodeInNetwork(NODE2));
        assertTrue(NET1.isNodeInNetwork(NODE3));
    }

    @Test
    public void addNodeToTable_OneNode() {
        NET1.addNodeToTable(NODE2);
        assertTrue(NET1.isNodeInNetwork(NODE1));
        assertTrue(NET1.isNodeInNetwork(NODE2));
    }

    @Test
    public void getLocalDictionary() {
        assertNotNull(NET1.getLocalDictionary());
    }

    @Test
    public void noNodeIsAdded_mainIsPresent() {
        assertTrue(NET1.isNodeInNetwork(NODE1));
    }

    @Test
    public void localNode_isPresent() {
        assertEquals(NODE1, NET1.getLocalNode());
    }

    @Test
    public void testSingleton() {
        assertEquals(KademliaJoinableNetwork.getInstance(),
                KademliaJoinableNetwork.getInstance());
    }

    @Test
    public void isNodeInNetwork_itIs() {
        assertTrue(NET1.isNodeInNetwork(new SMSKademliaNode(peer2)));
    }

    @Test
    public void isNodeInNetwork_itIsNot() {
        assertFalse(NET1.isNodeInNetwork(new SMSKademliaNode(peer3)));
    }

    @Test
    public void isAlive_itIsNot() {
        assertFalse(NET1.isAlive(peer1));
    }

    @Test
    public void isAlive_itIs_calledBefore() {
        NET1.connectionInfo.setRespond(true);
        assertTrue(NET1.isAlive(peer1));
    }

    @Test
    public void isAlive_itIs_calledDuring() {
        //an imaginary user responds after 5s
        Timer timer = new Timer();
        timer.schedule(new TimerTest(NET1), 5000);
        //this will last 10s
        assertTrue(NET1.isAlive(peer1));
    }

    @Test
    public void isAlive_checkIfSetUnresponsive_notCalled() {
        //I expected "setUnresponsive" not to be called
        NET1.connectionInfo.setRespond(true);
        NET1.isAlive(peer1);
        //create the node by the peer
        KademliaId id = new KademliaId(peer1);
        SMSKademliaNode responsive = new SMSKademliaNode(peer1);
        //increment its stale count, it will be considered unresponsive
        Contact c = NET1.getLocalRoutingTable().getBuckets()[NET1.getLocalRoutingTable().getBucketId(id)].getFromContacts(responsive);
        //now I check if isAlive worked properly
        assertEquals(c.staleCount(), 0);
    }

    @Test
    public void isAlive_checkIfSetUnresponsive_called() {
        NET1.isAlive(peer1);
        //create the node by the peer
        KademliaId id = new KademliaId(peer1);
        SMSKademliaNode unresponsive = new SMSKademliaNode(peer1);
        //increment its stale count, it will be considered unresponsive
        Contact c = NET1.getLocalRoutingTable().getBuckets()[NET1.getLocalRoutingTable().getBucketId(id)].getFromContacts(unresponsive);
        //now I check if isAlive worked properly
        assertEquals(c.staleCount(), 1);
    }


    @Test
    public void isAlive_calledTwice() {
        assertFalse(NET1.isAlive(peer1));
        NET1.connectionInfo.setRespond(true);
        assertTrue(NET1.isAlive(peer1));
    }

    @Test
    public void isAlive_differentPeers() {
        assertFalse(NET1.isAlive(peer1));
        assertFalse(NET1.isAlive(peer2));
    }

    @Test
    public void isAlive_onlyOnePeer() {
        assertFalse(NET1.isAlive(peer1));
        //I imagine receive a respond from peer2
        NET1.connectionInfo.setRespond(true);
        assertTrue(NET1.isAlive(peer2));
    }

    @Test(expected = NullPointerException.class) //I expect onResourceSetFail() to be called
    public void setResource() {
        NET1.setResource(KEY, RESOURCE, dummySetResourceListener);
    }

    @Test(expected = NullPointerException.class) //I expect onResourceGetFailed() to be called
    public void getResource() {
        NET1.getResource(KEY, dummyGetResourceListener);
    }

    @Test(expected = NullPointerException.class) //I expect onRemoveResourceFailed() to be called
    public void removeResource() {
        NET1.removeResource(KEY, dummyRemoveResourceListener);
    }

    @Test(expected = NullPointerException.class)
    //I expect onInvitationNotSent() to be called (peer2 is already in network)
    public void invite_alreadyInNetwork() {
        NET1.invite(peer2, dummyInviteListener);
    }

    @Test(expected = IllegalArgumentException.class)
    //I expect onInvitationSent() to be called (peer3 isn't in network)
    public void invite_notInNetwork() {
        NET1.invite(peer3, dummyInviteListener);
    }

    private class TimerTest extends TimerTask {
        private final KademliaNetwork net;

        TimerTest(KademliaNetwork net) {
            this.net = net;
        }

        @Override
        public void run() {
            //when an acknowledge is received this gets called
            net.connectionInfo.setRespond(true);
        }
    }
}
