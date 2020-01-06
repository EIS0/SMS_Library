package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.DefaultConfiguration;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;
import com.eis0.kademlianetwork.informationdeliverymanager.IdFinderHandler;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.kademlianetwork.informationdeliverymanager.ResearchMode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KademliaNetwork.class, SMSManager.class})
public class IdFinderHandlerTest {
    private final SMSPeer SEARCHER = new SMSPeer("+393423541601");
    private final KademliaId SEARCHER_ID = new KademliaId(SEARCHER);
    private final SMSKademliaNode LOCAL_NODE = new SMSKademliaNode(SEARCHER_ID);

    private final KademliaId VALID_ID1 = new KademliaId("0000000000000001");

    private final SMSPeer VALID_PEER1 = new SMSPeer("+393331530477");
    private final SMSKademliaNode NODE_ID1 = new SMSKademliaNode(VALID_PEER1);

    private final SMSPeer VALID_PEER2 = new SMSPeer("+393423541602");
    private final SMSKademliaNode VALID_NODE2 = new SMSKademliaNode(VALID_PEER2);

    private KademliaNetwork networkMock;
    private SMSManager smsManagerMock;

    private final SMSKademliaRoutingTable routingTable = new SMSKademliaRoutingTable(LOCAL_NODE, new DefaultConfiguration());


    @Before
    public void setup(){
        networkMock = mock(KademliaNetwork.class);
        PowerMockito.mockStatic(KademliaNetwork.class);
        PowerMockito.when(KademliaNetwork.getInstance()).thenReturn(networkMock);

        smsManagerMock = mock(SMSManager.class);
        PowerMockito.mockStatic(SMSManager.class);
        PowerMockito.when(SMSManager.getInstance()).thenReturn(smsManagerMock);

        when(networkMock.getLocalNode()).thenReturn(LOCAL_NODE);
        when(networkMock.getLocalRoutingTable()).thenReturn(routingTable);

        when(networkMock.isAlive(SEARCHER)).thenReturn(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullId_throws(){
        IdFinderHandler.searchId(null, SEARCHER, ResearchMode.AddToDictionary);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSearcher_throws(){
        IdFinderHandler.searchId(VALID_ID1, null, ResearchMode.AddToDictionary);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullRequest_throws(){
        IdFinderHandler.searchId(VALID_ID1, SEARCHER, null);
    }

    @Test
    public void findMyId(){
        IdFinderHandler.searchId(SEARCHER_ID, SEARCHER, ResearchMode.AddToDictionary);
        String expectedTextMessage = RequestTypes.ResultAddRequest.ordinal() + " " + SEARCHER_ID + " / / /";
        SMSMessage expectedMessage = new SMSMessage(SEARCHER, expectedTextMessage);
        verify(smsManagerMock, times(1)).sendMessage(expectedMessage);
    }

    @Test
    public void findIdInTable(){
        routingTable.insert(NODE_ID1);
        when(networkMock.isNodeInNetwork(NODE_ID1)).thenReturn(true);
        IdFinderHandler.searchId(NODE_ID1.getId(), SEARCHER, ResearchMode.AddToDictionary);
        String expectedTextMessage = RequestTypes.ResultAddRequest.ordinal() + " " + NODE_ID1.getId() + " / / /";
        SMSMessage expectedMessage = new SMSMessage(SEARCHER, expectedTextMessage);
        verify(smsManagerMock, times(1)).sendMessage(expectedMessage);
    }

    @Test
    public void forwardRequest(){
        //routingTable.insert(VALID_NODE1);
        routingTable.insert(VALID_NODE2);
        when(networkMock.isAlive(any(SMSPeer.class))).thenReturn(true);
        IdFinderHandler.searchId(VALID_NODE2.getId(), SEARCHER, ResearchMode.AddToDictionary);
        String expectedTextMessage =
                RequestTypes.FindIdForAddRequest.ordinal() + " " +
                        VALID_NODE2.getId() + " " +  SEARCHER + " / /";
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER2, expectedTextMessage);
        verify(smsManagerMock, times(1)).sendMessage(expectedMessage);
    }
}
