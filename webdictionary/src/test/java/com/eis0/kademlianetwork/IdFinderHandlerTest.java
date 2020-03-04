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
    //searcher
    private SMSPeer searcherPeer;
    private KademliaId searcherId;
    private SMSKademliaNode searcher;
    //NODE1
    private SMSPeer validPeer1;
    private KademliaId validNodeId1;
    private SMSKademliaNode validNode1;
    //NODE2
    private SMSPeer validPeer2;
    private KademliaId validNodeId2;
    private SMSKademliaNode validNode2;

    private KademliaJoinableNetwork networkMock;
    private SMSManager smsManagerMock;
    private SMSKademliaRoutingTable routingTable;
    private RequestsHandler requestsHandler;


    @Before
    public void setup() {
        //Searcher
        searcherPeer = new SMSPeer("+393423541601");
        searcherId = new KademliaId(searcherPeer);
        searcher = new SMSKademliaNode(searcherPeer);
        //Node1
        validPeer1 = new SMSPeer("+393335552121");
        validNodeId1 = new KademliaId(validPeer1);
        validNode1 = new SMSKademliaNode(validPeer1);
        //Node2
        validPeer2 = new SMSPeer("+393423541602");
        validNodeId2 = new KademliaId(validPeer2);
        validNode2 = new SMSKademliaNode(validPeer2);
        //RoutingTable
        routingTable = new SMSKademliaRoutingTable(searcher, new DefaultConfiguration());

        //MOCK
        networkMock = mock(KademliaJoinableNetwork.class);
        smsManagerMock = mock(SMSManager.class);
        requestsHandler = mock(RequestsHandler.class);
        PowerMockito.mockStatic(SMSManager.class);
        PowerMockito.when(SMSManager.getInstance()).thenReturn(smsManagerMock);

        PowerMockito.mockStatic(KademliaJoinableNetwork.class);
        PowerMockito.when(KademliaJoinableNetwork.getInstance()).thenReturn(networkMock);

        when(networkMock.getLocalNode()).thenReturn(searcher);
        when(networkMock.getLocalRoutingTable()).thenReturn(routingTable);

        when(networkMock.isAlive(searcherPeer)).thenReturn(true);
        when(networkMock.isAlive(validPeer1)).thenReturn(true);
        when(networkMock.isAlive(validPeer2)).thenReturn(false);

        when(networkMock.getRequestsHandler()).thenReturn(requestsHandler);

    }

    @Test(expected = NullPointerException.class)
    public void nullId_throws() {
        IdFinderHandler.searchId(null, searcherPeer);
    }

    @Test(expected = NullPointerException.class)
    public void nullSearcher_throws() {
        IdFinderHandler.searchId(validNodeId1, null);
    }

    /**
     * This test starts a searchId operation, that should end with the calling Request being closed,
     * but the Request doesn't exist; in the way it is written, the code should simply ignore the
     * situation after having started the research (no Exceptions)
     */
    @Test
    public void closingUnexistingRequest_noExcpetionThrow() {
        IdFinderHandler.searchId(validNodeId1, searcherPeer);
    }

    /**
     * In case the searched Id corresponds to my Id, there is no research made by SMS messages
     */
    @Test
    public void findMyId() {
        IdFinderHandler.searchId(searcherId, searcherPeer);
        String expectedTextMessage = RequestTypes.Ping.ordinal() + " " + searcherId + " / / /";
        SMSMessage expectedMessage = new SMSMessage(searcherPeer, expectedTextMessage);
        verify(smsManagerMock, times(0)).sendMessage(expectedMessage);
    }

    /**
     * There are 2 nodes at the inside of the RoutingTable, one is the searched one, the second one
     * is the local node; the searchId method call will cause the invocation of the
     * SMSManager.sendMessage(), trying to contact the node with an ID closest to searched one
     */
    @Test
    public void findNotMyId_inserted() {
        routingTable.insert(validNode1);
        IdFinderHandler.searchId(validNodeId1, searcherPeer);
        verify(smsManagerMock, times(1)).sendMessage(any(SMSMessage.class));
    }

    /**
     * There are 2 nodes at the inside of the RoutingTable, one is the searched one, the second one
     * is the local node; the searchId method call will cause the invocation of the
     * SMSManager.sendMessage(), trying to contact the node with an ID closest to searched one
     */
    @Test
    public void findNotMyId_inserted_DEAD() {
        routingTable.insert(validNode2);
        IdFinderHandler.searchId(validNodeId2, searcherPeer);
        verify(smsManagerMock, times(1)).sendMessage(any(SMSMessage.class));
    }

    /**
     * The local node is the only node at the inside of the RoutingTable, it means that any searched
     * id will cause no SMS message to be sent
     */
    @Test
    public void findNotMyId_notInserted() {
        IdFinderHandler.searchId(validNodeId1, searcherPeer);
        verify(smsManagerMock, times(0)).sendMessage(any(SMSMessage.class));
    }


    @Test
    public void findIdInTable_notSearchedByMe() {
        IdFinderHandler.searchId(searcherId, validPeer1);
        String expectedTextMessage = RequestTypes.FindIdSearchResult.ordinal() + " " + searcher + " / / /";
        SMSMessage expectedMessage = new SMSMessage(validPeer1, expectedTextMessage);
        verify(smsManagerMock, times(1)).sendMessage(expectedMessage);
    }

    /**
     * The searcher Peer is DEAD, but I don't care because I did my job
     */
    @Test
    public void findIdInTable_notSearchedByMe_DEAD() {
        IdFinderHandler.searchId(searcherId, validPeer2);
        String expectedTextMessage = RequestTypes.FindIdSearchResult.ordinal() + " " + searcher + " / / /";
        SMSMessage expectedMessage = new SMSMessage(validPeer2, expectedTextMessage);
        verify(smsManagerMock, times(1)).sendMessage(expectedMessage);
    }

    @Test
    public void forwardRequest() {
        routingTable.insert(validNode1);
        IdFinderHandler.searchId(validNodeId1, searcherPeer);
        String expectedTextMessage =
                RequestTypes.FindId.ordinal() + " " +
                        validNodeId1 + " " + searcherPeer + " / /";
        SMSMessage expectedMessage = new SMSMessage(validPeer1, expectedTextMessage);
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
        nodes.add(validNode1);
        nodes.remove(searcher);
        IdFinderHandler.searchIdList(searcherId, searcherPeer, nodes);
        verify(smsManagerMock, times(1)).sendMessage(any(SMSMessage.class));
    }

    /**
     * In the inserted list there is not the LocalNode. It is inserted as the last node to check
     */
    @Test
    public void noLocalNode_notLocalNodeSearched() {
        List<SMSKademliaNode> nodes = routingTable.getAllNodes();
        nodes.add(validNode1);
        nodes.remove(searcher);
        IdFinderHandler.searchIdList(validNodeId1, searcherPeer, nodes);
        //A message is sent to the validNode1
        verify(smsManagerMock, times(1)).sendMessage(any(SMSMessage.class));
    }
}
