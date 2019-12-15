package com.eis0.kademlia;

import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultConfigurationTest {

    /* Set up */
    DefaultConfiguration test = new DefaultConfiguration();

    @Test
    public void k() {
        assertEquals(test.k(), 2);
    }

    @Test
    public void replacementCacheSize() {
        assertEquals(test.replacementCacheSize(), 3);
    }

    @Test
    public void stale() {
        assertEquals(test.stale(),0);
    }
}