package com.example.webdictionary;

import com.eis0.smslibrary.SMSPeer;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @author Edoardo Raimondi
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SMSNet_Tests {
    @Test
    public void Instantiation_noErrors() {
        SMSNetDictionary net = new SMSNetDictionary();
    }

    //SerializableObject key implementation
    public class SerializableObjectTestKey extends SerializableObject {
        public int key;

        public SerializableObjectTestKey() {
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
    //SerializableObject Value implementation
    public class SerializableObjectTestValue extends SerializableObject {
        public String value;

        public SerializableObjectTestValue() {
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

    //creation of the the keys
    SerializableObjectTestKey k1 = new SerializableObjectTestKey();
    SerializableObjectTestKey k2 = new SerializableObjectTestKey();

    //creation of the values
    SerializableObjectTestValue v1 = new SerializableObjectTestValue();
    SerializableObjectTestValue v2 = new SerializableObjectTestValue();
    @Test
    public void addKey_CheckIfAdded() {
        SMSNetDictionary net = new SMSNetDictionary();
        net.add(k1, v2);
        assertEquals(net.getAvailableKeys()[0], k1);
    }

    @Test
    public void addKey_NoResource_CheckIfAdded() {
        SMSNetDictionary net = new SMSNetDictionary();
        net.add(k1, null);
        assertEquals(net.getAvailableKeys()[0], k1);
    }

    @Test
    public void addResource_CheckIfAdded() {
        SMSNetDictionary net = new SMSNetDictionary();
        net.add(k1, v2);
        assertEquals(net.getResource(k1), v2);
    }

    @Test
    public void removeKey_CheckNoPeer() {
        SMSNetDictionary net = new SMSNetDictionary();
        net.add(k2, v1);
        net.remove(k2);
        assertEquals(net.getAvailableKeys().length, 0);
    }

    @Test
    public void removeKey_CheckNoResources() {
        SMSNetDictionary net = new SMSNetDictionary();
        net.add(k1, v1);
        net.remove(k1);
        assertEquals(net.getResource(k1), null);
    }


    @Test
    public void addMultipleKeys(){
        SMSNetDictionary net = new SMSNetDictionary();
        net.add(k1, v1);
        net.add(k2, v2);
        SerializableObject[] keys = net.getAvailableKeys();
        assertEquals(keys.length, 2);
    }



}