package com.eis0.kademlianetwork.commands.localdictionary;

import com.eis0.netinterfaces.NetDictionary;
import com.eis0.webdictionary.SMSNetVocabulary;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KadRemoveLocalResourceTest {

    private final NetDictionary<String, String> test = new SMSNetVocabulary();

    @Before
    public void setUp(){
        test.addResource("key", "");
    }
    @Test
    public void execute() {
        
    }
}