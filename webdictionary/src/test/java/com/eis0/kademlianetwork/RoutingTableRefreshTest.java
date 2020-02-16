package com.eis0.kademlianetwork;

import android.telephony.SmsManager;

import com.eis.smslibrary.SMSPeer;
import com.eis0.UtilityMocks;
import com.eis0.kademlia.Contact;
import com.eis0.kademlia.DefaultConfiguration;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaBucket;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.routingtablemanager.RoutingTableRefresh;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

    private final int MAX_STALE_COUNT = 2;

    RoutingTableRefresh tableRefresh = new RoutingTableRefresh(NODE1, NET1);

    @Before
    public void setUp() {

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
    }

    @Test
    public void removeIfUnresponsive_responsive() {
        assertFalse(tableRefresh.removeIfUnresponsive(NODE1)); // I expect NODE1 to be not removed
    }

    @Test
    public void removeIfUnresponsive_unresponsive() {
        KademliaId id = NODE1.getId();
        //I check the bucket Id that contains that node.
        int b = NET1.getLocalRoutingTable().getBucketId(id);
        //I extract the bucket
        SMSKademliaBucket bucket = NET1.getLocalRoutingTable().getBuckets()[b];
        Contact c = bucket.getFromContacts(NODE1);
        for(int i=0; i<MAX_STALE_COUNT; i++) c.incrementStaleCount();
        assertTrue(tableRefresh.removeIfUnresponsive(NODE1)); // I expect NODE1 to be removed
    }

    @Test
    public void removeIfUnresponsive_responsive_checkIfInBucket(){
        tableRefresh.removeIfUnresponsive(NODE1); //I expected NODE1 to be not removed
        KademliaId id = NODE1.getId();
        //I check the bucket Id that contains that node.
        int b = NET1.getLocalRoutingTable().getBucketId(id);
        //I extract the bucket
        SMSKademliaBucket bucket = NET1.getLocalRoutingTable().getBuckets()[b];
        assertTrue(bucket.containsContact(new Contact(NODE1)));
    }

    @Test
    public void removeIfUnresponsive_unresponsive_checkIfNotInBucket(){
        KademliaId id = NODE1.getId();
        //I check the bucket Id that contains that node.
        int b = NET1.getLocalRoutingTable().getBucketId(id);
        //I extract the bucket
        SMSKademliaBucket bucket = NET1.getLocalRoutingTable().getBuckets()[b];
        Contact c = bucket.getFromContacts(NODE1);
        for(int i=0; i<MAX_STALE_COUNT; i++) c.incrementStaleCount();
        tableRefresh.removeIfUnresponsive(NODE1);
        assertFalse(bucket.containsContact(c));
    }
}
