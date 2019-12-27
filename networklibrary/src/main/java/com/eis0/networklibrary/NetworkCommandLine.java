package com.eis0.networklibrary;

import java.util.Arrays;

public class NetworkCommandLine {

    private final static String FIELD_SEPARATOR = ":";
    public final static String CREATE_NETWORK_CMD = "\r";

    private String command;
    private String[] arguments;

    public NetworkCommandLine(String command, String[] arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArguments() {
        return arguments;
    }

    public static NetworkCommandLine parseCommandLine(String commandLine) {
        String[] fields = commandLine.split(FIELD_SEPARATOR);
        return new NetworkCommandLine(fields[0], Arrays.copyOfRange(fields, 1, fields.length));
    }

    public String toMessageData() {
        StringBuilder builder = new StringBuilder().append(command);
        for(String argument : arguments) {
            builder.append(FIELD_SEPARATOR);
            builder.append(argument);
        }
        return builder.toString();
    }
}
