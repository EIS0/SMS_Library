package com.eis0.KademliaNetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SMSNetVocabulary;
import com.eis0.webdictionary.SerializableObject;

import org.junit.Test;

import static org.junit.Assert.*;

public class SendContentMessageTest {

    /*Creation of all the necessary params*/
    SMSPeer peer = new SMSPeer("3408140326");
    KademliaId ID = new KademliaId(peer);
    SMSNetVocabulary dic = new SMSNetVocabulary();
    SMSKademliaNode From = new SMSKademliaNode(ID, peer, dic);
    TestContent content = new TestContent();
    TestKey key = new TestKey();
    SendContentMessage test = new SendContentMessage(From, content, key);

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

        public String serialize(){
            return toString();
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

        public String serialize(){
            return toString();
        }
    }


    @Test
    public void getKey() { assertTrue(test.getKey().equals(key));}

    @Test
    public void getContent() {
        assertTrue(test.getContent().equals(content));
    }

    @Test
    public void getPeer() { assertTrue(test.getPeer().equals(peer)); }

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