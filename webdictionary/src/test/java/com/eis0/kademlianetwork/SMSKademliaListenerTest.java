package com.eis0.kademlianetwork;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlianetwork.ActivityStatus.SystemMessages;
import com.eis0.kademlianetwork.InformationDeliveryManager.KademliaMessage;
import com.eis0.kademlianetwork.InformationDeliveryManager.RequestTypes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({KademliaNetwork.class, SMSManager.class})
public class SMSKademliaListenerTest {

    private SMSPeer peer1 = new SMSPeer("+556");
    private SMSPeer peer2 = new SMSPeer("+554");

    private SMSKademliaNode node1 = new SMSKademliaNode(peer1);
    private SMSKademliaNode node2 = new SMSKademliaNode(peer2);

    private SMSManager smsManagerMock;

    @Spy
    KademliaNetwork spyNetwork = new KademliaNetwork();

    @Spy
    SMSKademliaListener spyListener = new SMSKademliaListener(spyNetwork);

    KademliaMessage acknowledge = new KademliaMessage(RequestTypes.AcknowledgeMessage, null, null, null, null);
    SMSMessage acknowledgeMessage = new SMSMessage(peer1, acknowledge.toString());

    @Before
    public void setUp(){

        smsManagerMock = mock(SMSManager.class);
        PowerMockito.mockStatic(SMSManager.class);
        PowerMockito.when(SMSManager.getInstance()).thenReturn(smsManagerMock);

        doReturn(true).when(spyNetwork.connectionInfo).hasRespond();

    }

    @Test
    public void acknowledgeCase(){

    }

}