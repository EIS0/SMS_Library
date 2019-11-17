package com.eis0.library_demo;

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
 * @author Giovanni Velludo
 * @author Matteo Carnelos
 */
public class PollManager implements ReceivedMessageListener<SMSMessage> {

    // NOTE: FIELD_SEPARATOR is a regex, there are some illegal values (e.g. "*")
    // must not be private for tests to work, not a big deal since it's final
    // TODO: Find a stronger FIELD_SEPARATOR (e.g. Escape Char)
    static final String FIELD_SEPARATOR = "\r";
    static final String NEW_POLL_MSG_CODE = "0";
    static final String ANSWER_MSG_CODE = "1";
    private static final String YES_ANSWER_CODE = "1";
    private static final String NO_ANSWER_CODE = "0";

    // Must always be static, because PollManager is a singleton
    private static PollManager instance = null;
    private PollListener pollListener;
    private SMSManager smsManager = SMSManager.getInstance();

    private static HashMap<Integer, TernaryPoll> sentPolls = new HashMap<>();

    /**
     * PollManager constructor, sets this as the SMSManager listener.
     * It cannot be accessed from outside the class because this follows the Singleton Design Pattern.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    private PollManager() {
        smsManager.addReceiveListener(this);
    }

    /**
     * Returns a new instance of PollManager if none exist, otherwise the one already created as per
     * the Singleton Design Patter.
     * @return The only instance of this class.
     * @author Giovanni Velludo
     */
    public static PollManager getInstance() {
        if(instance == null) instance = new PollManager();
        return instance;
    }

    /**
     * Set the listener that will be called when a new poll or an answer is received.
     * @param listener The listener to wake up. It must implement the PollListener interface.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    void setPollListener(PollListener listener) {
        pollListener = listener;
    }

    /**
     * Creates a new poll and sends it to all the included users, it eventually wakes the listener.
     * @param name The name given to the poll.
     * @param question The question to ask users.
     * @param users Users to which the question should be asked.
     * @throws IllegalArgumentException When users is empty
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    void createPoll(String name, String question, ArrayList<SMSPeer> users)
            throws  IllegalArgumentException {
        if (users.isEmpty()) throw new IllegalArgumentException("Can't create poll with no users");
        TernaryPoll poll = new TernaryPoll(name, question, users);
        sentPolls.put(poll.getPollId(), poll);
        pollListener.onSentPollUpdate(poll);
        /* needs to be last in order for pollListener to be called during unit tests, otherwise they
         * will fail because SMS messages can't be sent.
         */
        sendNewPoll(poll);
    }

    /**
     * Sends a new poll as a text message to each pollUser.
     * @param poll The poll to send.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    private void sendNewPoll(TernaryPoll poll) {
        String message = newPollToMessage(poll);
        for(SMSPeer user : poll.getPollUsers())
            smsManager.sendMessage(new SMSMessage(user, message));
    }

    /**
     * Converts a new poll to the following String:
     * NEW_POLL_MSG_CODE + pollId + pollName + pollQuestion
     * Fields and different pollUsers are separated by the FIELD_SEPARATOR.
     * @param poll The poll to convert.
     * @return The message to send to poll users.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    private static String newPollToMessage(TernaryPoll poll) {
        return NEW_POLL_MSG_CODE + FIELD_SEPARATOR
                + poll.getPollId() + FIELD_SEPARATOR
                + poll.getPollName() + FIELD_SEPARATOR
                + poll.getPollQuestion();
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
     *                we don't perform any checks on the validity of the message here. We should
     *                implement them in the future for added security, as this method is triggered
     *                by any message containing our APP_ID.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    public void onMessageReceived(SMSMessage message) {
        String data = message.getData();
        String[] fields = data.split(FIELD_SEPARATOR);
        int pollId = Integer.parseInt(fields[1]);
        switch(fields[0]) {
            // New poll received
            // Structure of the message (each filed is separated by the FIELD_SEPARATOR):
            // NEW_POLL_MSG_CODE + pollId + pollName + pollQuestion
            //        [0]           [1]       [2]          [3]
            case NEW_POLL_MSG_CODE:
                String pollName = fields[2];
                String pollQuestion = fields[3];
                SMSPeer pollAuthor = message.getPeer();
                TernaryPoll receivedPoll =
                        new TernaryPoll(pollAuthor, pollId, pollName, pollQuestion);
                pollListener.onPollReceived(receivedPoll);
                break;
            // You have received an answer for your poll
            // Structure of the message (each filed is separated by the FIELD_SEPARATOR):
            // ANSWER_MSG_CODE + pollId + answerCode
            //        [0]          [1]       [2]
            case ANSWER_MSG_CODE:
                boolean isYes = fields[2].equals(YES_ANSWER_CODE);
                SMSPeer voter = message.getPeer();
                TernaryPoll answeredPoll = sentPolls.get(pollId);
                if(isYes) answeredPoll.setYes(voter);
                else answeredPoll.setNo(voter);
                sentPolls.put(pollId, answeredPoll);
                pollListener.onSentPollUpdate(answeredPoll);
                break;
        }
    }

    /**
     * Sends the answer to the author and remove the poll from the receivedPolls map.
     * @param poll The poll to answer.
     * @param answer The user's answer, true equals "Yes" and false equals "No".
     * @throws IllegalArgumentException When `poll` was created by the user answering it.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    public void answerPoll(TernaryPoll poll, boolean answer) throws IllegalArgumentException {
        if(poll.getPollAuthor().equals(TernaryPoll.SELF_PEER))
            throw new IllegalArgumentException("Trying to answer an owned poll");
        sendAnswer(poll, answer);
    }

    /**
     * Sends an answer as a text message from a user to the author.
     * @param poll The poll which was answered.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    private void sendAnswer(TernaryPoll poll, boolean answer) {
        String message = answerToMessage(poll.getPollId(), answer);
        smsManager.sendMessage(new SMSMessage(poll.getPollAuthor(), message));
    }

    /**
     * Converts a poll answer to the following String:
     * ANSWER_MSG_CODE + pollId + answerCode
     * Fields are separated by FIELD_SEPARATOR.
     * @return Message to send to poll users.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    private static String answerToMessage(int id, boolean answer) {
        String message = ANSWER_MSG_CODE + FIELD_SEPARATOR + id + FIELD_SEPARATOR;
        if (answer) return message + YES_ANSWER_CODE;
        else return message + NO_ANSWER_CODE;
    }
}