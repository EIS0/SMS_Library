package com.eis0.netinterfaces.commands;

/**
 * Basic structure of a Command to be executed by the CommandExecutor.
 * Created following the
 * <a href="https://refactoring.guru/design-patterns/command">Command Design Pattern</a>
 *
 * @author Edoardo Raimondi (pattern chosen with all the group)
 */
public abstract class Command {

    /**
     * Execute the specific class command
     */
    protected abstract void execute();
}
