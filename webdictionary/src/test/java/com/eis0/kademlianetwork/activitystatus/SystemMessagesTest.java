package com.eis0.kademlianetwork.activitystatus;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaOldMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
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

    private final SMSPeer peer1 = new SMSPeer("+556");

    private final SMSKademliaNode node1 = new SMSKademliaNode(peer1);

    private SMSManager smsManagerMock;

    @Before
    public void setUp(){

        smsManagerMock = mock(SMSManager.class);
        PowerMockito.mockStatic(SMSManager.class);
        PowerMockito.when(SMSManager.getInstance()).thenReturn(smsManagerMock);

    }

    @Test
    public void sendAcknowledge(){
        SystemMessages.sendAcknowledge(peer1);
        KademliaOldMessage acknowledge = new KademliaOldMessage(RequestTypes.AcknowledgeMessage, null, null, null, null);
        SMSMessage acknowledgeMessage = new SMSMessage(peer1, acknowledge.toString());
        verify(smsManagerMock, times(1)).sendMessage(acknowledgeMessage);
    }

    @Test
    public void sendPing(){
        SystemMessages.sendPing(node1);
        KademliaOldMessage ping = new KademliaOldMessage(RequestTypes.Ping, null, null, null, null);
        SMSMessage pingMessage = new SMSMessage(peer1, ping.toString());
        verify(smsManagerMock, times(1)).sendMessage(pingMessage);
    }

    @Test
    public void sendPong(){
        SystemMessages.sendPong(peer1);
        KademliaOldMessage pong = new KademliaOldMessage(RequestTypes.Pong, null, null, null, null);
        SMSMessage pongMessage = new SMSMessage(peer1, pong.toString());
        verify(smsManagerMock, times(1)).sendMessage(pongMessage);
    }
}