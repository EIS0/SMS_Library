package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.UtilityMocks;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.ActivityStatus.RespondTimer;
import com.eis0.kademlianetwork.InformationDeliveryManager.KademliaMessage;
import com.eis0.kademlianetwork.InformationDeliveryManager.RequestTypes;
import com.eis0.kademlianetwork.Listener.SMSKademliaListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KademliaNetwork.class, SMSManager.class})
public class SMSKademliaListenerTest {

    private SMSPeer peer1 = new SMSPeer("+556");
    private SMSPeer peer2 = new SMSPeer("+554");

    private SMSKademliaNode node1 = new SMSKademliaNode(peer1);
    private SMSKademliaNode node2 = new SMSKademliaNode(peer2);

    private SMSManager smsManagerMock;

    private RespondTimer timer = new RespondTimer();
    @Spy
    private KademliaNetwork spyNetwork = new KademliaNetwork();

    @Spy
    private SMSKademliaListener spyListener = new SMSKademliaListener(spyNetwork);

    /*Default messages*/
    KademliaMessage acknowledge = new KademliaMessage(RequestTypes.AcknowledgeMessage, null, null, null, null);
    SMSMessage acknowledgeMessage = new SMSMessage(peer1, acknowledge.toString());
    KademliaMessage ping = new KademliaMessage(RequestTypes.Ping, null, null, null, null);
    SMSMessage pingMessage = new SMSMessage(peer1, ping.toString());
    KademliaMessage pong = new KademliaMessage(RequestTypes.Pong, null, null, null, null);
    SMSMessage pongMessage = new SMSMessage(peer1, pong.toString());

    @Before
    public void setUp(){

        spyNetwork.init(node1, spyListener, UtilityMocks.setupMocks());

        smsManagerMock = mock(SMSManager.class);
        PowerMockito.mockStatic(SMSManager.class);
        PowerMockito.when(SMSManager.getInstance()).thenReturn(smsManagerMock);

        doCallRealMethod().when(spyListener).onMessageReceived(acknowledgeMessage);
    }


    @Test
    public void AcknowledgeAction(){
        //I suppose someone sent me an acknowledge message
        spyListener.onMessageReceived(acknowledgeMessage);
        assertTrue(spyNetwork.connectionInfo.hasRespond());
    }

    @Test
    public void PingAction(){
        //I suppose someone sent me a ping message
        spyListener.onMessageReceived(pingMessage);
        verify(smsManagerMock).sendMessage(pongMessage);
    }

    @Test
    public void PongAction(){
        //I suppose somone sent me a pong message
        spyListener.onMessageReceived(pongMessage);
        assertTrue(spyNetwork.connectionInfo.hasPong());
    }

}