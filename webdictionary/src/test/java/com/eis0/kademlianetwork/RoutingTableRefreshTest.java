package com.eis0.kademlianetwork;

import android.telephony.SmsManager;

import com.eis.smslibrary.SMSPeer;
import com.eis0.UtilityMocks;
import com.eis0.kademlia.SMSKademliaNode;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SmsManager.class)
public class RoutingTableRefreshTest {

    private final SMSPeer PEER1 = new SMSPeer("+393423541604");
    private final SMSPeer PEER2 = new SMSPeer("+393423541605");
    private final SMSPeer PEER3 = new SMSPeer("+393423541606");
    private final SMSPeer PEER1_V2 = new SMSPeer("+393423541604");

    private final SMSKademliaNode NODE1 = new SMSKademliaNode(PEER1); //local node
    private final SMSKademliaNode NODE2 = new SMSKademliaNode(PEER2);
    private final SMSKademliaNode NODE3 = new SMSKademliaNode(PEER3);
    private final SMSKademliaNode NODE1_V2 = new SMSKademliaNode(PEER1_V2);

    private final KademliaNetwork NET1 = new KademliaNetwork();
    private final KademliaNetwork NET2 = new KademliaNetwork();
    private final KademliaNetwork NET3 = new KademliaNetwork();

    @Before
    public void setUp(){

        SmsManager smsManagerMock = mock(SmsManager.class);

        PowerMockito.mockStatic(SmsManager.class);
        PowerMockito.when(SmsManager.getDefault()).thenReturn(smsManagerMock);


        NET1.init(NODE1, UtilityMocks.setupMocks());
        //I don't set Node2 listener in order to set it unresponsive
        NET2.init(NODE3, UtilityMocks.setupMocks());
        NET3.init(NODE1_V2, UtilityMocks.setupMocks());

        //NET1 routing table population
        NET1.addNodeToTable(NODE2);
        NET1.addNodeToTable(NODE3);
        //NET2 routing table population
        NET2.addNodeToTable(NODE1_V2);


        //I expected node4 to be added in my routing table
        NET1.refresh.start();
    }


}
