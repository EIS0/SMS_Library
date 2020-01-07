package com.eis0.kademlianetwork.activitystatus;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.KademliaNetwork;
import com.eis0.kademlianetwork.informationdeliverymanager.KademliaMessage;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;

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
        SMSMessage acknowledge = new KademliaMessage()
                .setPeer(peer1)
                .setRequestType(RequestTypes.AcknowledgeMessage)
                .buildMessage();
        verify(smsManagerMock, times(1)).sendMessage(acknowledge);
    }

    @Test
    public void sendPing(){
        SystemMessages.sendPing(node1);
        SMSMessage ping = new KademliaMessage()
                .setPeer(peer1)
                .setRequestType(RequestTypes.Ping)
                .buildMessage();
        verify(smsManagerMock, times(1)).sendMessage(ping);
    }

    @Test
    public void sendPong(){
        SystemMessages.sendPong(peer1);
        SMSMessage pong = new KademliaMessage()
                .setPeer(peer1)
                .setRequestType(RequestTypes.Pong)
                .buildMessage();
        verify(smsManagerMock, times(1)).sendMessage(pong);
    }
}