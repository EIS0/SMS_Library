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
@PrepareForTest({KademliaNetwork.class, SMSManager.class, KademliaJoinableNetwork.class})
public class IdFinderHandlerTest {
    //SEARCHER
    private final SMSPeer SEARCHER_PEER = new SMSPeer("+393423541601");
    private final KademliaId SEARCHER_ID = new KademliaId(SEARCHER_PEER);
    private final SMSKademliaNode SEARCHER = new SMSKademliaNode(SEARCHER_ID);

    //NODE1
    private final KademliaId VALID_NODE1_ID = new KademliaId("0000000000000001");
    private final SMSKademliaNode VALID_NODE1 = new SMSKademliaNode(VALID_NODE1_ID);

    //NODE2
    private final SMSPeer VALID_PEER2 = new SMSPeer("+393423541602");
    private final KademliaId VALID_NODE2_ID = new KademliaId(VALID_PEER2);
    private final SMSKademliaNode VALID_NODE2 = new SMSKademliaNode(VALID_PEER2);

    private KademliaJoinableNetwork networkMock = mock(KademliaJoinableNetwork.class);
    private SMSManager smsManagerMock;

    private final SMSKademliaRoutingTable routingTable = new SMSKademliaRoutingTable(SEARCHER, new DefaultConfiguration());


    @Before
    public void setup(){
        networkMock = mock(KademliaJoinableNetwork.class);
        smsManagerMock = mock(SMSManager.class);
        PowerMockito.mockStatic(SMSManager.class);
        PowerMockito.when(SMSManager.getInstance()).thenReturn(smsManagerMock);

        PowerMockito.mockStatic(KademliaJoinableNetwork.class);
        PowerMockito.when(KademliaJoinableNetwork.getInstance()).thenReturn(networkMock);

        when(networkMock.getLocalNode()).thenReturn(SEARCHER);
        when(networkMock.getLocalRoutingTable()).thenReturn(routingTable);

        when(networkMock.isAlive(SEARCHER_PEER)).thenReturn(true);
        when(networkMock.isAlive(any(SMSPeer.class))).thenReturn(true);

        //when(networkMock.isNodeInNetwork(VALID_NODE2)).thenReturn(true);

    }

    @Test(expected = IllegalArgumentException.class)
    public void nullId_throws(){
        IdFinderHandler.searchId(null, SEARCHER_PEER, ResearchMode.AddToDictionary);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullSearcher_throws(){
        IdFinderHandler.searchId(VALID_NODE1_ID, null, ResearchMode.AddToDictionary);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullRequest_throws(){
        IdFinderHandler.searchId(VALID_NODE1_ID, SEARCHER_PEER, null);
    }

    @Test
    public void findMyId(){
        IdFinderHandler.searchId(SEARCHER_ID, SEARCHER_PEER, ResearchMode.AddToDictionary);
        String expectedTextMessage = RequestTypes.ResultAddRequest.ordinal() + " " + SEARCHER_ID + " / / /";
        SMSMessage expectedMessage = new SMSMessage(SEARCHER_PEER, expectedTextMessage);
        verify(smsManagerMock, times(1)).sendMessage(expectedMessage);
    }

    @Test
    public void findIdInTable(){
        routingTable.insert(VALID_NODE2);
        IdFinderHandler.searchId(VALID_NODE2_ID, SEARCHER_PEER, ResearchMode.AddToDictionary);
        String expectedTextMessage = RequestTypes.ResultAddRequest.ordinal() + " " + VALID_NODE2 + " / / /";
        SMSMessage expectedMessage = new SMSMessage(SEARCHER_PEER, expectedTextMessage);
        verify(smsManagerMock, times(1)).sendMessage(expectedMessage);
    }

    @Test
    public void forwardRequest(){
        //routingTable.insert(VALID_NODE1);
        routingTable.insert(VALID_NODE2);
        IdFinderHandler.searchId(VALID_NODE2_ID, SEARCHER_PEER, ResearchMode.AddToDictionary);
        String expectedTextMessage =
                RequestTypes.FindIdForAddRequest.ordinal() + " " +
                        VALID_NODE2_ID + " " + SEARCHER_PEER + " / /";
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER2, expectedTextMessage);
        verify(smsManagerMock, times(1)).sendMessage(expectedMessage);
    }
}
