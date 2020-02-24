package com.eis0.kademlianetwork;

import android.graphics.Paint;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlianetwork.commands.KadAcceptInvite;
import com.eis0.netinterfaces.commands.Command;
import com.eis0.netinterfaces.commands.CommandExecutor;
import com.eis0.netinterfaces.listeners.JoinInvitationListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.matchers.Not;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.doCallRealMethod;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({
        "javax.management.*",
        "javax.net.ssl.*",
        "org.apache.log4j.*"
})
@PrepareForTest({CommandExecutor.class, JoinInvitationListener.class})

public class KademliaJoinableNetworkTest {

    private final SMSPeer PEER = new SMSPeer("+34087140326");
    private final KademliaInvitation invitation = new KademliaInvitation(PEER);

    private final KademliaJoinableNetwork NETWORK = KademliaJoinableNetwork.getInstance();

    private JoinInvitationListener<KademliaInvitation> invitationListener;

    @Before
    public void setUp() throws Exception {

        invitationListener = mock(JoinInvitationListener.class);

        PowerMockito.mockStatic(CommandExecutor.class);
        PowerMockito.mockStatic(JoinInvitationListener.class);

        /*mocked void methods throwing an exception as conventional*/
        doThrow(new RuntimeException()).when(CommandExecutor.class, "execute", Mockito.any(Command.class));
        Mockito.doThrow(IllegalArgumentException.class).when(invitationListener).onJoinInvitationReceived(Mockito.any(KademliaInvitation.class));

    }

    @Test
    public void getInstance() {
        assertEquals(KademliaJoinableNetwork.getInstance(), NETWORK);
    }

    @Test (expected = RuntimeException.class)
    public void acceptJoinInvitation() {
        NETWORK.acceptJoinInvitation(invitation);
    }

    @Test (expected = RuntimeException.class) // I expect acceptJoinInvitation() to be called
    public void checkInvitation_ListenerNotSet() {
        NETWORK.checkInvitation(invitation);
    }

    @Test (expected = IllegalArgumentException.class) //I expect onJoinInvitation() to be called
    public void checkInvitation_listenerSet() {
        NETWORK.setJoinInvitationListener(invitationListener); //2 test in 1. Not so elegant but necessary
        NETWORK.checkInvitation(invitation);
    }

    @Test
    public void setNetDictionary() { /*not testable*/ }
}