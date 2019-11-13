package com.eis0.smslibrary;

import org.junit.Test;

import static org.junit.Assert.*;

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
        SMSPeer test = new SMSPeer("12345"); //I expect to be false
        assertEquals(test.isEmpty(), false );
    }

    @Test
    public void isValid(){
        SMSPeer test = new SMSPeer("+3408140326");
        assertEquals(test.isValid(), true);
    }

    @Test
    public void isValid1() {
        SMSPeer test = new SMSPeer("+1234567823445827373662");
        assertEquals(test.isValid(), false );
    }
    @Test
    public void isValid2(){
        SMSPeer test = new SMSPeer("+12334");
        assertEquals(test.isValid(), true);
    }
    @Test
    public void isValid3(){
        SMSPeer test = new SMSPeer("");
        assertEquals(test.isValid(), false);
    }
    @Test
    public void isValid4(){
        SMSPeer test = new SMSPeer("+1233er4"); //I catch exception in the SMSPeer class
        assertEquals(test.isValid(), false);
    }
    @Test
    public void hasBoolean(){
        SMSPeer test = new SMSPeer("1234");
        assertEquals(test.hasPrefix(), false);
    }
}