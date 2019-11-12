package com.eis0.smslibrary;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SMSPeer_Tests {
    @Test
    public void getAddress_returnsExpected() {
        SMSPeer test = new SMSPeer("1234");
        assertEquals(test.getAddress(), "1234");
    }

    @Test
    public void toString_returnsExpected() {
        SMSPeer test = new SMSPeer("1234");
        assertEquals(test.toString(), "1234");
    }

    @Test
    public void isEmpty_withAddress() {
        SMSPeer test = new SMSPeer("12345");
        assertFalse(test.isEmpty());
    }

    @Test
    public void isEmpty_withoutAddress() {
        SMSPeer test = new SMSPeer("");
        assertTrue(test.isEmpty());
    }

    @Test
    public void isValid() {
        SMSPeer test = new SMSPeer("+1234567823445827373662");
        assertFalse(test.isValid());
    }

    @Test
    public void isValid2(){
        SMSPeer test = new SMSPeer("+12334");
        assertTrue(test.isValid());
    }

    @Test
    public void isValid3(){
        SMSPeer test = new SMSPeer("");
        assertFalse(test.isValid());
    }

    @Test
    public void isValid4(){
        SMSPeer test = new SMSPeer("+1233er4"); //I catch exception in the SMSPeer class
        assertFalse(test.isValid());
    }

    @Test
    public void isValid5(){
        SMSPeer test = new SMSPeer("3423541601"); //I catch exception in the SMSPeer class
        assertTrue(test.isValid());
    }

    @Test
    public void hasBoolean(){
        SMSPeer test = new SMSPeer("1234");
        assertFalse(test.hasPrefix());
    }

    @Test
    public void checkEqualPeers() {
        SMSPeer peer1 = new SMSPeer("12345");
        SMSPeer peer2 = new SMSPeer("12345");
        assertEquals(peer1, peer2);
    }

    @Test
    public void checkNotEqualObjects() {
        SMSPeer peer1 = new SMSPeer("12345");
        int anotherObject = 5;
        assertNotEquals(peer1, anotherObject);
    }

    @Test
    public void checkNotEqualWithNull() {
        SMSPeer peer1 = new SMSPeer("12345");
        assertNotEquals(peer1, null);

    }

    @Test
    public void checkHashCode(){
        SMSPeer peer1 = new SMSPeer("12345");
        assertEquals(peer1.hashCode(), "12345".hashCode());
    }
}