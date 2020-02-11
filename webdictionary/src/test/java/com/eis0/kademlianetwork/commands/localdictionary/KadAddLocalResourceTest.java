package com.eis0.kademlianetwork.commands.localdictionary;

import com.eis0.netinterfaces.NetDictionary;

import org.junit.Test;

import static org.junit.Assert.*;

public class KadAddLocalResourceTest {

    private NetDictionary<String, String> test;


    @Test
    public void execute() {
        new KadAddLocalResource("test", "", test).execute();
        assertEquals(test.getResource("test"), "");
    }
}