package com.eis0.easypoll.poll;

import com.eis0.easypoll.DataProvider;
import com.eis0.networklibrary.Network;
import com.eis0.networklibrary.NetworkMessageBuilder;
import com.eis0.networklibrary.NetworksPool;
import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.List;

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

    private static PollManager instance = null;

    private SMSManager smsManager = SMSManager.getInstance();
    private DataProvider dataProvider = DataProvider.getInstance();

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
    public void createPoll(String name, String question, List<SMSPeer> users) {
        Network usersNetwork = NetworksPool.obtainNetwork(users);
        BinaryPoll poll = new BinaryPoll(name, question, usersNetwork);
        for(SMSPeer peer : usersNetwork.getPeers()) {
            NetworkMessageBuilder builder = new NetworkMessageBuilder(PollCommands.NEW_POLL)
                    .addArgument(String.valueOf(poll.getLocalId()))
                    .addArgument(poll.getName())
                    .addArgument(poll.getQuestion())
                    .addArguments(usersNetwork.getSubNetForPeer(peer).getAddresses());
            usersNetwork.unicastMessage(peer, builder.buildMessage());
        }
        dataProvider.addPoll(poll);
    }

    // ---------------------------- POLL RECEIVING ---------------------------- //

    /**
     * Receives an SMSMessage and updates poll data accordingly.
     *
     * messageCode is the first header, and it's always one of the following values:
     * [NEW_POLL_MSG_CODE] when the message contains a new poll;
     * [ANSWER_MSG_CODE] when the message is sent from a user to the author and contains an setAnswer;
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
        SMSPeer peer = message.getPeer();
        NetworkMessageBuilder networkMessage = NetworkMessageBuilder.parseNetworkMessage(data);
        String cmd = networkMessage.getCommand();
        List<String> args = networkMessage.getArguments();
        long id = Long.parseLong(args.get(0));
        Network ownerNetwork;
        switch (cmd) {
            // New poll received
            // Structure of the message (each filed is separated by the FIELD_SEPARATOR):
            // NEW_POLL_MSG_CODE + pollId + pollName + pollQuestion
            //        [0]           [1]       [2]          [3]
            case PollCommands.NEW_POLL:
                String name = args.get(1);
                String question = args.get(2);
                List<String> addresses = args.subList(3, args.size());
                List<SMSPeer> users = new ArrayList<>();
                for(String address : addresses) users.add(new SMSPeer(address));
                Network usersNetwork = NetworksPool.obtainNetwork(users);
                ownerNetwork = NetworksPool.obtainNetwork(peer);
                BinaryPoll poll = new BinaryPoll(id, name, question, ownerNetwork, usersNetwork);
                dataProvider.addPoll(poll);
                break;
            // You have received an setAnswer for your poll
            // Structure of the message (each filed is separated by the FIELD_SEPARATOR):
            // ANSWER_MSG_CODE + pollId + author + answerCode
            //        [0]          [1]      [2]       [3]
            case PollCommands.ANSWER_POLL:
                String authorAddress = args.get(1);
                if(authorAddress.equals(NetworksPool.LOCALNET_ADDR)) ownerNetwork = NetworksPool.obtainLocalNetwork();
                else ownerNetwork = NetworksPool.obtainNetwork(new SMSPeer((authorAddress)));
                boolean answer = args.get(2).equals(PollCommands.YES_ANSWER);
                for(BinaryPoll openedPoll : DataProvider.getOpenedPolls()) {
                    if(openedPoll.getLocalId() == id && openedPoll.getAuthors().equals(ownerNetwork)) {
                        openedPoll.setAnswer(answer);
                        dataProvider.updatePoll(openedPoll);
                    }
                }
                break;
        }
    }

    // ---------------------------- POLL ANSWERING ---------------------------- //

    /**
     * Sends the setAnswer to the author and remove the poll from the receivedPolls map.
     *
     * @param poll   The poll to setAnswer.
     * @param answer The user's setAnswer, true equals "Yes" and false equals "No".
     * @throws IllegalArgumentException When `poll` was created by the user answering it.
     * @author Giovanni Velludo
     * @author Matteo Carnelos
     */
    public void answerPoll(BinaryPoll poll, boolean answer) {
        poll.setAnswer(answer);

        String localId = String.valueOf(poll.getLocalId());
        String answerCommand = answer ? PollCommands.YES_ANSWER : PollCommands.NO_ANSWER;

        NetworkMessageBuilder builder = new NetworkMessageBuilder(PollCommands.ANSWER_POLL)
                .addArgument(localId)
                .addArguments(poll.getAuthors().getAddresses())
                .addArgument(answerCommand);
        poll.getUsers().broadcastMessage(builder.buildMessage());

        builder = new NetworkMessageBuilder(PollCommands.ANSWER_POLL)
                .addArgument(localId)
                .addArgument(NetworksPool.LOCALNET_ADDR)
                .addArgument(answerCommand);
        poll.getAuthors().broadcastMessage(builder.buildMessage());

        DataProvider.getIncomingPolls().remove(poll);
        dataProvider.updatePoll(poll);
    }
}
