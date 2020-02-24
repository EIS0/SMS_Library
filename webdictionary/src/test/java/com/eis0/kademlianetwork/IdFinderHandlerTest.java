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
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KademliaNetwork.class, SMSManager.class, KademliaJoinableNetwork.class})
public class IdFinderHandlerTest {
    //SEARCHER
    private SMSPeer SEARCHER_PEER;
    private KademliaId SEARCHER_ID;
    private SMSKademliaNode SEARCHER;
    //NODE1
    private SMSPeer VALID_PEER1;
    private KademliaId VALID_NODE1_ID;
    private SMSKademliaNode VALID_NODE1;
    //NODE2
    private SMSPeer VALID_PEER2;
    private KademliaId VALID_NODE2_ID;
    private SMSKademliaNode VALID_NODE2;

    private KademliaJoinableNetwork networkMock;
    private SMSManager smsManagerMock;
    private SMSKademliaRoutingTable routingTable;
    private RequestsHandler requestsHandler;


    @Before
    public void setup() {
        //Searcher
        SEARCHER_PEER = new SMSPeer("+393423541601");
        SEARCHER_ID = new KademliaId(SEARCHER_PEER);
        SEARCHER = new SMSKademliaNode(SEARCHER_PEER);
        //Node1
        VALID_PEER1 = new SMSPeer("+393335552121");
        VALID_NODE1_ID = new KademliaId(VALID_PEER1);
        VALID_NODE1 = new SMSKademliaNode(VALID_PEER1);
        //Node2
        VALID_PEER2 = new SMSPeer("+393423541602");
        VALID_NODE2_ID = new KademliaId(VALID_PEER2);
        VALID_NODE2 = new SMSKademliaNode(VALID_PEER2);
        //RoutingTable
        routingTable = new SMSKademliaRoutingTable(SEARCHER, new DefaultConfiguration());

        //MOCK
        networkMock = mock(KademliaJoinableNetwork.class);
        smsManagerMock = mock(SMSManager.class);
        requestsHandler = mock(RequestsHandler.class);
        PowerMockito.mockStatic(SMSManager.class);
        PowerMockito.when(SMSManager.getInstance()).thenReturn(smsManagerMock);

        PowerMockito.mockStatic(KademliaJoinableNetwork.class);
        PowerMockito.when(KademliaJoinableNetwork.getInstance()).thenReturn(networkMock);

        when(networkMock.getLocalNode()).thenReturn(SEARCHER);
        when(networkMock.getLocalRoutingTable()).thenReturn(routingTable);

        when(networkMock.isAlive(SEARCHER_PEER)).thenReturn(true);
        when(networkMock.isAlive(VALID_PEER1)).thenReturn(true);
        when(networkMock.isAlive(VALID_PEER2)).thenReturn(false);


        when(networkMock.getRequestsHandler()).thenReturn(requestsHandler);

        //when(networkMock.isNodeInNetwork(VALID_NODE2)).thenReturn(true);

    }

    @Test(expected = NullPointerException.class)
    public void nullId_throws() {
        IdFinderHandler.searchId(null, SEARCHER_PEER);
    }

    @Test(expected = NullPointerException.class)
    public void nullSearcher_throws() {
        IdFinderHandler.searchId(VALID_NODE1_ID, null);
    }

    /**
     * This test starts a searchId operation, that should end with the calling Request being closed,
     * but the Request doesn't exist; as it is written, the code simply ignore the situation after
     * having started the research
     */
    @Test
    public void nullRequest_throws() {
        IdFinderHandler.searchId(VALID_NODE1_ID, SEARCHER_PEER);
    }

    /**
     * In case the searched Id corresponds to my Id, there is no research made by SMS messages
     */
    @Test
    public void findMyId() {
        IdFinderHandler.searchId(SEARCHER_ID, SEARCHER_PEER);
        String expectedTextMessage = RequestTypes.Ping.ordinal() + " " + SEARCHER_ID + " / / /";
        SMSMessage expectedMessage = new SMSMessage(SEARCHER_PEER, expectedTextMessage);
        verify(smsManagerMock, times(0)).sendMessage(expectedMessage);
    }

