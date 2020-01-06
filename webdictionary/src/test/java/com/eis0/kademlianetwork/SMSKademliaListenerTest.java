package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.UtilityMocks;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.listener.SMSKademliaListener;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaOldMessage;

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

    private final SMSPeer peer1 = new SMSPeer("+556");

    private final SMSKademliaNode node1 = new SMSKademliaNode(peer1);

    private SMSManager smsManagerMock;

    @Spy
    private KademliaNetwork spyNetwork = new KademliaNetwork();

    @Spy
    private SMSKademliaListener spyListener = new SMSKademliaListener(spyNetwork);

    /*Default messages*/
    private final KademliaOldMessage acknowledge = new KademliaOldMessage(RequestTypes.AcknowledgeMessage, null, null, null, null);
    private final SMSMessage acknowledgeMessage = new SMSMessage(peer1, acknowledge.toString());
    private final KademliaOldMessage ping = new KademliaOldMessage(RequestTypes.Ping, null, null, null, null);
    private final SMSMessage pingMessage = new SMSMessage(peer1, ping.toString());
    private final KademliaOldMessage pong = new KademliaOldMessage(RequestTypes.Pong, null, null, null, null);
    private final SMSMessage pongMessage = new SMSMessage(peer1, pong.toString());

    @Before
    public void setUp(){

        spyNetwork.init(node1, UtilityMocks.setupMocks());

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
        //I suppose someone sent me a pong message
        spyListener.onMessageReceived(pongMessage);
        assertTrue(spyNetwork.connectionInfo.hasPong());
    }

}