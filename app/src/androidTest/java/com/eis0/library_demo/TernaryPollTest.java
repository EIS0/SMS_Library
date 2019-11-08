package com.eis0.library_demo;

import com.eis0.smslibrary.SMSPeer;

import org.junit.Test;

import static org.junit.Assert.*;

public class TernaryPollTest {

    @Test
    public void addUser(){
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer expectedInPoll = new SMSPeer("654321");
        TernaryPoll test = new TernaryPoll(author);
        test.addUser(expectedInPoll);
        assertEquals(test.hasUser(expectedInPoll), true);

    }

    @Test
    public void hasUser() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer notInPoll = new SMSPeer("654321");
        TernaryPoll test = new TernaryPoll(author);
        assertEquals(test.hasUser(notInPoll), false );
    }

    @Test
    public void hasUser2() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer inPoll = new SMSPeer("654321");
        TernaryPoll test = new TernaryPoll(author);
        test.addUser(inPoll);
        assertEquals(test.hasUser(inPoll), true);
    }

    @Test
    public void getAnswerYes() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer inPoll = new SMSPeer("654321");
        TernaryPoll test = new TernaryPoll(author);
        test.addUser(inPoll);
        test.setYes(inPoll);
        assertEquals(test.getAnswer(inPoll), "Yes");
    }

    @Test
    public void getAnswerNo() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer inPoll = new SMSPeer("654321");
        TernaryPoll test = new TernaryPoll(author);
        test.addUser(inPoll);
        test.setNo(inPoll);
        assertEquals(test.getAnswer(inPoll), "No");
    }

    @Test
    public void getAnswerUnavailable() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer inPoll = new SMSPeer("654321");
        TernaryPoll test = new TernaryPoll(author);
        test.addUser(inPoll);
        assertEquals(test.getAnswer(inPoll), "Unavailable");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAnswer_noUser(){
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer notInPoll = new SMSPeer("654321");
        TernaryPoll test = new TernaryPoll(author);
        test.getAnswer(notInPoll);
    }

    @Test
    public void getPollId() {
        SMSPeer author = new SMSPeer("1234567");
        TernaryPoll test = new TernaryPoll(author);
        assertEquals(test.getPollId(), 6 );
    }
}