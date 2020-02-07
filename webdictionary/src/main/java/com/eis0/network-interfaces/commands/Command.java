package com.eis.communication.network.commands;

/**
 * Basic structure of a Command to be executed by the CommandExecutor.
 * Created following the
 * <a href="https://refactoring.guru/design-patterns/command">Command Design Pattern</a>
 *
 * @author Edoardo Raimondi, idea by Marco Cognolato, Enrico Cestaro, Giovanni Velludo
 */
abstract class Command {

    /**
     * Execute the specific class command
     */
    protected abstract void execute();
}
