package com.eis0.kademlianetwork.ActivityStatus;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.InformationDeliveryManager.KademliaMessage;
import com.eis0.kademlianetwork.InformationDeliveryManager.RequestTypes;
import com.eis0.kademlianetwork.KademliaNetwork;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(PowerMockRunner.class)
@PrepareForTest({KademliaNetwork.class, SMSManager.class})
public class SystemMessagesTest {

    private SMSPeer peer1 = new SMSPeer("+556");
    private SMSPeer peer2 = new SMSPeer("+554");

    private SMSKademliaNode node1 = new SMSKademliaNode(peer1);
    private SMSKademliaNode node2 = new SMSKademliaNode(peer2);

    private SMSManager smsManagerMock;

    private final SystemMessages systemMessages = new SystemMessages();


    @Before
    public void setUp(){

        smsManagerMock = mock(SMSManager.class);
        PowerMockito.mockStatic(SMSManager.class);
        PowerMockito.when(SMSManager.getInstance()).thenReturn(smsManagerMock);

    }

    @Test
    public void sendAcknowledge(){
        systemMessages.sendAcknowledge(peer1);
        KademliaMessage acknowledge = new KademliaMessage(RequestTypes.AcknowledgeMessage, null, null, null, null);
        SMSMessage acknowledgeMessage = new SMSMessage(peer1, acknowledge.toString());
        verify(smsManagerMock, times(1)).sendMessage(acknowledgeMessage);
    }

    @Test
    public void sendPing(){
        systemMessages.sendPing(node1);
        KademliaMessage ping = new KademliaMessage(RequestTypes.Ping, null, null, null, null);
        SMSMessage pingMessage = new SMSMessage(peer1, ping.toString());
        verify(smsManagerMock, times(1)).sendMessage(pingMessage);
    }

    @Test
    public void sendPong(){
        systemMessages.sendPong(peer1);
        KademliaMessage pong = new KademliaMessage(RequestTypes.Pong, null, null, null, null);
        SMSMessage pongMessage = new SMSMessage(peer1, pong.toString());
        verify(smsManagerMock, times(1)).sendMessage(pongMessage);
    }
}