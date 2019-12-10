package com.eis0.kademlianetwork;

import com.eis0.kademlia.KademliaId;
import com.eis0.kademlia.SMSKademliaNode;
import com.eis0.smslibrary.SMSPeer;
import com.eis0.webdictionary.SMSNetVocabulary;
import com.eis0.webdictionary.SerializableObject;

import org.junit.Test;

import static org.junit.Assert.*;

public class ContentMessageTest {
    SMSPeer peer = new SMSPeer("3408140326");
    KademliaId ID = new KademliaId(peer);
    static SMSNetVocabulary dic = new SMSNetVocabulary();
    SMSKademliaNode From = new SMSKademliaNode(ID, peer, dic);
    ContentMessageTest.TestContent content = new ContentMessageTest.TestContent();
    ContentMessage test = new ContentMessage(From, content);

    /*SerializableObject key implementation*/
    public class TestContent extends SerializableObject {

        public int key;

        public TestContent() {
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

    /*Start testing*/
    @Test
    public void getContent(){ assertTrue(test.getContent().equals(content)); }

    @Test
    public void getPeer() { assertTrue(test.getPeer().equals(peer)); }

    @Test
    public void getCode() {
        byte shouldBe = 0x05;
        assertEquals(test.getCode(), shouldBe);
    }

    @Test
    public void getData() {
        String ShouldBe ="ContentMessage x with the following content : uno";
        assertEquals(test.getData(), ShouldBe);
    }

}