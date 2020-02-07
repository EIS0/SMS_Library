package com.eis0.kademlianetwork.commands;

import com.eis.smslibrary.SMSManager;
import com.eis.smslibrary.SMSMessage;
import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlianetwork.KademliaInvitation;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestTypes;
import com.eis0.netinterfaces.commands.CommandExecutor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests to check if the SendInvitation command works correctly
 *
 * @author Marco Cognolato
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SMSManager.class)
public class KadSendInvitationTest {

    private String destination = "+393423541601";
    private SMSPeer validPeer = new SMSPeer(destination);
    private KademliaInvitation validInvitation = new KademliaInvitation(validPeer);
    private KademliaInvitation invalidInvitation = new KademliaInvitation(null);

    private SMSManager managerMock;

    @Before
    public void setUp() {
        managerMock = mock(SMSManager.class);

        PowerMockito.mockStatic(SMSManager.class);
        PowerMockito.when(SMSManager.getInstance()).thenReturn(managerMock);
    }

    @Test
    public void execute_asExpected() {
        CommandExecutor.execute(new KadSendInvitation(validInvitation));
        String expectedString = RequestTypes.JoinPermission.ordinal() + " / / / /";
        SMSMessage expectedMessage = new SMSMessage(validPeer, expectedString);
        verify(managerMock, times(1)).sendMessage(expectedMessage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void execute_wrongInvitation() {
        CommandExecutor.execute(new KadSendInvitation(invalidInvitation));
    }
}
