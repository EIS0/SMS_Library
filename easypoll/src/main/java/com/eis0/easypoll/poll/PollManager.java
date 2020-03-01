package com.eis0.easypoll.poll;

import androidx.annotation.NonNull;

import com.eis0.easypoll.DataProvider;
import com.eis0.networklibrary.Network;
import com.eis0.networklibrary.NetworkMessageBuilder;
import com.eis0.networklibrary.NetworksPool;
import com.eis0.smslibrary.ReceivedMessageListener;
import com.eis0.smslibrary.SMSManager;
import com.eis0.smslibrary.SMSMessage;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Creates and modifies {@link BinaryPoll} objects based on inputs from Activities and SMS messages.<br>
 * Every update to the polls is sent to the {@link DataProvider} class, an {@link java.util.Observable}
 * object that contains all the polls data. It follows the Singleton Design Pattern.
 *
 * @author Matteo Carnelos
 */
public class PollManager implements ReceivedMessageListener<SMSMessage> {

    private static PollManager instance = null;

    private DataProvider dataProvider = DataProvider.getInstance();

    // ---------------------------- SINGLETON CONSTRUCTORS ---------------------------- //

    /**
     * PollManager constructor, sets this as the {@link SMSManager} listener.<br>
     * It cannot be accessed from outside the class because this follows the Singleton Design Pattern.
     *
     * @author Matteo Carnelos
     */
    private PollManager() {
        SMSManager.getInstance().setReceiveListener(this);
    }

    /**
     * Returns a new instance of {@link PollManager} if none exist, otherwise the one already created as per
     * the Singleton Design Patter.
     *
     * @return The only instance of this class.
     * @author Matteo Carnelos
     */
    @NonNull
    public static PollManager getInstance() {
        if (instance == null) instance = new PollManager();
        return instance;
    }

    // ---------------------------- POLL CREATION ---------------------------- //

    /**
     * Create a new poll and broadcast it to all the included users, then update polls data of the
     * {@link DataProvider}.<br>
     *
     * @param name The name given to the poll.
     * @param question The question to ask to users.
     * @param users Users to which the question should be asked.
     * @author Matteo Carnelos
     */
    public void createPoll(@NonNull String name, @NonNull String question, @NonNull List<SMSPeer> users) {
        Network usersNetwork = NetworksPool.obtainNetwork(users);
        BinaryPoll poll = new BinaryPoll(name, question, usersNetwork);
        // A NEW_POLL message follows this pattern:
        // NEW_POLL_CMD + pollNumber + pollName + pollQuestion + [pollUsersAddresses]
        NetworkMessageBuilder builder = new NetworkMessageBuilder(PollCommands.NEW_POLL)
                .addArgument(String.valueOf(poll.getNumber()))
                .addArgument(poll.getName())
                .addArgument(poll.getQuestion())
                .addArguments(usersNetwork.getAddresses());
        usersNetwork.broadcastMessage(builder.buildMessage());
        dataProvider.addPoll(poll);
    }

    // ---------------------------- POLL ANSWERING ---------------------------- //

    /**
     * Send the answer to the author and all the users subscribed to the poll. Finally, remove
     * the answered poll from the incoming polls list.
     *
     * @param poll The poll to answer.
     * @param answer The user's answer, true equals "Yes" and false equals "No".
     * @throws IllegalArgumentException If the author is trying to answer an owning poll.
     * @author Matteo Carnelos
     */
    public void answerPoll(@NonNull BinaryPoll poll, boolean answer) {
        if(poll.getAuthor().isLocalNetwork())
            throw new IllegalArgumentException("Authors are not allowed to answer their polls.");
        String number = String.valueOf(poll.getNumber());
        String answerCode = answer ? PollCommands.YES_ANSWER : PollCommands.NO_ANSWER;
        // The ANSWER_POLL message for users follows this pattern:
        // ANSWER_POLL_CMD + number + authorAddress + answerCode
        NetworkMessageBuilder builder = new NetworkMessageBuilder(PollCommands.ANSWER_POLL)
                .addArgument(number)
                .addArguments(poll.getAuthor().getAddresses())
                .addArgument(answerCode);
        poll.getUsers().broadcastMessage(builder.buildMessage());
        // The ANSWER_POLL message for author follows this pattern:
        // ANSWER_POLL_CMD + number + LOCALNET_ADDR + answerCode
        // The LOCALNET_ADDR tells the author that the answer is for a poll that he created
        builder = new NetworkMessageBuilder(PollCommands.ANSWER_POLL)
                .addArgument(number)
                .addArgument(Network.LOCALNET_ADDR)
                .addArgument(answerCode);
        poll.getAuthor().broadcastMessage(builder.buildMessage());
        DataProvider.getIncomingPolls().remove(poll);
    }

