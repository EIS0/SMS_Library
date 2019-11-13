package com.eis0.library_demo;

import com.eis0.smslibrary.SMSPeer;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TernaryPollTest {

    @Test
    public void addValidUser() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer expectedInPoll = new SMSPeer("654321");
        ArrayList<SMSPeer> users = new ArrayList<>();
        users.add(expectedInPoll);
        TernaryPoll test = new TernaryPoll("ciao", author, users);
        assertEquals(test.hasUser(expectedInPoll), true);

    }


    @Test
    public void hasUser_noExpected() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer notInPoll = new SMSPeer("654321");
        ArrayList<SMSPeer> users = new ArrayList<>();
        TernaryPoll test = new TernaryPoll("ciao", author, users);
        assertEquals(test.hasUser(notInPoll), false);
    }

    @Test
    public void hasUser_expected() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer inPoll = new SMSPeer("654321");
        ArrayList<SMSPeer> users = new ArrayList<>();
        users.add(inPoll);
        TernaryPoll test = new TernaryPoll("cvb", author, users);
        assertEquals(test.hasUser(inPoll), true);
    }

    @Test
    public void getAnswerYes() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer inPoll = new SMSPeer("654321");
        ArrayList<SMSPeer> users = new ArrayList<>();
        users.add(inPoll);
        TernaryPoll test = new TernaryPoll("fuck off", author, users);
        test.setYes(inPoll);
        assertEquals(test.getAnswer(inPoll), "Yes");
    }


    @Test
    public void getAnswerNo() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer inPoll = new SMSPeer("654321");
        ArrayList<SMSPeer> users = new ArrayList<>();
        users.add(inPoll);
        TernaryPoll test = new TernaryPoll("lol", author, users);
        test.setNo(inPoll);
        assertEquals(test.getAnswer(inPoll), "No");
    }


    @Test
    public void getAnswerUnavailable() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer inPoll = new SMSPeer("654321");
        ArrayList<SMSPeer> users = new ArrayList<>();
        users.add(inPoll);
        TernaryPoll test = new TernaryPoll("", author, users);
        assertEquals(test.getAnswer(inPoll), "Unavailable");
    }


    @Test(expected = IllegalArgumentException.class)
    public void getAnswer_noUser() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer notInPoll = new SMSPeer("654321");
        ArrayList<SMSPeer> users = new ArrayList<>();
        TernaryPoll test = new TernaryPoll("", author, users);
        test.getAnswer(notInPoll);
    }

    @Test
    public void getPollId() {
        SMSPeer author = new SMSPeer("1234567");
        ArrayList<SMSPeer> users = new ArrayList<>();
        TernaryPoll test = new TernaryPoll("", author, users);
        assertEquals(test.getPollId(), 1 );
    }
}
