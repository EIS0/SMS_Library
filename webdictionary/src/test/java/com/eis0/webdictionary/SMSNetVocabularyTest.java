package com.eis0.webdictionary;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SMSNetVocabularyTest {

    private SMSNetVocabulary net;
    private String key1;
    private String key2;
    private String value1;
    private String value2;

    @Before
    public void setup(){
        net = new SMSNetVocabulary();
        key1 = "key1";
        key2 = "key2";
        value1 = "value1";
        value2 = "value2";
    }

    @Test
    public void differentObjects_areDifferent(){
        Assert.assertNotEquals(key1, key2);
    }

    @Test
    public void sameObjects_isEqual(){
        Assert.assertEquals(value1, value1);
    }

    /*

    @Test
    public void addSingleResource_canFindIt(){
        net.addResource(key1, value1);
        Assert.assertEquals(net.getResource(key1), value1);
    }

    @Test
    public void addSingleResource_cantFindIfNotAdded(){
        net.addResource(key1, value1);
        Assert.assertNotEquals(net.getResource(key1), value2);
    }

    @Test
    public void cantFindIfNotAdded(){
        Assert.assertNull(net.getResource(key1));
    }

    @Test
    public void addMultipleResources_canFindOne(){
        net.addResource(key1, value1);
        net.addResource(key2, value2);
        Assert.assertEquals(net.getResource(key1), value1);
    }

    @Test
    public void addMultipleResources_canFindOther(){
        net.addResource(key1, value1);
        net.addResource(key2, value2);
        Assert.assertEquals(net.getResource(key2), value2);
    }

    @Test
    public void updateResource_correctlyUpdates(){
        net.addResource(key1, value1);
        net.update(key1, value2);
        Assert.assertEquals(net.getResource(key1), value2);
    }

    @Test
    public void addingSameKey_updatesResource(){
        net.addResource(key1, value1);
        net.addResource(key1, value2);
        Assert.assertEquals(net.getResource(key1), value2);
    }

    @Test(expected = NullPointerException.class)
    public void addNullKey_throws(){
        net.addResource(null, value1);
    }

    @Test(expected = NullPointerException.class)
    public void addNullResource_throws(){
        net.addResource(key1, null);
    }

    @Test(expected = NullPointerException.class)
    public void addNullBoth_throws(){
        net.addResource(null, null);
    }

    @Test
    public void removeKey_cantFindIt(){
        net.addResource(key1, value1);
        net.removeResource(key1);
        Assert.assertNull(net.getResource(key1));
    }

    @Test(expected = NullPointerException.class)
    public void removeNull_throws(){
        net.removeResource(null);
    }
     */


    private class TestObject extends SerializableObject{
        private final String name;

        TestObject(String name){
            this.name = name;
        }

        @Override
        public String serialize(){ return name; }

        String getData(){
            return name;
        }

        @Override
        public boolean equals(Object toCompare){
            if(toCompare == null || toCompare.getClass() != this.getClass())
                return false;
            TestObject thisObj = (TestObject)toCompare;
            return thisObj.getData().equals(this.name);
        }
        /*

        @Override
        public int hashCode(){
            return this.name.hashCode();
        }
         */

        public String toString(){
            return name;
        }
    }

}
