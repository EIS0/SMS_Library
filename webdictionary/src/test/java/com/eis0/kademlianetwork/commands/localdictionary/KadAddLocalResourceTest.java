package com.eis0.kademlianetwork.commands.localdictionary;

import com.eis0.netinterfaces.NetDictionary;
import com.eis0.webdictionary.SMSNetVocabulary;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KadAddLocalResourceTest {

    private final NetDictionary<String, String> flex = new SMSNetVocabulary();

    @Test
    public void execute() {
        new KadAddLocalResource("test", "value", flex).execute();
        assertEquals(flex.getResource("test"), "value");
    }
}