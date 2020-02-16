package com.eis0.kademlianetwork.routingtablemanager;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.DefaultConfiguration;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.kademlia.SMSKademliaRoutingTable;
import com.eis0.kademlianetwork.informationdeliverymanager.RequestsHandler;
import com.eis0.netinterfaces.commands.Command;
import com.eis0.netinterfaces.commands.CommandExecutor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({
        "javax.management.*",
        "javax.net.ssl.*",
        "org.apache.log4j.*"
})
@PrepareForTest(TableUpdateIterator.class)


public class TableUpdateHandlerTest {

    private final SMSPeer PEER = new SMSPeer("12345");
    private final SMSKademliaNode NODE = new SMSKademliaNode(PEER);
    private final KademliaId ID = new KademliaId();

    private SMSKademliaRoutingTable routingTable = new SMSKademliaRoutingTable(NODE, new DefaultConfiguration());
    private RequestsHandler requestsHandler = new RequestsHandler();

    private TableUpdateHandler tableUpdateHandler = new TableUpdateHandler();

    @Before
    public void setUp() throws Exception{

        TableUpdateIterator dummyTableUpdateIterator = PowerMockito.mock(TableUpdateIterator.class);
        PowerMockito.whenNew(TableUpdateIterator.class).withAnyArguments().thenReturn(dummyTableUpdateIterator);
        doThrow(new IllegalArgumentException()).when(dummyTableUpdateIterator).start();
    }

    @Test (expected = IllegalArgumentException.class) //I expected start() to be called
    public void updateTable() {
        tableUpdateHandler.updateTable(routingTable, ID, PEER, requestsHandler);
    }
}