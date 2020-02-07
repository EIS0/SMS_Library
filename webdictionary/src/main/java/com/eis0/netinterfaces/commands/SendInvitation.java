package com.eis0.netinterfaces.commands;

import androidx.annotation.NonNull;

import com.eis0.netinterfaces.Invitation;

/**
 * Command to send an Invitation to a network
 *
 * @author Marco Cognolato
 * @author Giovanni Velludo
 */
public abstract class SendInvitation<I extends Invitation> extends Command {

    protected I invitation;

    /**
     * Constructor for the SendInvitation command, requires data to work
     *
     * @param invitation The Invitation containing the info of the network
     */
    public SendInvitation(@NonNull I invitation) {
        this.invitation = invitation;
    }

    /**
     * Execute the SendInvitation logic: sends a request to join a network
     */
    protected abstract void execute();
}
