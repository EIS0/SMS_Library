package com.eis0.library_demo;

import android.util.Pair;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Creates and modifies TernaryPoll objects based on inputs from Activities and SMS Messages.
 * To be notified of changes to polls, your class must implement PollListener, and you must pass it
 * to an instance of PollManager with addPollListener(PollListener listener).
 * Right now it communicates directly with SMSManager, but in the future it will send messages to a
 * class handling messages longer than a single SMS.
 * @author Giovanni Velludo, except where specified otherwise.
 */
public class PollManager implements ReceivedMessageListener<SMSMessage> {

    // must not be private for tests to work, not a big deal since it's final
    static final String FIELD_SEPARATOR = ":";
    private static PollManager instance = null; // Must always be static for getInstance to work
    // TODO: write polls to disk when the program is removed from memory
    private HashMap<Pair<SMSPeer, Integer>, TernaryPoll> incomingPolls = new HashMap<>();
    private HashMap<Integer, TernaryPoll> sentPolls = new HashMap<>();

    private static PollListener pollListener;
    private SMSManager smsManager = SMSManager.getInstance();

    // Singleton Design Pattern
    private PollManager() {
        smsManager.addReceiveListener(this);
    }

    /**
     * Returns a new instance of PollManager if none exist, otherwise the one already created as per
     * the Singleton Design Patter.
     * @return The only instance of this class.
     */
    public static PollManager getInstance() {
        if(instance == null) instance = new PollManager();
        return instance;
    }

    /**
     * Adds the listener listening for incoming TernaryPolls.
     * @param listener The listener to wake up when a message is received. It must implement the
     *                 PollListener interface.
     */
    void setPollListener(PollListener listener) {
        pollListener = listener;
    }

    /**
     * Creates a new poll and sends it to all included users, except for the current user.
     * @param question The question to ask users.
     * @param users Users to which the question should be asked.
     */
    void createPoll(String name, String question, ArrayList<SMSPeer> users) {
        TernaryPoll poll = new TernaryPoll(name, question, users);
        sentPolls.put(poll.getPollID(), poll);
        sendNewPoll(poll);
        pollListener.onSentPollUpdate(poll);
    }

    /**
     * Sets the answer of the user in the local copy of the poll and sends the updated poll to
     * the author.
     * @param poll The poll to answer.
     * @param answer The user's answer, true equals "Yes" and false equals "No".
     */
    public void answerPoll(TernaryPoll poll, boolean answer) {
        if(poll.getPollAuthor().equals(TernaryPoll.SELF_PEER)) throw new IllegalArgumentException("Trying to answer an owned poll");
        Pair<SMSPeer, Integer> key = new Pair<>(poll.getPollAuthor(), poll.getPollID());
        sendAnswer(poll, answer);
        incomingPolls.remove(key);
    }

    /**
     * Receives an SMSMessage and updates poll data accordingly.
     *
     * messageCode is the first header, and it's always one of the following values:
     * 0 when the message contains a new poll;
     * 1 when the message is sent from a user to the author and contains an answer;
     *
     * @param message The SMS messaged passed by SMSHandler. SMSHandler already checks if the
     *                message is meant for our app and strips it of its identification section, so
     *                we don't perform any checks on the validity of the message here. We could
     *                implement them in the future for added security.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    public void onMessageReceived(SMSMessage message) {
        String data = message.getData();
        String[] fields = data.split(FIELD_SEPARATOR);
        int pollID = Integer.parseInt(fields[1]);
        switch(fields[0]) {
            // You have received a new poll
            case "0":
                String pollName = fields[2];
                String pollQuestion = fields[3];
                SMSPeer pollAuthor = message.getPeer();
                ArrayList<SMSPeer> peers = new ArrayList<>();
                for (String destination: Arrays.copyOfRange(fields, 4, fields.length))
                    peers.add(new SMSPeer(destination));
                TernaryPoll receivedPoll = new TernaryPoll(pollName, pollQuestion, pollAuthor, pollID, peers);
                incomingPolls.put(new Pair<>(pollAuthor, pollID), receivedPoll);
                pollListener.onIncomingPoll(receivedPoll);
                break;
            // You have received an answer for your poll
            case "1":
                boolean isYes = fields[2].equals("1");
                SMSPeer voter = message.getPeer().withoutPrefix();
                TernaryPoll answeredPoll = sentPolls.get(pollID);
                if(isYes) answeredPoll.setYes(voter);
                else answeredPoll.setNo(voter);
                sentPolls.put(pollID, answeredPoll);
                pollListener.onSentPollUpdate(answeredPoll);
                break;
        }
    }

    /**
     * Sends a new poll as a text message to each pollUser.
     * @param poll The poll to send.
     */
    private void sendNewPoll(TernaryPoll poll) {
        String message = newPollToMessage(poll);
        for(SMSPeer user : poll.getPollUsers().keySet())
            smsManager.sendMessage(new SMSMessage(user, message));
    }

    /**
     * Converts a new poll to the following String:
     * messageCode + pollID + pollQuestion + pollUsers
     * Fields and different pollUsers are separated by FIELD_SEPARATOR.
     * @param poll The new poll to convert to a String.
     * @return The message to send to poll users.
     */
    private static String newPollToMessage(TernaryPoll poll) {
        String message = "0" + FIELD_SEPARATOR
                + poll.getPollID() + FIELD_SEPARATOR
                + poll.getPollName() + FIELD_SEPARATOR
                + poll.getPollQuestion();
        // Adds each pollUser to the end of the message
        for (SMSPeer user : poll.getPollUsers().keySet()) message += FIELD_SEPARATOR + user;
        return message;
    }

    /**
     * Sends an answer as a text message from a user to the author.
     * @param poll The poll which was answered.
     */
    private void sendAnswer(TernaryPoll poll, boolean answer) {
        String message = answerToMessage(poll.getPollID(), answer);
        smsManager.sendMessage(new SMSMessage(poll.getPollAuthor(), message));
    }

    /**
     * Converts a poll answer to the following String:
     * messageCode + pollID + pollResult
     * Fields are separated by FIELD_SEPARATOR.
     * @return Message to send to poll users.
     */
    private static String answerToMessage(int id, boolean answer) {
        String message = "1" + FIELD_SEPARATOR + id + FIELD_SEPARATOR;
        int result;
        if (answer) result = 1;
        else result = 0;
        return message + result;
    }
}