    // ---------------------------- POLL RECEIVING ---------------------------- //

    /**
     * Receives an {@link SMSMessage} and updates polls data accordingly.<br>
     * Every message contains a command line, composed by a command and a variable number of arguments.
     * The command is always one of the following values:<br>
     * NEW_POLL_CMD - When the message contains a new poll;<br>
     * ANSWER_POLL_CMD -  When the message contains an answer.<br>
     * The number and meaning of the arguments depends on the type of command.
     *
     * @param message The SMS message passed by {@link SMSManager}. The message is formatted following
     *                the rules of the {@link NetworkMessageBuilder}.
     * @author Matteo Carnelos
     */
    public void onMessageReceived(@NonNull SMSMessage message) {
        String data = message.getData();
        SMSPeer peer = message.getPeer();
        // Parse the formatted text into a NetworkMessageBuilder object
        // Example: "CMD:ARG1:ARG2:ARG3" => Command:   CMD
        //                                  Arguments: [ARG1, ARG2, ARG3]
        NetworkMessageBuilder networkMessage = NetworkMessageBuilder.parseNetworkMessage(data);
        String cmd = networkMessage.getCommand();
        List<String> args = networkMessage.getArguments();
        long number = Long.parseLong(args.get(0));
        Network authorNetwork;
        switch (cmd) {
            // NEW_POLL message, it follows the structure below:
            // NEW_POLL_CMD + pollNumber + pollName + pollQuestion + [pollUsersAddresses]
            //                   [0]         [1]           [2]             [3...]
            // ---- cmd ----|-------------------------- [args] --------------------------
            case PollCommands.NEW_POLL:
                // Data gathering
                String name = args.get(1);
                String question = args.get(2);
                List<String> addresses = args.subList(3, args.size());
                Network usersNetwork = extractNetworkFromAddresses(addresses);
                authorNetwork = NetworksPool.obtainNetwork(peer);
                // Command execution
                BinaryPoll poll = new BinaryPoll(number, name, question, authorNetwork, usersNetwork);
                dataProvider.addPoll(poll);
                break;
            // ANSWER_POLL message, it follows the structure below:
            // ANSWER_POLL_CMD + pollNumber + authorAddress + answerCode
            //                      [0]           [1]            [2]
            // ------ cmd -----|---------------- [args] ----------------
            case PollCommands.ANSWER_POLL:
                // Data gathering
                String authorAddress = args.get(1);
                authorNetwork = extractNetworkFromAddresses(Collections.singletonList(authorAddress));
                boolean answer = args.get(2).equals(PollCommands.YES_ANSWER);
                // Command execution
                applyAnswerToPoll(number, authorNetwork, answer);
                break;
        }
    }

    /**
     * Extract the correspondent {@link Network} object given a list of addresses.
     *
     * @param addresses The list of addresses in the network. If this list contains only the
     *                  LOCALNET_ADDR, the LOCALNET will be returned.
     * @return The correspondent {@link Network} object.
     * @author Matteo Carnelos
     */
    @NonNull
    private Network extractNetworkFromAddresses(@NonNull List<String> addresses) {
        // Check if the authorAddress is the LOCALNET_ADDR, it means that the extracted network
        // will be the LOCALNET
        if(addresses.size() == 1 && addresses.get(0).equals(Network.LOCALNET_ADDR))
            return NetworksPool.obtainLocalNetwork();
        List<SMSPeer> peers = new ArrayList<>();
        for(String address : addresses) peers.add(new SMSPeer(address));
        return NetworksPool.obtainNetwork(peers);
    }

    /**
     * Apply the given answer to the correspondent poll giving its identification.
     *
     * @param number The number of the poll.
     * @param author The author of the poll.
     * @param answer The answer to apply.
     * @author Matteo Carnelos
     */
    private synchronized void applyAnswerToPoll(long number, @NonNull Network author, boolean answer) {
        for(BinaryPoll openedPoll : DataProvider.getOpenedPolls()) {
            if(openedPoll.getNumber() == number && openedPoll.getAuthor().equals(author)) {
                openedPoll.setAnswer(answer);
                dataProvider.updatePoll(openedPoll);
                break;
            }
        }
    }
}
