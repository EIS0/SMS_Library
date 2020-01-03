package com.eis0.networklibrary;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Class for the creation of formatted, ready-to-send, network messages.<br>
 * A network message is basically a command line composed by one command and a variable number of
 * arguments. The built message is a string containing the command and the arguments concatenated
 * together separated by the {@link #FIELD_SEPARATOR}.<br>
 * Example of built network message: "CMD:ARG1:ARG2:ARG3:..." (FIELD_SEPARATOR = ":")<br>
 * This class follows the principles of the Builder Design Pattern.
 *
 * @author Matteo Carnelos
 */
public class NetworkMessageBuilder {

    final static String FIELD_SEPARATOR = ":";

    private String command;
    private List<String> arguments = new ArrayList<>();

    // ---------------------------- CONSTRUCTORS ---------------------------- //

    /**
     * Create a {@link NetworkMessageBuilder} with the given command.
     *
     * @param command The command for the message.
     * @author Matteo Carnelos
     */
    public NetworkMessageBuilder(@NonNull String command) {
        this.command = command;
    }

    /**
     * Create a {@link NetworkMessageBuilder} with the given command and arguments.
     *
     * @param command The command for the message.
     * @param arguments The arguments for the builder.
     * @author Matteo Carnelos
     */
    public NetworkMessageBuilder(@NonNull String command, @NonNull List<String> arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    // ---------------------------- GETTERS ---------------------------- //

    /**
     * Get the network message command.
     *
     * @return A string representing the command code.
     * @author Matteo Carnelos
     */
    public String getCommand() {
        return command;
    }

    /**
     * Get the network message arguments. It can be an empty list.
     *
     * @return A list representing the arguments codes.
     * @author Matteo Carnelos
     */
    public List<String> getArguments() {
        return arguments;
    }

    // ---------------------------- ADDERS ---------------------------- //

    /**
     * Add an argument to the list of arguments.
     *
     * @param argument The argument to add.
     * @return The new {@link NetworkMessageBuilder} object obtained by the addition.
     * @author Matteo Carnelos
     */
    public NetworkMessageBuilder addArgument(@NonNull String argument) {
        addArguments(Collections.singletonList(argument));
        return this;
    }

    /**
     * Add a list of arguments.
     *
     * @param arguments The arguments to add.
     * @return The new {@link NetworkMessageBuilder} object obtained by the addition.
     * @author Matteo Carnelos
     */
    public NetworkMessageBuilder addArguments(@NonNull List<String> arguments) {
        this.arguments.addAll(arguments);
        return this;
    }

    // ---------------------------- INSPECTIONS ---------------------------- //

    /**
     * Parse a formatted network message into a {@link NetworkMessageBuilder} object.<br>
     * Basically, extract the command and the arguments from the given command line.
     *
     * @param commandLine The network message command line.
     * @return A {@link NetworkMessageBuilder} object initialised with the correspondent command and
     *         arguments.
     * @author Matteo Carnelos
     */
    public static NetworkMessageBuilder parseNetworkMessage(@NonNull String commandLine) {
        List<String> fields = Arrays.asList(commandLine.split(FIELD_SEPARATOR));
        return new NetworkMessageBuilder(fields.get(0), fields.subList(1, fields.size()));
    }

    // ---------------------------- BUILDING ---------------------------- //

    /**
     * Build the message with the command and the arguments of this object.<br>
     * A network message is built following the builder pattern, that is the following:<br>
     * CMD, ARG1, ARG2, ARG3, ...   ->   "CMD:ARG1:ARG2:ARG3:..." (":" is the FIELD_SEPARATOR)
     *
     * @return The formatted message as a string.
     * @author Matteo Carnelos
     */
    public String buildMessage() {
        StringBuilder builder = new StringBuilder().append(command);
        for(String argument : arguments) {
            builder.append(FIELD_SEPARATOR);
            builder.append(argument);
        }
        return builder.toString();
    }
}
