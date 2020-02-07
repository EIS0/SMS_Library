package com.eis.communication.network;

/**
 * Basic interface for a Command to be executed by the net.
 * Created following the
 * <a href="https://refactoring.guru/design-patterns/command">Command Design Pattern</a>
 *
 * @author Edoardo Raimondi, idea by Marco Cognolato, Enrico Cestaro
 */
public interface Command {

    /**
     * Execute the specific class command
     */
    void execute();
}
