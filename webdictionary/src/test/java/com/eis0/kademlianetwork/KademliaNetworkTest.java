package com.eis0.kademlianetwork;


import com.eis.smslibrary.SMSPeer;
import com.eis0.UtilityMocks;
import com.eis0.kademlia.Contact;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.listener.SMSKademliaListener;

import org.junit.Before;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class KademliaNetworkTest {

    private final SMSPeer peer1 = new SMSPeer("+5554");
    private final SMSPeer peer2 = new SMSPeer("+5555");
    private final SMSPeer peer3 = new SMSPeer("+5556");

    private final SMSKademliaNode NODE1 = new SMSKademliaNode(peer1); //local node
    private final SMSKademliaNode NODE2 = new SMSKademliaNode(peer2);
    private final SMSKademliaNode NODE3 = new SMSKademliaNode(peer3);

    private final KademliaNetwork NET1 = new KademliaNetwork();

    private final SMSKademliaListener mockListener1 = mock(SMSKademliaListener.class);

    @Before
    public void setUp(){
        NET1.init(NODE1, UtilityMocks.setupMocks());
        NET1.getLocalRoutingTable().insert(new SMSKademliaNode(peer2));
    }

    @Test
    public void addNodeToTable_TwoNodes(){
        NET1.addNodeToTable(NODE2);
        NET1.addNodeToTable(NODE3);
        assertTrue(NET1.isNodeInNetwork(NODE1));
        assertTrue(NET1.isNodeInNetwork(NODE2));
        assertTrue(NET1.isNodeInNetwork(NODE3));
    }

    @Test
    public void addNodeToTable_OneNode(){
        NET1.addNodeToTable(NODE2);
        assertTrue(NET1.isNodeInNetwork(NODE1));
        assertTrue(NET1.isNodeInNetwork(NODE2));
    }

    @Test
    public void getLocalDictionary(){
        assertNotNull(NET1.getLocalDictionary());
    }

    @Test
    public void noNodeIsAdded_mainIsPresent(){
        assertTrue(NET1.isNodeInNetwork(NODE1));
    }

    @Test
    public void localNode_isPresent(){
        assertEquals(NODE1, NET1.getLocalNode());
    }

    @Test
    public void testSingleton(){
        assertEquals(KademliaJoinableNetwork.getInstance(),
                KademliaJoinableNetwork.getInstance());
    }

    @Test
    public void isAlive_itIsNot(){
        assertFalse(NET1.isAlive(peer1));
    }

    @Test
    public void isAlive_itIs_calledBefore(){
        NET1.connectionInfo.setRespond(true);
        assertTrue(NET1.isAlive(peer1));
    }

    @Test
    public void isAlive_itIs_calledDuring(){
        //an imaginary user responds after 5s
        Timer timer = new Timer();
        timer.schedule(new TimerTest(NET1), 5000);
        //this will last 10s
        assertTrue(NET1.isAlive(peer1));
    }

    @Test
    public void isAlive_checkIfSetUnresponsive_notCalled(){
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
    public void isAlive_checkIfSetUnresponsive_called(){
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
    public void isAlive_calledTwice(){
        assertFalse(NET1.isAlive(peer1));
        NET1.connectionInfo.setRespond(true);
        assertTrue(NET1.isAlive(peer1));
    }

    @Test
    public void isAlive_differentPeers(){
        assertFalse(NET1.isAlive(peer1));
        assertFalse(NET1.isAlive(peer2));
    }

    @Test
    public void isAlive_onlyOnePeer(){
        assertFalse(NET1.isAlive(peer1));
        //I imagine receive a respond from peer2
        NET1.connectionInfo.setRespond(true);
        assertTrue(NET1.isAlive(peer2));
    }



    private class TimerTest extends TimerTask{
        private final KademliaNetwork net;

        TimerTest(KademliaNetwork net){
            this.net = net;
        }

        @Override
        public void run() {
            //when an acknowledge is received this gets called
            net.connectionInfo.setRespond(true);
        }
    }
}
