package com.eis0.easypoll.poll;

import android.util.SparseArray;

import com.eis0.easypoll.DataProvider;
import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;

/**
 * Creates and modifies BinaryPoll objects based on inputs from Activities and SMS Messages.
 * To be notified of changes to polls, your class must implement PollListener, and you must pass it
 * to an instance of PollManager with setPollListener(PollListener listener).
 * Right now it communicates directly with SMSManager, but in the future it will send messages to a
 * class handling messages longer than a single SMS.
 *
 * @author Giovanni Velludo
 * @author Matteo Carnelos
 */
public class PollManager implements ReceivedMessageListener<SMSMessage> {

    // NOTE: FIELD_SEPARATOR is a regex, there are some illegal values (e.g. "*")
    // must not be private for tests to work, not a big deal since it's final
    public static final String FIELD_SEPARATOR = "\r";
    public static final String NEW_POLL_MSG_CODE = "EasyPoll";
    private static final String ANSWER_MSG_CODE = "Answer";
    private static final String YES_MSG_CODE = "Yes";
    private static final String NO_MSG_CODE = "No";

    // Must always be static for getInstance to work
    private static PollManager instance = null;
    private static PollListener pollListener;
    private SMSManager smsManager = SMSManager.getInstance();

    private static SparseArray<BinaryPoll> sentPolls = new SparseArray<>();

    // ---------------------------- SINGLETON CONSTRUCTORS ---------------------------- //

    /**
     * PollManager constructor, sets this as the SMSManager listener.
     * It cannot be accessed from outside the class because this follows the Singleton Design Pattern.
     *
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    private PollManager() {
        smsManager.setReceiveListener(this);
    }

    /**
     * Returns a new instance of PollManager if none exist, otherwise the one already created as per
     * the Singleton Design Patter.
     *
     * @return The only instance of this class.
     * @author Giovanni Velludo
     */
    public static PollManager getInstance() {
        if (instance == null) instance = new PollManager();
        return instance;
    }

    // ---------------------------- DATA STORING ---------------------------- //

    /**
     * Populate the sentPolls SparseArray with the new data usually read from the internal storage.
     *
     * @author Matteo Carnelos
     */
    public static void updateSentPolls() {
        for(BinaryPoll openedPoll : DataProvider.getOpenedPolls())
            sentPolls.put(openedPoll.getPollId(), openedPoll);
    }

    // ---------------------------- LISTENERS MANAGING ---------------------------- //

    /**
     * Set the listener that will be called when a new poll or an answer is received.
     *
     * @param listener The listener to wake up. It must implement the PollListener interface.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    public void setPollListener(PollListener listener) {
        pollListener = listener;
    }

    // ---------------------------- POLL CREATION ---------------------------- //

    /**
     * Creates a new poll and sends it to all the included users, it eventually wakes the listener.
     *
     * @param name     The name given to the poll.
     * @param question The question to ask users.
     * @param users    Users to which the question should be asked.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    public void createPoll(String name, String question, ArrayList<SMSPeer> users) {
        BinaryPoll poll = new BinaryPoll(name, question, users);
        sentPolls.put(poll.getPollId(), poll);
        sendNewPoll(poll);
        pollListener.onSentPollUpdate(poll);
    }

    /**
     * Sends a new poll as a text message to each pollUser.
     *
     * @param poll The poll to send.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    private void sendNewPoll(BinaryPoll poll) {
        String message = newPollToMessage(poll);
        for (SMSPeer user : poll.getPollUsers())
            smsManager.sendMessage(new SMSMessage(user, message));
    }

    /**
     * Converts a new poll to the following String:
     * NEW_POLL_MSG_CODE + pollId + pollName + pollQuestion
     * Fields and different pollUsers are separated by the FIELD_SEPARATOR.
     *
     * @param poll The poll to convert.
     * @return The message to send to poll users.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    private static String newPollToMessage(BinaryPoll poll) {
        return NEW_POLL_MSG_CODE + FIELD_SEPARATOR
                + poll.getPollId() + FIELD_SEPARATOR
                + poll.getPollName() + FIELD_SEPARATOR
                + poll.getPollQuestion();
    }

    // ---------------------------- POLL RECEIVING ---------------------------- //

    /**
     * Receives an SMSMessage and updates poll data accordingly.
     *
     * messageCode is the first header, and it's always one of the following values:
     * [NEW_POLL_MSG_CODE] when the message contains a new poll;
     * [ANSWER_MSG_CODE] when the message is sent from a user to the author and contains an answer;
     *
     * @param message The SMS message passed by SMSHandler. SMSHandler already checks if the
     *                message is meant for our app and strips it of its identification section, so
     *                we don't perform any checks on the validity of the message here. We could
     *                implement them in the future for added security.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    public void onMessageReceived(SMSMessage message) {
        String data = message.getData();
        String[] fields = data.split(FIELD_SEPARATOR);
        int pollId = Integer.parseInt(fields[1]);
        switch (fields[0]) {
            // New poll received
            // Structure of the message (each filed is separated by the FIELD_SEPARATOR):
            // NEW_POLL_MSG_CODE + pollId + pollName + pollQuestion
            //        [0]           [1]       [2]          [3]
            case NEW_POLL_MSG_CODE:
                String pollName = fields[2];
                String pollQuestion = fields[3];
                SMSPeer pollAuthor = message.getPeer();
                BinaryPoll receivedPoll =
                        new BinaryPoll(pollAuthor, pollId, pollName, pollQuestion);
                pollListener.onPollReceived(receivedPoll);
                break;
            // You have received an answer for your poll
            // Structure of the message (each filed is separated by the FIELD_SEPARATOR):
            // ANSWER_MSG_CODE + pollId + answerCode
            //        [0]          [1]       [2]
            case ANSWER_MSG_CODE:
                boolean isYes = fields[2].equals(YES_MSG_CODE);
                SMSPeer voter = message.getPeer();
                BinaryPoll answeredPoll = sentPolls.get(pollId);
                if(isYes) answeredPoll.setYes(voter);
                else answeredPoll.setNo(voter);
                sentPolls.put(pollId, answeredPoll);
                pollListener.onSentPollUpdate(answeredPoll);
                break;
        }
    }

    // ---------------------------- POLL ANSWERING ---------------------------- //

    /**
     * Sends the answer to the author and remove the poll from the receivedPolls map.
     *
     * @param poll   The poll to answer.
     * @param answer The user's answer, true equals "Yes" and false equals "No".
     * @throws IllegalArgumentException When `poll` was created by the user answering it.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    public void answerPoll(BinaryPoll poll, boolean answer) throws IllegalArgumentException {
        if (poll.getPollAuthor() == null)
            throw new IllegalArgumentException("Trying to answer an owned poll");
        sendAnswer(poll, answer);
        pollListener.onPollAnswered(poll);
    }

    /**
     * Sends an answer as a text message from a user to the author.
     *
     * @param poll The poll which was answered.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    private void sendAnswer(BinaryPoll poll, boolean answer) {
        String message = answerToMessage(poll.getPollId(), answer);
        smsManager.sendMessage(new SMSMessage(poll.getPollAuthor(), message));
    }

    /**
     * Converts a poll answer to the following String:
     * ANSWER_MSG_CODE + pollId + answerCode
     * Fields are separated by FIELD_SEPARATOR.
     *
     * @return Message to send to poll users.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    private static String answerToMessage(int id, boolean answer) {
        String message = ANSWER_MSG_CODE + FIELD_SEPARATOR + id + FIELD_SEPARATOR;
        if (answer) return message + YES_MSG_CODE;
        else return message + NO_MSG_CODE;
    }
}
