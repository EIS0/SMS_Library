package com.eis0.kademlianetwork;


import android.content.Context;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

public class KademliaNetworkTest {

    Context mockContext = spy(Context.class);

    @Test
    public void testSingleton(){
        assertEquals(KademliaNetwork.getInstance(mockContext),
                     KademliaNetwork.getInstance(mockContext));
    }
}
