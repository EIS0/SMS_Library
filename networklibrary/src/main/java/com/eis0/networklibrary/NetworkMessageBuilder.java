package com.eis0.networklibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NetworkMessageBuilder {

    public final static String FIELD_SEPARATOR = ":";

    private String command;
    private List<String> arguments = new ArrayList<>();

    public NetworkMessageBuilder(String command) {
        this.command = command;
    }

    public NetworkMessageBuilder(String command, List<String> arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public NetworkMessageBuilder addArgument(String argument) {
        addArguments(Collections.singletonList(argument));
        return this;
    }

    public NetworkMessageBuilder addArguments(List<String> arguments) {
        this.arguments.addAll(arguments);
        return this;
    }

    public static NetworkMessageBuilder parseNetworkMessage(String commandLine) {
        List<String> fields = Arrays.asList(commandLine.split(FIELD_SEPARATOR));
        return new NetworkMessageBuilder(fields.get(0), fields.subList(1, fields.size()));
    }

    public String buildMessage() {
        StringBuilder builder = new StringBuilder().append(command);
        for(String argument : arguments) {
            builder.append(FIELD_SEPARATOR);
            builder.append(argument);
        }
        return builder.toString();
    }
}
