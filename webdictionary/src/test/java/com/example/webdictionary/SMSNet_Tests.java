package com.example.webdictionary;

import com.eis0.smslibrary.SMSPeer;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
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

    public class ObjectTest{
        public String text;
        public int num;
        public ObjectTest(){
            text = "test";
            num = 0;
        }
    }
    ObjectTest t = new ObjectTest();
    ObjectTest m = new ObjectTest();
    SMSKey key = new SMSKey(t);
    SMSKey key2 = new SMSKey(m);
    SMSKey key3 = new SMSKey(t);
    @Test
    public void addKey_CheckIfAdded() {
        SMSNetDictionary net = new SMSNetDictionary();
        SMSResource resource = new SMSResource("photo.png");
        SMSResource[] resources = {resource};
        net.add(key, resources);
        assertEquals(net.getAvailableKeys()[0], t);
    }

    @Test
    public void addKey_NoResource_CheckIfAdded() {
        SMSNetDictionary net = new SMSNetDictionary();
        net.add(key, null);
        assertEquals(net.getAvailableKeys()[0], key);
    }

    @Test
    public void addResource_CheckIfAdded() {
        SMSNetDictionary net = new SMSNetDictionary();
        SMSResource resource = new SMSResource("photo.png");
        SMSResource[] resources = {resource};
        net.add(key, resources);
        assertEquals(net.getAvailableResources()[0], resource);
    }

    @Test
    public void removeKey_CheckNoPeer() {
        SMSNetDictionary net = new SMSNetDictionary();
        SMSResource resource = new SMSResource("photo.png");
        SMSResource[] resources = {resource};
        net.add(key, resources);
        net.remove(key);
        assertEquals(net.getAvailableKeys().length, 0);
    }

    @Test
    public void removeKey_CheckNoResources() {
        SMSNetDictionary net = new SMSNetDictionary();
        SMSResource resource = new SMSResource("photo.png");
        SMSResource[] resources = {resource};
        net.add(key, resources);
        net.remove(key);
        assertNull(net.getAvailableResources());
    }

    @Test
    public void addResources_CheckKeyResources() {
        SMSNetDictionary net = new SMSNetDictionary();
        SMSResource resource1 = new SMSResource("photo.png");
        SMSResource resource2 = new SMSResource(t);
        SMSResource resource3 = new SMSResource("test.jpg");
        SMSResource[] resources = {resource1, resource2, resource3};
        net.add(key, resources);
        for (int i = 0; i < resources.length; i++) {
            assertEquals(net.findPeerResources(key)[i], resources[i]);
        }
    }

    @Test
    public void findKeyWithResource() {
        SMSNetDictionary net = new SMSNetDictionary();
        SMSResource resource1 = new SMSResource("photo.png");
        SMSResource resource2 = new SMSResource("home.jpg");
        SMSResource resource3 = new SMSResource("test.jpg");
        SMSResource[] resources1 = {resource1, resource2};
        SMSResource[] resources2 = {resource3};
        net.add(key, resources1);
        net.add(key2, resources2);
        assertEquals(net.findKeyWithResource(resource2), key);
    }

    @Test
    public void addResourcesToAnExistingKey() {
        SMSNetDictionary net = new SMSNetDictionary();
        SMSResource resource1 = new SMSResource("photo.png");
        SMSResource resource2 = new SMSResource("home.jpg");
        SMSResource[] resources1 = {resource1};
        SMSResource[] resources2 = {resource2};
        net.add(key, resources1);
        net.add(key, resources2);
        SMSKey[] keys = net.getAvailableKeys();
        assertEquals(keys.length, 1);
    }

    @Test
    public void addResourcesToAnExistingKey_checkResources() {
       SMSNetDictionary net = new SMSNetDictionary();
       SMSResource resource1 = new SMSResource("photo.png");
       SMSResource resource2 = new SMSResource("home.jpg");
       SMSResource[] resources1 = {resource1};
       SMSResource[] resources2 = {resource2};
       net.add(key, resources1);
       net.add(key, resources2);
       SMSResource[] resources = net.getAvailableResources();
       int cont=0;
       Object[] shouldResources = new Object[2];
        for (SMSResource resource : resources) {
            shouldResources[cont++] = resource.getResource();
        }
       Object[] trueResources = {resource1.getResource(), resource2.getResource()};
       assertEquals(shouldResources[0], trueResources[0]);
       assertEquals(shouldResources[1], trueResources[1]);
}

    @Test
    public void addMultipleKeys(){
        SMSNetDictionary net = new SMSNetDictionary();
        net.add(key, null);
        net.add(key2, null);
        SMSKey[] keys = net.getAvailableKeys();
        assertEquals(keys.length, 1);
    }

    @Test
    public void testHashMapWithSMSkeysEquals1(){
        HashMap<SMSKey, String> hash = new HashMap<>();
        hash.put(key,"" );
        assertTrue(hash.containsKey(key3));
    }


}