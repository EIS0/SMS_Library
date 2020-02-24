package com.eis0.kademlianetwork.activitystatus;

import com.eis0.kademlianetwork.informationdeliverymanager.Requests.FindResourceRequest;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetResourceTimerTest {

    private FindResourceRequest requestMock;

    @Before
    public void setup(){
        requestMock = mock(FindResourceRequest.class);
        //setting up the mock to return false, then true.
        when(requestMock.isCompleted()).thenReturn(false).thenReturn(true);
    }

    @Test
    public void requestCompleted_waitsForOneSecond(){
        GetResourceTimer timer = new GetResourceTimer(requestMock);
        timer.run();
        verify(requestMock, times(2)).isCompleted();
    }
}