    /**
     * There are 2 nodes at the inside of the RoutingTable, one is the searched one, the second one
     * is the local node; the searchId method call will cause the invocation of the
     * SMSManager.sendMessage(), trying to contact the node with an ID closest to searched one
     */
    @Test
    public void findNotMyId_inserted() {
        routingTable.insert(VALID_NODE1);
        IdFinderHandler.searchId(VALID_NODE1_ID, SEARCHER_PEER);
        verify(smsManagerMock, times(1)).sendMessage(any(SMSMessage.class));
    }

    /**
     * There are 2 nodes at the inside of the RoutingTable, one is the searched one, the second one
     * is the local node; the searchId method call will cause the invocation of the
     * SMSManager.sendMessage(), trying to contact the node with an ID closest to searched one
     */
    @Test
    public void findNotMyId_inserted_DEAD() {
        routingTable.insert(VALID_NODE2);
        IdFinderHandler.searchId(VALID_NODE2_ID, SEARCHER_PEER);
        verify(smsManagerMock, times(1)).sendMessage(any(SMSMessage.class));
    }

    /**
     * The local node is the only node at the inside of the RoutingTable, it means that any searched
     * id will cause no SMS message to be sent
     */
    @Test
    public void findNotMyId_notInserted() {
        IdFinderHandler.searchId(VALID_NODE1_ID, SEARCHER_PEER);
        verify(smsManagerMock, times(0)).sendMessage(any(SMSMessage.class));
    }


    @Test
    public void findIdInTable_notSearchedByMe() {
        IdFinderHandler.searchId(SEARCHER_ID, VALID_PEER1);
        String expectedTextMessage = RequestTypes.FindIdSearchResult.ordinal() + " " + SEARCHER + " / / /";
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER1, expectedTextMessage);
        verify(smsManagerMock, times(1)).sendMessage(expectedMessage);
    }

    /**
     * The searcher Peer is DEAD, but I don't care because I did my job
     */
    @Test
    public void findIdInTable_notSearchedByMe_DEAD() {
        IdFinderHandler.searchId(SEARCHER_ID, VALID_PEER2);
        String expectedTextMessage = RequestTypes.FindIdSearchResult.ordinal() + " " + SEARCHER + " / / /";
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER2, expectedTextMessage);
        verify(smsManagerMock, times(1)).sendMessage(expectedMessage);
    }

    @Test
    public void forwardRequest() {
        routingTable.insert(VALID_NODE1);
        IdFinderHandler.searchId(VALID_NODE1_ID, SEARCHER_PEER);
        String expectedTextMessage =
                RequestTypes.FindId.ordinal() + " " +
                        VALID_NODE1_ID + " " + SEARCHER_PEER + " / /";
        SMSMessage expectedMessage = new SMSMessage(VALID_PEER1, expectedTextMessage);
        verify(smsManagerMock, times(1)).sendMessage(expectedMessage);
    }

    /**
     * In the inserted list there is not the LocalNode. It is inserted as last node to check
     * The searched node is the local node; this is an irrealistic situation, if the searched id
     * corresponds to the localNode id, then the local node is always in the list that the searchIdList()
     * will analyze.
     * Anyway, we know for sure that the localNode is always part of the lust of nodes considered as
     * possible closest node (to the idToFind)
     */
    @Test
    public void noLocalNode_localNodeSearched() {
        List<SMSKademliaNode> nodes = routingTable.getAllNodes();
        nodes.add(VALID_NODE1);
        nodes.remove(SEARCHER);
        IdFinderHandler.searchIdList(SEARCHER_ID, SEARCHER_PEER, nodes);
        verify(smsManagerMock, times(1)).sendMessage(any(SMSMessage.class));
    }

    /**
     * In the inserted list there is not the LocalNode. It is inserted as the last node to check
     */
    @Test
    public void noLocalNode_notLocalNodeSearched() {
        List<SMSKademliaNode> nodes = routingTable.getAllNodes();
        nodes.add(VALID_NODE1);
        nodes.remove(SEARCHER);
        IdFinderHandler.searchIdList(VALID_NODE1_ID, SEARCHER_PEER, nodes);
        //A message is sent to the VALID_NODE1
        verify(smsManagerMock, times(1)).sendMessage(any(SMSMessage.class));
    }
}
