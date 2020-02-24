package com.eis0.kademlianetwork;

import android.util.Log;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.UtilityMocks;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.informationdeliverymanager.IdFinderHandler;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.kademlianetwork.listener.SMSKademliaListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KademliaNetwork.class, SMSManager.class,
        KademliaJoinableNetwork.class, Log.class, IdFinderHandler.class})
public class SMSKademliaListenerTest {

    private final SMSPeer peer1 = new SMSPeer("+393423541601");
    private final SMSPeer peer2 = new SMSPeer("+393423541602");
    private final KademliaId idToFind1 = new KademliaId(peer2);
    private final SMSKademliaNode node1 = new SMSKademliaNode(peer1);

    private SMSManager smsManagerMock;

    @Spy
    private KademliaJoinableNetwork spyNetwork = KademliaJoinableNetwork.getInstance();

    @Spy
    private SMSKademliaListener spyListener = new SMSKademliaListener();

    /*Default messages*/
    private final SMSMessage acknowledgeMessage = new KademliaMessage()
            .setPeer(peer1)
            .setRequestType(RequestTypes.AcknowledgeMessage)
            .buildMessage();
    private final SMSMessage pingMessage = new KademliaMessage()
            .setPeer(peer1)
            .setRequestType(RequestTypes.Ping)
            .buildMessage();
    private final SMSMessage pongMessage = new KademliaMessage()
            .setPeer(peer1)
            .setRequestType(RequestTypes.Pong)
            .buildMessage();
    private final SMSMessage joinMessage = new KademliaMessage()
            .setPeer(peer1)
            .setRequestType(RequestTypes.JoinPermission)
            .buildMessage();
    private final SMSMessage acceptJoinMessage = new KademliaMessage()
            .setPeer(peer1)
            .setRequestType(RequestTypes.AcceptJoin)
            .buildMessage();
    private final SMSMessage findIdMessage = new KademliaMessage()
            .setPeer(peer1)
            .setIdToFind(idToFind1)
            .setSearcher(peer1)
            .setRequestType(RequestTypes.FindId)
            .buildMessage();

    @Before
    public void setUp() {
        smsManagerMock = mock(SMSManager.class);
        PowerMockito.mockStatic(SMSManager.class);
        PowerMockito.when(SMSManager.getInstance()).thenReturn(smsManagerMock);
        PowerMockito.mockStatic(KademliaJoinableNetwork.class);
        PowerMockito.when(KademliaJoinableNetwork.getInstance()).thenReturn(spyNetwork);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(IdFinderHandler.class);

        doCallRealMethod().when(spyListener).onMessageReceived(acknowledgeMessage);
        spyNetwork.init(node1, UtilityMocks.setupMocks());
    }

    @Test
    public void AcknowledgeAction_asExpected() {
        //I suppose someone sent me an acknowledgeMessage message
        spyListener.onMessageReceived(acknowledgeMessage);
        assertTrue(spyNetwork.connectionInfo.hasRespond());
    }

    @Test
    public void PingAction_asExpected() {
        //I suppose someone sent me a pingMessage message
        spyListener.onMessageReceived(pingMessage);
        verify(smsManagerMock).sendMessage(pongMessage);
    }

    @Test
    public void PongAction_asExpected() {
        //I suppose someone sent me a pongMessage message
        spyListener.onMessageReceived(pongMessage);
        assertTrue(spyNetwork.connectionInfo.hasPong());
    }

    @Test
    public void JoinPermission_asExpected() {
        spyListener.onMessageReceived(joinMessage);
        verify(spyNetwork, times(1)).checkInvitation(new KademliaInvitation(peer1));
    }

    @Test
    public void AcceptJoin_asExpected(){
        spyListener.onMessageReceived(acceptJoinMessage);
        verify(spyNetwork, times(1))
                .addNodeToTable(new SMSKademliaNode(peer1));
        verify(spyNetwork, times(1))
                .updateTable();
    }

    @Test
    public void FindId_asExpected(){
        spyListener.onMessageReceived(findIdMessage);
        verify(smsManagerMock).sendMessage(acknowledgeMessage);
        PowerMockito.verifyStatic(times(1));
        IdFinderHandler.searchId(idToFind1, peer1);
    }

}