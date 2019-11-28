package com.eis0.smslibrary;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SMSPeer_Tests {

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

    @Test(expected = IllegalArgumentException.class)
    public void isValid() {
        SMSPeer test = new SMSPeer("+1234567823445827373662");
    }

    @Test
    public void isValid2(){
        SMSPeer test = new SMSPeer("+12334");
    }

    @Test(expected = IllegalArgumentException.class)
    public void isValid3(){
        SMSPeer test = new SMSPeer("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void isValid4(){
        SMSPeer test = new SMSPeer("+1233er4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void isValid5(){
        SMSPeer test = new SMSPeer("111111111111111111111111111111");
    }

    @Test(expected = IllegalArgumentException.class)
    public void isValid6(){
        SMSPeer test = new SMSPeer("string with 3423541601 inside");
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
