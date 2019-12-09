package com.eis0.kademlianetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SMSNetVocabulary;
import com.eis0.webdictionary.SerializableObject;

import org.junit.Test;

import static org.junit.Assert.*;

public class ViewContentMessageTest {
    SMSPeer peer = new SMSPeer("3408140326");
    KademliaId ID = new KademliaId(peer);
    static SMSNetVocabulary dic = new SMSNetVocabulary();
    SMSKademliaNode From = new SMSKademliaNode(ID, peer, dic);
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
            if ((toCompare == null) || (toCompare.getClass() != this.getClass())) {
                return false;
            }
            SerializableObject other = (SerializableObject) toCompare;
            return other.toString().equals(this.toString());
        }

        @Override
        public String serialize() {
            return toString();
        }
    }
    @Test
    public void getPeer() { assertTrue(test.getPeer().equals(peer)); }

    @Test
    public void getCode() {
        byte shouldBe = 0x04;
        assertEquals(test.getCode(), shouldBe);
    }

    @Test
    public void getData() {
        String ShouldBe = "ContentMessage[origin=108877ECC3A9B2C286E5CD813119910F6DF43EC4,key=uno]";
        assertEquals(test.getData(), ShouldBe);
    }

}