package com.eis0.kademlianetwork.commands.localdictionary;

import com.eis0.kademlianetwork.KademliaNetwork;
import com.eis0.netinterfaces.NetDictionary;
import com.eis0.webdictionary.SMSNetVocabulary;

import org.junit.Test;

import static org.junit.Assert.*;

public class KadAddLocalResourceTest {

    private final NetDictionary<String, String> flex = new SMSNetVocabulary();

    @Test
    public void execute() {
        new KadAddLocalResource("test", "", flex).execute();
        assertEquals(flex.getResource("test"), "");
    }
}