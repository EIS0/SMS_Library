package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.UtilityMocks;
import com.eis0.kademlia.SMSKademliaNode;
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
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KademliaNetwork.class, SMSManager.class})
public class SMSKademliaListenerTest {

    private final SMSPeer peer1 = new SMSPeer("+3408140326");

    private final SMSKademliaNode node1 = new SMSKademliaNode(peer1);

    private SMSManager smsManagerMock;

    @Spy
    private KademliaJoinableNetwork spyNetwork = KademliaJoinableNetwork.getInstance();

    @Spy
    private SMSKademliaListener spyListener = new SMSKademliaListener();

    /*Default messages*/
    private final SMSMessage acknowledge = new KademliaMessage()
            .setPeer(peer1)
            .setRequestType(RequestTypes.AcknowledgeMessage)
            .buildMessage();
    private final SMSMessage ping = new KademliaMessage()
            .setPeer(peer1)
            .setRequestType(RequestTypes.Ping)
            .buildMessage();
    private final SMSMessage pong = new KademliaMessage()
            .setPeer(peer1)
            .setRequestType(RequestTypes.Pong)
            .buildMessage();

    @Before
    public void setUp(){

        spyNetwork.init(node1, UtilityMocks.setupMocks());

        smsManagerMock = mock(SMSManager.class);
        PowerMockito.mockStatic(SMSManager.class);
        PowerMockito.when(SMSManager.getInstance()).thenReturn(smsManagerMock);

        doCallRealMethod().when(spyListener).onMessageReceived(acknowledge);
    }



    @Test
    public void AcknowledgeAction(){
        //I suppose someone sent me an acknowledge message
        spyListener.onMessageReceived(acknowledge);
        assertTrue(spyNetwork.connectionInfo.hasRespond());
    }

    @Test
    public void PingAction(){
        //I suppose someone sent me a ping message
        spyListener.onMessageReceived(ping);
        verify(smsManagerMock).sendMessage(pong);
    }

    @Test
    public void PongAction(){
        //I suppose someone sent me a pong message
        spyListener.onMessageReceived(pong);
        assertTrue(spyNetwork.connectionInfo.hasPong());
    }

}