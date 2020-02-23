package com.eis0.kademlianetwork.activitystatus;

import com.eis.smslibrary.SMSPeer;
import com.eis0.kademlia.KademliaId;
import com.eis0.kademlianetwork.informationdeliverymanager.Requests.FindIdRequest;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FindIdTimerTest {

    private final SMSPeer VALID_PEER = new SMSPeer("+393335552121");
    private final KademliaId VALID_ID = new KademliaId(VALID_PEER);
    private FindIdRequest requestMock;

    @Before
    public void setup(){
        requestMock = mock(FindIdRequest.class);
        //setting up the mock to return false, then true.
        when(requestMock.isCompleted()).thenReturn(false).thenReturn(true);
    }

    @Test
    public void requestCompleted_waitsForOneSecond(){
        FindIdTimer timer = new FindIdTimer(requestMock);
        timer.run();
        verify(requestMock, times(2)).isCompleted();
    }
}