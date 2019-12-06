package com.eis0.webdictionary;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @author Edoardo Raimondi
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SMSNetTest {
    @Test
    public void Instantiation_noErrors() {
        SMSNetVocabulary net = new SMSNetVocabulary();
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
        public String serialize(){ return toString(); }

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
        public String serialize(){ return toString(); }

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
    public void addResource_CheckIfAdded() {
        SMSNetVocabulary net = new SMSNetVocabulary();
        net.add(k1, v2);
        assertEquals(net.getResource(k1), v2);
    }

    @Test
    public void removeKey_CheckNoResources() {
        SMSNetVocabulary net = new SMSNetVocabulary();
        net.add(k1, v1);
        net.remove(k1);
        assertEquals(net.getResource(k1), null);
    }
}