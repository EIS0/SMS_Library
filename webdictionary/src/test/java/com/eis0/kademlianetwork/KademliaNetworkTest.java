package com.eis0.kademlianetwork;


import com.eis.smslibrary.SMSPeer;
import com.eis0.UtilityMocks;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.listener.SMSKademliaListener;

import org.junit.Before;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    public void noNodeIsAdded_mainIsPresent(){
        assertTrue(NET1.isNodeInNetwork(NODE1));
    }

    @Test
    public void localNode_isPresent(){
        assertEquals(NODE1, NET1.getLocalNode());
    }

    @Test
    public void testSingleton(){
        assertEquals(KademliaNetwork.getInstance(),
                     KademliaNetwork.getInstance());
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
