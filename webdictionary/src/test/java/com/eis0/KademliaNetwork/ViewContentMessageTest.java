package com.eis0.KademliaNetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SMSNetDictionary;
import com.eis0.webdictionary.SerializableObject;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ViewContentMessageTest {
    SMSPeer peer = new SMSPeer("3408140326");
    KademliaId ID = new KademliaId(peer);
    static SMSNetDictionary dic = new SMSNetDictionary();
    SMSKademliaNode From = new SMSKademliaNode(ID, peer, dic);
    SMSKademliaNode To = new SMSKademliaNode(new KademliaId(), peer, dic);
    TestContent content = new TestContent();
    TestKey key = new TestKey();
    ViewContentMessage test = new ViewContentMessage(From, key);

    /*SerializableObject key implementation*/
    public class TestKey extends SerializableObject {

        public int key;

        public TestKey() {
            key = 1;
        }

        @Override
        public String toString() {
            return "uno";
        }

        @Override
        public boolean equals(Object toCompare) {
            if((toCompare == null) || (toCompare.getClass() != this.getClass())) {
                return false;
            }
            SerializableObject other = (SerializableObject)toCompare;
            return other.toString().equals(this.toString());
        }
    }
    /*SerializableObject Value implementation*/
    public class TestContent extends SerializableObject {
        public String value;

        public TestContent() {
            value = "test";
        }

        @Override
        public String toString() {
            return value;
        }

        @Override
        public boolean equals(Object toCompare) {
            if ((toCompare == null) || (toCompare.getClass() != this.getClass())) {
                return false;
            }
            SerializableObject other = (SerializableObject) toCompare;
            return other.toString().equals(this.toString());
        }
    }


    @Before
    public void dicPopolator(){
        To.getDictionary().add(content, key);
    }


    @Test
    public void getContent() {
        To.getDictionary().add(content, key);
        assertTrue(test.getContent(To).equals(content));
    }
}