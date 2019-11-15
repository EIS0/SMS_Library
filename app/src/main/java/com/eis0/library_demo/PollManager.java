package com.eis0.library_demo;

import android.util.Pair;
import android.util.SparseArray;

import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Creates and modifies TernaryPoll objects based on inputs from Activities and SMS Messages.
 * To be notified of changes to polls, your class must implement PollListener, and you must pass it
 * to an instance of PollManager with setPollListener(PollListener listener).
 * Right now it communicates directly with SMSManager, but in the future it will send messages to a
 * class handling messages longer than a single SMS.
 * @author Giovanni Velludo, modified by Matteo Carnelos.
 */
public class PollManager implements ReceivedMessageListener<SMSMessage> {

    // NOTE: FIELD_SEPARATOR is a regex, there are some illegal values (e.g. "*")
    private static final String FIELD_SEPARATOR = ":";
    private static final String NEW_POLL_MSG_CODE = "0";
    private static final String ANSWER_MSG_CODE = "1";
    private static final String YES_ANSWER_CODE = "1";
    private static final String NO_ANSWER_CODE = "0";

    // Must always be static for getInstance to work
    private static PollManager instance = null;
    private static PollListener pollListener;
    private SMSManager smsManager = SMSManager.getInstance();

    // TODO: write polls to disk when the program is removed from memory
    private HashMap<Pair<SMSPeer, Integer>, TernaryPoll> receivedPolls = new HashMap<>();
    private SparseArray<TernaryPoll> sentPolls = new SparseArray<>();

    // Singleton Design Pattern
    private PollManager() {
        smsManager.addReceiveListener(this);
    }

    /**
     * Returns a new instance of PollManager if none exist, otherwise the one already created as per
     * the Singleton Design Patter.
     * @return The only instance of this class.
     * @author Giovanni Velludo.
     */
    public static PollManager getInstance() {
        if(instance == null) instance = new PollManager();
        return instance;
    }

    /**
     * Set the listener that will be called when a new poll or an answer is received.
     * @param listener The listener to wake up. It must implements the PollListener interface.
     * @author Giovanni Velludo, modified by Matteo Carnelos.
     */
    public void setPollListener(PollListener listener) {
        pollListener = listener;
    }

    /**
     * Creates a new poll and sends it to all the included users.
     * @param name The name given to the poll.
     * @param question The question to ask users.
     * @param users Users to which the question should be asked.
     * @author Giovanni Velludo, modified by Matteo Carnelos.
     */
    void createPoll(String name, String question, ArrayList<SMSPeer> users) {
        TernaryPoll poll = new TernaryPoll(name, question, users);
        sentPolls.put(poll.pollID, poll);
        sendNewPoll(poll);
        pollListener.onSentPollUpdate(poll);
    }

    /**
     * Sends a new poll as a text message to each pollUser.
     * @param poll The poll to send.
     * @author Giovanni Velludo, modified by Matteo Carnelos.
     */
    private void sendNewPoll(TernaryPoll poll) {
        String message = newPollToMessage(poll);
        for(SMSPeer user : poll.getPollUsers())
            smsManager.sendMessage(new SMSMessage(user, message));
    }

    /**
     * Converts a new poll to the following String:
     * NEW_POLL_MSG_CODE + pollID + pollName + pollQuestion
     * Fields and different pollUsers are separated by the FIELD_SEPARATOR.
     * @param poll The poll to convert.
     * @return The message to send to poll users.
     * @author Giovanni Velludo, modified by Matteo Carnelos.
     */
    private static String newPollToMessage(TernaryPoll poll) {
        String message = NEW_POLL_MSG_CODE + FIELD_SEPARATOR
                + poll.getPollId() + FIELD_SEPARATOR
                + poll.getPollName() + FIELD_SEPARATOR
                + poll.getPollQuestion();
        return message;
    }

    /**
     * Receives an SMSMessage and updates poll data accordingly.
     *
     * messageCode is the first header, and it's always one of the following values:
     * [NEW_POLL_MSG_CODE] when the message contains a new poll;
     * [ANSWER_MSG_CODE] when the message is sent from a user to the author and contains an answer;
     *
     * @param message The SMS messaged passed by SMSHandler. SMSHandler already checks if the
     *                message is meant for our app and strips it of its identification section, so
     *                we don't perform any checks on the validity of the message here. We could
     *                implement them in the future for added security.
     * @author Matteo Carnelos
     */
    public void onMessageReceived(SMSMessage message) {
        String data = message.getData();
        String[] fields = data.split(FIELD_SEPARATOR);
        int pollID = Integer.parseInt(fields[1]);
        switch(fields[0]) {
            // New poll received
            // Structure of the message (each filed is separated by the FIELD_SEPARATOR):
            // NEW_POLL_MSG_CODE + pollID + pollName + pollQuestion
            //        [0]           [1]       [2]          [3]
            case NEW_POLL_MSG_CODE:
                String pollName = fields[2];
                String pollQuestion = fields[3];
                SMSPeer pollAuthor = message.getPeer();
                TernaryPoll receivedPoll = new TernaryPoll(pollID, pollName, pollQuestion, pollAuthor);
                receivedPolls.put(new Pair<>(pollAuthor, pollID), receivedPoll);
                pollListener.onReceivePoll(receivedPoll);
                break;
            // You have received an answer for your poll
            // Structure of the message (each filed is separated by the FIELD_SEPARATOR):
            // ANSWER_MSG_CODE + pollID + answerCode
            //        [0]          [1]       [2]
            case ANSWER_MSG_CODE:
                boolean isYes = fields[2].equals(YES_ANSWER_CODE);
                SMSPeer voter = message.getPeer();
                TernaryPoll answeredPoll = sentPolls.get(pollID);
                if(isYes) answeredPoll.setYes(voter);
                else answeredPoll.setNo(voter);
                sentPolls.put(pollID, answeredPoll);
                pollListener.onSentPollUpdate(answeredPoll);
                break;
        }
    }

    /**
     * Sends the answer to the author and remove the poll from the receivedPolls map.
     * @param answer The user's answer, true equals "Yes" and false equals "No".
     * @author Giovanni Velludo, modified by Matteo Carnelos.
     */
    public void answerPoll(TernaryPoll poll, boolean answer) {
        if(poll.getPollAuthor().equals(TernaryPoll.SELF_PEER)) throw new IllegalArgumentException("Trying to answer an owned poll");
        Pair<SMSPeer, Integer> key = new Pair<>(poll.getPollAuthor(), poll.getPollId());
        sendAnswer(poll, answer);
        receivedPolls.remove(key);
    }

    /**
     * Sends an answer as a text message from a user to the author.
     * @param poll The poll which was answered.
     * @author Giovanni Velludo.
     */
    private void sendAnswer(TernaryPoll poll, boolean answer) {
        String message = answerToMessage(poll.getPollId(), answer);
        smsManager.sendMessage(new SMSMessage(poll.getPollAuthor(), message));
    }

    /**
     * Converts a poll answer to the following String:
     * ANSWER_MSG_CODE + pollID + answerCode
     * Fields are separated by FIELD_SEPARATOR.
     * @return Message to send to poll users.
     * @author Giovanni Velludo, modified by Matteo Carnelos.
     */
    private static String answerToMessage(int id, boolean answer) {
        String message = ANSWER_MSG_CODE + FIELD_SEPARATOR + id + FIELD_SEPARATOR;
        if (answer) return message + YES_ANSWER_CODE;
        else return message + NO_ANSWER_CODE;
    }
}