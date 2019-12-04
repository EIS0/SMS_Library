package com.eis0.KademliaNetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SMSNetDictionary;
import com.eis0.webdictionary.SerializableObject;

import org.junit.Test;
import org.w3c.dom.Node;

import static org.junit.Assert.*;

public class SendContentMessageTest {

    SMSPeer peer = new SMSPeer("3408140326");
    KademliaId ID = new KademliaId(peer);
    SMSKademliaNode From = new SMSKademliaNode(ID, 123, peer);
    SMSKademliaNode To = new SMSKademliaNode(new KademliaId(), 123, peer);
    TestContent content = new TestContent();
    TestKey key = new TestKey();
    SendContentMessage test = new SendContentMessage(From, content, key);
    SMSNetDictionary dic = new SMSNetDictionary();

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


    @Test
    public void setContent() {
        test.setContent(dic, To);
        assertTrue(dic.getResource(key).equals(content));
    }

    @Test
    public void getContent() {
        assertTrue(test.getContent().equals(content));
    }

    @Test
    public void getPeer() {
        assertTrue(test.getPeer().equals(peer));
    }

    @Test
    public void getCode() {
        byte shouldBe = 0x01;
        assertEquals(test.getCode(), shouldBe);
    }

    @Test
    public void getData() {
        String ShouldBe = "ContentMessage[origin=108877ECC3A9B2C286E5CD813119910F6DF43EC4,content=test]";
        assertEquals(test.getData(), ShouldBe);
    }
}