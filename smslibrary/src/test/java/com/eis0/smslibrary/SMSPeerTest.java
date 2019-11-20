package com.eis0.smslibrary;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SMSPeerTest {

    @Test
    public void getAddress() {
        SMSPeer test = new SMSPeer("1234");
        assertEquals(test.getAddress(), "1234");
    }

    @Test
    public void toString1() {
        SMSPeer test = new SMSPeer("1234");
        assertEquals(test.getAddress(), "1234");
    }

    @Test
    public void isEmpty() {
        SMSPeer test = new SMSPeer("12345");
        assertFalse(test.isEmpty());
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
        SMSPeer test = new SMSPeer("+1233er4");
        assertFalse(test.isValid());
    }

    @Test
    public void isValid5(){
        SMSPeer test = new SMSPeer("111111111111111111111111111111"); //I catch exception in the SMSPeer class
        assertFalse(test.isValid());
    }

    @Test
    public void isValid6(){
        SMSPeer test = new SMSPeer("string with 3423541601 inside"); //I catch exception in the SMSPeer class
        assertFalse(test.isValid());
    }

    @Test
    public void hasBoolean(){
        SMSPeer test = new SMSPeer("1234");
        assertFalse(test.hasPrefix());
    }

    @Test
    public void peerToString(){
        SMSPeer test = new SMSPeer("1234");
        assertEquals(test.toString(), "1234");
    }
}
