package com.eis0.kademlia;

import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SMSNetVocabulary;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SMSKademliaNodeTest {

    /*Test initialization*/
    KademliaId random = new KademliaId();
    SMSNetVocabulary dic = new SMSNetVocabulary();
    SMSKademliaNode Node1 = new SMSKademliaNode(random,new SMSPeer("3497364511"),dic);

    @Test
    public void getNodeIdTest(){
        assertTrue(Node1.getNodeId().equals(random));
    }

    @Test
    public void equalsTest(){
        assertTrue(Node1.equals(Node1));
    }

    @Test
    public void getDictionaryTest(){ assertTrue(Node1.getDictionary().equals(dic)); }


}