package com.eis0.kademlianetwork;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KademliaNetworkTest {
    @Test
    public void testSingleton(){
        assertEquals(KademliaNetwork.getInstance(), KademliaNetwork.getInstance());
    }
}
