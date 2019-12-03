package com.eis0.easypoll;

public class BinaryPollTest {/*

    @Test
    public void addValidUser() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer expectedInPoll = new SMSPeer("654321");
        ArrayList<SMSPeer> users = new ArrayList<>();
        users.add(expectedInPoll);
        BinaryPoll test = new BinaryPoll("ciao", author, users);
        assertEquals(test.hasUser(expectedInPoll), true);

    }


    @Test
    public void hasUser_noExpected() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer notInPoll = new SMSPeer("654321");
        ArrayList<SMSPeer> users = new ArrayList<>();
        BinaryPoll test = new BinaryPoll("ciao", author, users);
        assertEquals(test.hasUser(notInPoll), false);
    }

    @Test
    public void hasUser_expected() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer inPoll = new SMSPeer("654321");
        ArrayList<SMSPeer> users = new ArrayList<>();
        users.add(inPoll);
        BinaryPoll test = new BinaryPoll("cvb", author, users);
        assertEquals(test.hasUser(inPoll), true);
    }

    @Test
    public void getAnswerYes() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer inPoll = new SMSPeer("654321");
        ArrayList<SMSPeer> users = new ArrayList<>();
        users.add(inPoll);
        BinaryPoll test = new BinaryPoll("fuck off", author, users);
        test.setYes(inPoll);
        assertEquals(test.getAnswer(inPoll), "Yes");
    }


    @Test
    public void getAnswerNo() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer inPoll = new SMSPeer("654321");
        ArrayList<SMSPeer> users = new ArrayList<>();
        users.add(inPoll);
        BinaryPoll test = new BinaryPoll("lol", author, users);
        test.setNo(inPoll);
        assertEquals(test.getAnswer(inPoll), "No");
    }


    @Test
    public void getAnswerUnavailable() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer inPoll = new SMSPeer("654321");
        ArrayList<SMSPeer> users = new ArrayList<>();
        users.add(inPoll);
        BinaryPoll test = new BinaryPoll("", author, users);
        assertEquals(test.getAnswer(inPoll), "Unavailable");
    }


    @Test(expected = IllegalArgumentException.class)
    public void getAnswer_noUser() {
        SMSPeer author = new SMSPeer("1234567");
        SMSPeer notInPoll = new SMSPeer("654321");
        ArrayList<SMSPeer> users = new ArrayList<>();
        BinaryPoll test = new BinaryPoll("", author, users);
        test.getAnswer(notInPoll);
    }

    @Test
    public void getPollId() {
        SMSPeer author = new SMSPeer("1234567");
        ArrayList<SMSPeer> users = new ArrayList<>();
        BinaryPoll test = new BinaryPoll("", author, users);
        assertEquals(test.getPollId(), 1);
    }
    */
}
